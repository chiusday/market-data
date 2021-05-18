package jon.marketdata.stocker.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("file-data-source")
public class FileDataSourceConfig {
    private String intraday;
}
