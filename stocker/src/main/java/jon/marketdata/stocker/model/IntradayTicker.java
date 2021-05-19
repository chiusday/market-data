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
@ToString(callSuper = true)
public class IntradayTicker extends Ticker {
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
    private String priceTime;
    private long recordId;

    public IntradayTicker(String symbol) {
        super(symbol);
    }

    public IntradayTicker(String symbol, double open, double close, double high,
            double low, String priceTime) {

        super(symbol, open, close, high, low);
        this.priceTime = priceTime;
    }
}
