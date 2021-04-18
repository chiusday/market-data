package jon.marketdata.stocker.interfaces;

import static jon.marketdata.stocker.utils.ObjectConverter.objectMapper;

import com.fasterxml.jackson.databind.JsonNode;
import jon.marketdata.stocker.model.Ticker;
import jon.marketdata.stocker.service.converter.AVJsonToIntradayTickerList;
import jon.marketdata.stocker.service.converter.AVJsonToTickerConverter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.BiFunction;

/**
 * Abstarct converter of Alphavantage List of prices. This abstract converts each json
 * ticker data into the supplied Ticker subclass {@literal >}T{@literal >}.<br>
 * {@link AVJsonToIntradayTickerList#getIntervalFieldName()} has to be implemented by
 * the concrete class that will have the specific interval field name (like, priceTime
 * for Intraday Ticker or priceDate for Historical Ticker, etc.)<br>
 * @param <T> - Ticker covariant that will fill up this list
 * @param <C> - ConvertibleAVJsonTicker covariant that will convert the given json ticker
 *           data into the supplied {@literal <}T{@literal >} Ticker covariant.
 *
 * @author chiusday
 */
@Slf4j
@Component
public abstract class AVJsonToTickerList<T extends Ticker,
            C extends ConvertibleAVJsonTicker<T>>
        implements BiFunction<String, String, List<T>> {

    private final String key;
    private final AVJsonToTickerConverter<T, C> converter;

    public AVJsonToTickerList() {
        super();
        this.key = "";
        this.converter = null;
    }

    public AVJsonToTickerList(String key, AVJsonToTickerConverter<T, C> converter) {
        super();
        this.key = key;
        this.converter = converter;
    }

    public abstract String getIntervalFieldName();

    @SneakyThrows
    public List<T> apply(String json, String symbol) {

        List<T> reply = new ArrayList<>();
        JsonNode nodeTickers = objectMapper.readTree(json);

        List<JsonNode> nodes = nodeTickers.findValues(key);
        Iterator<String> tickerTimes = nodes.get(0).fieldNames();
        while (tickerTimes.hasNext()) {
            String tickerTime = tickerTimes.next();
            Map<String, String> additionalFields = new HashMap<>();
            additionalFields.put("symbol", symbol);
            additionalFields.put(getIntervalFieldName(), tickerTime);
            List<JsonNode> n = nodes.get(0).findValues(tickerTime);
            T t = converter.apply(n.get(0).toString(), additionalFields);
            reply.add(t);
        }

        return reply;
    }
}
