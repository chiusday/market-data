package jon.marketdata.stocker.service.converter;

import jon.marketdata.stocker.interfaces.AVJsonToTickerList;
import jon.marketdata.stocker.model.AVIntradayTicker;
import jon.marketdata.stocker.model.IntradayTicker;
import org.springframework.stereotype.Component;

/**
 * Concrete class of AVJsonToTickerList for {@link jon.marketdata.stocker.model.IntradayTicker}
 * implementation of {@link jon.marketdata.stocker.model.Ticker}.<br>
 * This implements {@link AVJsonToTickerList#getIntervalFieldName()}
 *
 * @author chiusday
 */
@Component
public class AVJsonToIntradayTickerList
        extends AVJsonToTickerList<IntradayTicker, AVIntradayTicker> {

    public final static String intervalFieldName = "priceTime";

    public AVJsonToIntradayTickerList() { super(); }

    public AVJsonToIntradayTickerList(String key,
            AVJsonToTickerConverter<IntradayTicker, AVIntradayTicker> converter) {

        super(key, converter);
    }

    public String getIntervalFieldName() {
        return intervalFieldName;
    }
}
