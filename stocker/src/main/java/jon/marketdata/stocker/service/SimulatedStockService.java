package jon.marketdata.stocker.service;

import jon.marketdata.stocker.model.IntradayTicker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.math.RoundingMode;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static jon.marketdata.stocker.utils.NumberUtils.Round;

/**
 * Service to provide simulated stock functionalities
 *
 * @author chiusday
 */
@Slf4j
@Service
public class SimulatedStockService {
    public static final int spread = 5;
    public static final int decimalPlaces = 4;
    public static AtomicLong recordId = new AtomicLong();

    /**
     * Creates a random IntradayTicker object using the provided symbol. The number
     * of created tickers is equal to the provided limit.
     * The recordId is the a class level AtomicLong so it will increment across requests
     *
     * @param symbol value used as the IntradayTicker's symbol
     * @param limit number of random IntradayTickers to create
     * @return Flux of randomly created IntradayTickers
     */
    public Flux<IntradayTicker> simulate(String symbol, long limit) {
        return Flux.just(symbol)
                .map(s -> randomIntradayTicker(s, recordId.incrementAndGet()))
                .repeat(limit - 1);
    }

    /**
     * Creates a randomly created IntradayTicker object. The symbol is taken from the
     * input. The open, close, high and low prices are computed using
     * {@link #getRandomPrice(String, int)}. The priceTime is current timestamp
     *
     * @param symbol value used as the IntradayTicker's symbol
     * @return a randomly generated IntradayTicker object without recordId
     */
    public IntradayTicker randomIntradayTicker(String symbol) {
        return new IntradayTicker(
                symbol,
                getRandomPrice(symbol, spread),
                getRandomPrice(symbol, spread),
                getRandomPrice(symbol, spread),
                getRandomPrice(symbol, spread),
                Instant.now().toString()
        );
    }

    /**
     * Creates a randomly created IntradayTicker object by calling
     * {@link #randomIntradayTicker(String)} then set the recordId input
     *
     * @param symbol value used as the IntradayTicker's symbol
     * @param recId value to use as recordId
     * @return a randomly generated IntradayTicker object
     */
    public IntradayTicker randomIntradayTicker(String symbol, long recId) {
        IntradayTicker ticker = randomIntradayTicker(symbol);
        ticker.setRecordId(recId);
        return ticker;
    }

    /**
     * Generates a price by adding the result of {@link #getIntValue(String)}
     * and {@link #getRandomSpread(int)} rounded by
     * {@link jon.marketdata.stocker.utils.NumberUtils#Round(double, int, RoundingMode)}
     *
     * @param symbol used as parameter for {@link #getIntValue(String)}
     * @param spread used as parameter for {@link #getRandomSpread(int)}
     * @return returns a rounded value of the symbol's ASCII integer value plus the
     * spread.
     */
    private double getRandomPrice(String symbol, int spread) {
        double price = getIntValue(symbol) + getRandomSpread(spread);
        return Round(price, decimalPlaces, RoundingMode.HALF_UP);
    }

    /**
     * Summation of each character's ASCII value of the input symbol
     *
     * @param symbol value whose each character will be summed
     * @return sum of the ASCII value all the characters of the input symbol
     */
    private int getIntValue(String symbol) {
        return symbol.chars().reduce(0, Integer::sum);
    }

    /**
     * {@link Random} times the spread
     *
     * @param spread multiplier to randomize the spread
     * @return returns a random double within the given spread input
     */
    private double getRandomSpread(int spread) {
        return 1 + new Random().nextDouble() * spread;
    }
}
