package jon.marketata.manager.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "message-wait")
public class MessageWaitConfig {
    private int sleep;
    private int snooze;
}
