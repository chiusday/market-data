package jon.marketdata.stocker.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("alphavantage-static")
public class AlphavantageStatic {
    private StaticsTimeSeries timeSeries;
}
