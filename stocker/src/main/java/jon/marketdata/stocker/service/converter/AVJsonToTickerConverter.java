package jon.marketdata.stocker.service.converter;
import static jon.marketdata.stocker.utils.ObjectConverter.objectMapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import jon.marketdata.stocker.interfaces.ConvertibleAVJsonTicker;
import jon.marketdata.stocker.model.Ticker;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Alphavantage returned json data has space and special character in it's field names.<br>
 * This converter is specific to Alphavantage response json format.<br>
 * <br>
 * Type parameters: {@literal <}T{@literal >} - Ticker covariant that will be return by this funcion
 *                  {@literal <}C{@literal >} - Covariant of {@link jon.marketdata.stocker.interfaces.ConvertibleAVJsonTicker)}
 *                  that will have the fields required to covert the json to object
 *                  without reflection. Although, there are a few reflection used here
 *                  but having the actual fields set significantly improve performance.
 *
 * @author chiusday
 *
 */
@Slf4j
@Component
public class AVJsonToTickerConverter<T extends Ticker ,
        C extends ConvertibleAVJsonTicker<T>>
        implements BiFunction<String, Map<String, String>, T> {

    private final Class<T> tickerType;
    private final Class<C> convertableTickerType;

    public AVJsonToTickerConverter() {
        this.tickerType = null;
        this.convertableTickerType = null;
    }

    public AVJsonToTickerConverter(Class<C> convertableTickerType, Class<T> tickerType) {
        this.tickerType = tickerType;
        this.convertableTickerType = convertableTickerType;
    }

    @Override
    @SneakyThrows
    public T apply(String json, Map<String, String> additionalFields) {
        log.trace("json input -> "+json);
        //{"1. open":"132.8900","2. high":"132.9500","3. low":"132.8700","4. close":"132.9100","5. volume":"24195"}
        Map<String, Double> mapTickerElems = objectMapper.readValue(json,
                new TypeReference<>(){});
        Map<String, String> tickerMap = new HashMap<>(additionalFields);
        C converibleTicker = getCInstance(convertableTickerType);
        for (Entry<String, Double> entry : mapTickerElems.entrySet()) {
            for (String fieldName : converibleTicker.getFields()) {
                if (entry.getKey().contains(fieldName)) {
                    tickerMap.put(fieldName, entry.getValue().toString());
                    break;
                }
            }
        }

        return objectMapper.convertValue(tickerMap, tickerType);
    }

    private String getTickerElements(String json) throws JsonProcessingException {
        JsonNode node = objectMapper.readTree(json);
        return node.get(1).asText();
    }

    private C getCInstance(Class<C> type) throws NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {

        try {
            return type.getConstructor().newInstance();
        } catch (Exception e) {
            log.error("Error getting an instance of {}", type.getClass().getName(), e);
            throw e;
        }
    }
}
