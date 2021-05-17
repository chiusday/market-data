package jon.marketdata.stocker;

import com.fasterxml.jackson.databind.ObjectMapper;
import jon.marketdata.stocker.config.AlphavantageStatic;
import jon.marketdata.stocker.model.AVIntradayTicker;
import jon.marketdata.stocker.model.IntradayTicker;
import jon.marketdata.stocker.service.converter.RxAVJsonToIntradayTickerList;
import jon.marketdata.stocker.service.converter.RxAVJsonToTickerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.codec.CodecCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.util.MimeType;

@SpringBootApplication
public class AppStocker {
    @Autowired
    private AlphavantageStatic alphavantageStatic;

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

//    @Bean
//    public CodecCustomizer ndJsonCustomizer(ObjectMapper objectMapper) {
//        Jackson2JsonEncoder jsonEncoder = new Jackson2JsonEncoder(objectMapper,
//                new MimeType("application", "json"), new MimeType("application", "x-ndjson"));
//        return codecs -> codecs.defaultCodecs().jackson2JsonEncoder(jsonEncoder);
//    }
}
