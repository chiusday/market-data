package jon.marketdata.stocker.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Ticker extends TickerBase {
    protected double open;
    protected double close;
    protected double high;
    protected double low;

    public Ticker(String symbol) {
        super(symbol);
    }
}
