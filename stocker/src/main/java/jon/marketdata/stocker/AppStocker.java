package jon.marketdata.stocker;

import jon.marketdata.stocker.config.AlphavantageStatic;
import jon.marketdata.stocker.model.AVIntradayTicker;
import jon.marketdata.stocker.model.IntradayTicker;
import jon.marketdata.stocker.producer.KafkaKeyedProducer;
import jon.marketdata.stocker.producer.KafkaProducer;
import jon.marketdata.stocker.service.converter.RxAVJsonToIntradayTickerList;
import jon.marketdata.stocker.service.converter.RxAVJsonToTickerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AppStocker {
    @Autowired
    private AlphavantageStatic alphavantageStatic;

    //put this in the config
    private final String STOCK_TOPIC = "stock-topic";

    public static void main(String[] args){
        SpringApplication.run(AppStocker.class, args);
    }

    @Bean
    public RxAVJsonToTickerConverter<IntradayTicker, AVIntradayTicker>
            rxAVJsonToIntradayTickerConverter() {

        return new RxAVJsonToTickerConverter<>(AVIntradayTicker.class,
                IntradayTicker.class);
    }

    @Bean
    public RxAVJsonToIntradayTickerList rxAVJsonToIntradayTickers() {
        return new RxAVJsonToIntradayTickerList(
                alphavantageStatic.getTimeSeries().getIntraday(),
                rxAVJsonToIntradayTickerConverter());
    }

    @Bean
    public KafkaProducer stockProducer(){
        return new KafkaProducer(STOCK_TOPIC);
    }

    @Bean
    public KafkaKeyedProducer stockKeyedProducer() {
        return new KafkaKeyedProducer(STOCK_TOPIC);
    }

}
