package jon.marketdata.stocker.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Getter
@Setter
@Component
@ConfigurationProperties("alphavantage-intraday")
public class AlphavantageIntradayConn extends AlphavantageUrlConn {
    private String interval;

    @Override
    public String getQueryFullUri(String symbol){
        return new StringBuilder(super.getQueryFullUri(symbol))
                .append("&")
                .append(this.interval)
                .append("&")
                .append(this.apikey)
                .toString();
    }

    @Override
    public String getQueryFullUrl(String symbol) {
        return super.getQueryFullUrl(symbol);
    }
}
