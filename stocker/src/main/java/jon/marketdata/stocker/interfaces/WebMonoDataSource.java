package jon.marketdata.stocker.interfaces;

import jon.marketdata.stocker.config.AlphavantageUrlConn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.logging.Level;

@Slf4j
public abstract class WebMonoDataSource<T extends AlphavantageUrlConn>
    implements DataSource {

    protected T alphavantageRestApiConn;

    @PostConstruct
    protected abstract void setAlphavantageRestApiConn();

//    @Override
//    public Mono<String> getStringData(String symbol) {
//        return WebClient.create(alphavantageRestApiConn.getUrl())
//                .get().uri(alphavantageRestApiConn.getQueryFullUri(symbol))
//                .retrieve()
//                .bodyToMono(String.class)
//                .log(log.getName(), log.isTraceEnabled()?Level.INFO:Level.WARNING);
//    }

    @Override
    public String getStringData(String symbol) {
        String url = alphavantageRestApiConn.getQueryFullUrl(symbol);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);
    }
}
