package jon.marketdata.stocker.service.converter;

import static java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import jon.marketdata.stocker.interfaces.ConvertibleAVJsonTicker;
import jon.marketdata.stocker.model.Ticker;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Delayed;
import java.util.function.BiFunction;

import static jon.marketdata.stocker.utils.ObjectConverter.objectMapper;

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
public class RxAVJsonToTickerConverter <T extends Ticker,
        C extends ConvertibleAVJsonTicker<T>>
        implements BiFunction<Flux<JsonNode>, Map<String, String>, Mono<T>> {

    private final Class<T> tickerType;
    private final Class<C> convertableTickerType;

    public RxAVJsonToTickerConverter() {
        this.tickerType = null;
        this.convertableTickerType = null;
    }

    public RxAVJsonToTickerConverter(Class<C> convertableTickerType, Class<T> tickerType) {
        this.tickerType = tickerType;
        this.convertableTickerType = convertableTickerType;
    }

    @Override
    @SneakyThrows
    public Mono<T> apply(Flux<JsonNode> json, Map<String, String> additionalFields) {
        log.trace("json input -> "+json);
        C converibleTicker = getCInstance(convertableTickerType);
        Set<String> tickerFields =  converibleTicker.getFields();
        //{"1. open":"132.8900","2. high":"132.9500","3. low":"132.8700","4. close":"132.9100","5. volume":"24195"}
        return json.map(a -> objectMapper.convertValue(a, new TypeReference<Map<String, String>>() {}))
                .flatMap(map ->
                        Flux.fromIterable(map.entrySet())
                                .map(entry -> getTickerFieldAndValue(tickerFields, entry))
                                .filter(pair -> !StringUtils.isEmpty(pair.getKey()))
                )
                .mergeWith(Flux.fromIterable(additionalFields.entrySet()))
                .collectMap(Entry::getKey, Entry::getValue)
                .map(tMap -> objectMapper.convertValue(tMap, tickerType));
    }

    private Entry<String, String> getTickerFieldAndValue(Set<String> fields,
            Entry<String, String> entry){

        for (String fieldName : fields) {
            if (entry.getKey().contains(fieldName)) {
                return new AbstractMap.SimpleEntry<>(fieldName, entry.getValue());
            }
        }

        return new AbstractMap.SimpleEntry<>("","");
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
