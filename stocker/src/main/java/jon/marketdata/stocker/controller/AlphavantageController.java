package jon.marketdata.stocker.controller;

import jon.marketdata.stocker.model.IntradayTicker;
import jon.marketdata.stocker.service.AVService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class AlphavantageController {
    private AVService AVService;

    public AlphavantageController(AVService AVService) {
        this.AVService = AVService;
    }

    @GetMapping(value = "/stock/intraday/{id}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<IntradayTicker> getIntradayTickers(@PathVariable String id) {
        return AVService.getIntradayList(id);
    }
}
