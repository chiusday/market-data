package jon.marketdata.stocker.interfaces;

import jon.marketdata.stocker.config.AlphavantageUrlConn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Slf4j
public abstract class WebDataSource<T extends AlphavantageUrlConn>
    implements DataSource {

    protected T alphavantageRestApiConn;

    @PostConstruct
    protected abstract void setAlphavantageRestApiConn();

    @Override
    public String getStringData(String symbol) {
        String url = alphavantageRestApiConn.getQueryFullUrl(symbol);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);
    }
}
