package jon.marketdata.stocker.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

/**
 * Ticker with a time based price point intervals
 *
 * @author chiusday
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class IntradayTicker extends Ticker {
    @Setter(AccessLevel.NONE)
    private String priceTime;

    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
    public void setPriceTime(String time) {
        this.priceTime = time;
    }

    public IntradayTicker(String symbol) {
        this.symbol = symbol;
    }
}
