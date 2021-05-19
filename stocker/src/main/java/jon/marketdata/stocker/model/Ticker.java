package jon.marketdata.stocker.model;

import lombok.*;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Ticker extends TickerBase {
    protected double open;
    protected double close;
    protected double high;
    protected double low;

    public Ticker(String symbol) {
        super(symbol);
    }

    @Builder
    public Ticker(String symbol, double open, double close, double high, double low) {
        this(symbol);
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
    }
}
