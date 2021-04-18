package jon.marketdata.stocker.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import jon.marketdata.stocker.model.Ticker;
import jon.marketdata.stocker.service.converter.AVJsonToIntradayTickerList;
import jon.marketdata.stocker.service.converter.RxAVJsonToTickerConverter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import static jon.marketdata.stocker.utils.ObjectConverter.objectMapper;

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
public abstract class RxAVJsonToTickerList<T extends Ticker,
            C extends ConvertibleAVJsonTicker<T>>
        implements BiFunction<Flux<String>, String, Flux<T>> {

    private final String key;
    private final RxAVJsonToTickerConverter<T, C> converter;

    public RxAVJsonToTickerList() {
        super();
        this.key = "";
        this.converter = null;
    }

    public RxAVJsonToTickerList(String key, RxAVJsonToTickerConverter<T, C> converter) {
        super();
        this.key = key;
        this.converter = converter;
    }

    public abstract String getIntervalFieldName();

    @Override
    @SneakyThrows
    public Flux<T> apply(Flux<String> json, String symbol) {

        return json.map(jsonData -> {
            try {
                return objectMapper.readTree(jsonData);
            } catch (JsonProcessingException e) {
                log.error("Failed to convert json input to JsonNode:\n{}", jsonData);
                return objectMapper.nullNode();
            }})
                .map(nodeData -> nodeData.findValues(key).get(0))
                .flatMap(objectNode -> {
                    Map<String, JsonNode> mapTickers = objectMapper.convertValue(objectNode, new TypeReference<>(){});
                    return Flux.fromIterable(mapTickers.entrySet())
                            .flatMap(entry -> {
                                Map<String, String> additionalFields = new HashMap<>();
                                additionalFields.put("symbol", symbol);
                                additionalFields.put(getIntervalFieldName(), entry.getKey());
                                return converter.apply(
                                        Flux.just(entry.getValue()), additionalFields);
                            })
                            .onErrorResume(error -> {
                                log.error("Failed to convert to Ticker:\n"+error);
                                return Mono.empty();
                            });
                });
    }
}
