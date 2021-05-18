package jon.marketata.manager.cache;

import jon.marketata.manager.model.IntradayTicker;

import java.util.concurrent.ConcurrentLinkedQueue;

public class StockCache {
    public static final ConcurrentLinkedQueue<IntradayTicker> IntradayTickerQueue =
            new ConcurrentLinkedQueue<>();
}
