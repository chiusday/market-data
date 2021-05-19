package jon.marketdata.stocker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import jon.marketdata.stocker.filedatasource.IntradayFileData;
import jon.marketdata.stocker.kafka.producer.KafkaProducer;
import jon.marketdata.stocker.model.IntradayTicker;
import jon.marketdata.stocker.service.converter.RxAVJsonToIntradayTickerList;
import jon.marketdata.stocker.service.webdatasource.IntradayWebData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static jon.marketdata.stocker.utils.ObjectConverter.WriteValue;

@Slf4j
@Service
public class AVService {
    private final IntradayWebData intradayWebData;
    private final IntradayFileData intradayFileData;
    private final RxAVJsonToIntradayTickerList rxAVJsonToIntradayTickers;
    private final KafkaProducer stockProducer;

    public AVService(IntradayWebData intradayWebData,
            IntradayFileData intradayFileData,
            RxAVJsonToIntradayTickerList rxAVJsonToIntradayTickers,
            KafkaProducer stockProducer){

        this.intradayWebData = intradayWebData;
        this.intradayFileData = intradayFileData;
        this.rxAVJsonToIntradayTickers = rxAVJsonToIntradayTickers;
        this.stockProducer = stockProducer;
    }

    public Flux<IntradayTicker> getIntradayList(String symbol){
        String jsonFullData = intradayWebData.getStringData(symbol);
        if (jsonFullData.contains("Error")) return Flux.just(new IntradayTicker(jsonFullData));
        return rxAVJsonToIntradayTickers.apply(Flux.just(jsonFullData), symbol)
                .delayElements(Duration.ofMillis(200))
                .doOnNext(t -> {
                    try { stockProducer.send(WriteValue(t)); }
                    catch (JsonProcessingException e) {
                        log.error("Error converting stock {} to String.", t.toString(), e);}
                })
                .onErrorResume(e -> {
                    log.error("Error sending to to kafka", e);
                    return Mono.just(new IntradayTicker("Error"));
                });
    }
}
