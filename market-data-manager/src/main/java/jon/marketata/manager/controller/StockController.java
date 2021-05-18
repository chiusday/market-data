package jon.marketata.manager.controller;

import jon.marketata.manager.model.IntradayTicker;
import jon.marketata.manager.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class StockController {
    public final static String topic = "stock-topic";
    @Autowired private StockService stockService;

    /**
     * Get IntradayTickes
     * @return Flux of IntradayTickers
     */
    @GetMapping(value = "/stock/prices/intraday",
            produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<IntradayTicker> getStockPrices() {
        return stockService.getTickers();
    }
}
