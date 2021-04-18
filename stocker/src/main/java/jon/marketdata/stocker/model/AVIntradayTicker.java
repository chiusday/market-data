package jon.marketdata.stocker.model;

import jon.marketdata.stocker.interfaces.ConvertibleAVJsonTicker;
import lombok.Data;

/**
 * Concrete model class for AlphaVantage Intraday Ticker that can be converted from json
 * to an object. Fields Set from {@link jon.marketdata.stocker.interfaces.ConvertibleAVJsonTicker}
 * must be populated here for the corresponding converter work.
 *
 * @author chiusday
 *
 */
public class AVIntradayTicker extends ConvertibleAVJsonTicker<IntradayTicker> {
    public AVIntradayTicker() {
        super(IntradayTicker.class);
        this.fields.add("open");
        this.fields.add("close");
        this.fields.add("high");
        this.fields.add("low");
        this.fields.add("priceTime");
    }
}
