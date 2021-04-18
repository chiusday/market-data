package jon.marketdata.stocker.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TickerBase {
    protected String symbol;

    public TickerBase(String symbol) {
        this.symbol = symbol;
    }
}
