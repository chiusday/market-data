package jon.marketdata.stocker.service;

import jon.marketdata.stocker.config.AlphavantageStatic;
import jon.marketdata.stocker.model.IntradayTicker;
import jon.marketdata.stocker.service.converter.RxAVJsonToIntradayTickerList;
import jon.marketdata.stocker.service.webdatasource.IntradayWebData;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
public class AVService {
    private IntradayWebData intradayWebData;
    private AlphavantageStatic alphavantageStatic;
    private RxAVJsonToIntradayTickerList rxAVJsonToIntradayTickers;

    public AVService(IntradayWebData intradayWebData,
            AlphavantageStatic alphavantageStatic,
            RxAVJsonToIntradayTickerList rxAVJsonToIntradayTickers){

        this.intradayWebData = intradayWebData;
        this.alphavantageStatic = alphavantageStatic;
        this.rxAVJsonToIntradayTickers = rxAVJsonToIntradayTickers;
    }

    public Flux<IntradayTicker> getIntradayList(String symbol){
        String jsonFullData = intradayWebData.getStringData(symbol);
        if (jsonFullData.contains("Error")) return Flux.just(new IntradayTicker(jsonFullData));
        return rxAVJsonToIntradayTickers.apply(Flux.just(jsonFullData), symbol)
                .delayElements(Duration.ofMillis(100));
    }
}
