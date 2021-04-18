package jon.marketdata.stocker.interfaces;

import jon.marketdata.stocker.model.Ticker;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Abstract class that has the fields required for converting AlphaVantage json String
 * to object. AlphaVantage has unconventional json field names that has space and
 * special characters.
 * Ex: {"1. open":"132.8900","2. high":"132.9500","3. low":"132.8700","4. close":"132.9100","5. volume":"24195"}
 * It is more expensive to use pure object mapper and reflection to convert the json
 * data into String.
 * This abstract provide set of fields to save some of reflection performance cost.
 * Although there are still some reflection the will happen, but significantly reduced.
 *
 * Type parameters: <T> - Type of Ticker this model will be converted to.
 * @author chiusday
 *
 */
@Data
public abstract class ConvertibleAVJsonTicker<T extends Ticker>{

    @Setter(AccessLevel.NONE)
    public final Class<T> type;
    public String symbol;
    @Setter(AccessLevel.NONE)
    public final Set<String> fields;

    public ConvertibleAVJsonTicker(Class<T> type) {
        this.type = type;
        this.fields = new HashSet<>();
    }
}
