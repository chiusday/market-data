package jon.marketata.manager.service;

import jon.marketata.manager.config.MessageWaitConfig;
import jon.marketata.manager.model.IntradayTicker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import static jon.marketata.manager.cache.StockCache.IntradayTickerQueue;

/** Provides business functions relating to stocks
 *
 * @author chiusday
 */
@Slf4j
@Service
public class StockService {
    private final MessageWaitConfig waitConfig;

    public StockService(MessageWaitConfig messageWaitConfig) {
        this.waitConfig = messageWaitConfig;
    }
    /**
     * Retrieves tickers from in memory Queue. This Queue could be populated from DB
     * Kafka, etc. This is meant to simulate a stream source of data that may emit
     * data one-by-one after a period of time of processing, hence Flux generate is
     * used her with a Thread sleep inside, which should not be used in production
     * setting.
     * Cycles is used to limit the number of times this will loop waiting for the data
     * source of the Queue to insert data into the queue.
     *
     * @return IntradayTicker from the in-memory Queue. Each ticker emitted is polled
     * from the queue. When the cycles equal or greater than the snooze value, and no
     * new data in the queue, the Flux sink will complete and the generation will stop.
     * The cycle is reset everytime there is a new Ticker in the Queue.
     */
    public Flux<IntradayTicker> getTickers() {
        return Flux.generate(
                IntradayTickerQueue::poll,
                (state, sink) -> {
                    int cycles = 1;
                    while (state==null && (cycles++ <= waitConfig.getSnooze())) {
                        try { Thread.sleep(waitConfig.getSleep()); }
                        catch (InterruptedException e) { e.printStackTrace();}
                        state = IntradayTickerQueue.poll();
                    }
                    if (state==null || cycles >= waitConfig.getSnooze())
                        sink.complete();
                    else
                        sink.next(state);
                    return IntradayTickerQueue.poll();
                });
    }

    /**
     * Retrieves tickers from in memory Queue. This Queue could be populated from DB
     * Kafka, etc. This function will not poll but rather get a copy of what ever is in
     * the queue and return it as a Flux
     *
     * @return a Flux of IntradayTickers copy of all the elements in the Queue
     */
    public Flux<IntradayTicker> getAllTickers() {
        return Flux.just(IntradayTickerQueue.toArray(
                new IntradayTicker[IntradayTickerQueue.size()])
        );
    }
}
