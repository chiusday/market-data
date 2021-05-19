package jon.marketdata.stocker.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jon.marketdata.stocker.kafka.producer.KafkaKeyedProducer;
import jon.marketdata.stocker.kafka.producer.KafkaProducer;
import jon.marketdata.stocker.model.IntradayTicker;
import jon.marketdata.stocker.service.SimulatedStockService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

import static jon.marketdata.stocker.utils.ObjectConverter.WriteValue;

/**
 * Endpoint controller for simulated stocks data.
 * The {@link MediaType#APPLICATION_STREAM_JSON_VALUE} produced by the endpoints is
 * is supported by Chrome browser. Safari does not support this. Safari will wait for
 * all the elements to be emitted which means the flux to complete before showing the
 * result in the browser.
 *
 * @author chiusday
 */
@RestController
public class SimulatedStockController {
    private final SimulatedStockService simulatedStockService;
    private final KafkaProducer stockProducer;
    private final KafkaKeyedProducer stockKeyedProducer;

    public SimulatedStockController(SimulatedStockService simulatedStockService,
            KafkaProducer stockProducer, KafkaKeyedProducer stockKeyedProducer) {

        this.simulatedStockService = simulatedStockService;
        this.stockProducer = stockProducer;
        this.stockKeyedProducer = stockKeyedProducer;
    }

    /**
     * {@link MediaType#APPLICATION_STREAM_JSON_VALUE} is not supported by Safari
     * Endpoint that will return a Flux of simulated IntradayTicker. The number of
     * IntradayTicker objects returned is capped by the input limit. The symbol input
     * will be used in the generation of the tickers.
     *
     * @param id value that will be used as simulated IntradayTicker symbol
     * @param limit the number of simulated IntradayTicker that will be generated
     * @return Flux of simulated IntradayTicker.
     * {@link MediaType#APPLICATION_STREAM_JSON_VALUE} is not supported by Safari.
     * Safari will wait for Flux to complete before showing the result on the browser
     */
    @GetMapping(value = "/simulate/intraday/{id}/limit/{limit}",
            produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<IntradayTicker> simulateStocks(@PathVariable String id,
            @PathVariable int limit) {

        return simulatedStockService.simulate(id, limit)
                .doOnNext(t -> {
                    try { stockProducer.send(WriteValue(t));}
                    catch (JsonProcessingException e) { e.printStackTrace();}
                });
    }

    /**
     * {@link MediaType#APPLICATION_STREAM_JSON_VALUE} is not supported by Safari
     * Endpoint that will return a Flux of simulated IntradayTicker intervally. The
     * number of IntradayTicker objects returned is capped by the input limit. The
     * symbol input will be used in the generation of the tickers and the frequency
     * of the immission of data is based in the interval input
     *
     * @param id value that will be used as simulated IntradayTicker symbol
     * @param limit the number of simulated IntradayTicker that will be generated
     * @param interval the number of simulated IntradayTicker that will be generated
     * @return Flux of fixed number of simulated IntradayTicker emitted intervally.
     * {@link MediaType#APPLICATION_STREAM_JSON_VALUE} is not supported by Safari.
     * Safari will wait for Flux to complete before showing the result on the browser
     */
    @GetMapping(value = "/simulate/intraday/{id}/limit/{limit}/interval/{interval}",
            produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<IntradayTicker> simulateStocks(@PathVariable String id,
            @PathVariable int limit, @PathVariable int interval) {

        return simulatedStockService.simulate(id, limit)
                .delayElements(Duration.ofMillis(interval))
                .doOnNext(t -> {
                    try { stockProducer.send(WriteValue(t));}
                    catch (JsonProcessingException e) { e.printStackTrace();}
                });
    }

    /**
     * {@link MediaType#APPLICATION_STREAM_JSON_VALUE} is not supported by Safari
     * Endpoint that will return a Flux of simulated IntradayTicker intervally where
     * the symbol is used as kafka message key to ensure sorted feed of the message to
     * the consumers. The number of IntradayTicker objects returned is capped by the
     * input limit. The symbol input will be used in the generation of the tickers and
     * the frequency of the immission of data is based in the interval input
     *
     * @param id value that will be used as simulated IntradayTicker symbol and key of
     *           all kafka messages that will be sent by this endoint
     * @param limit the number of simulated IntradayTicker that will be generated
     * @param interval the number of simulated IntradayTicker that will be generated
     * @return Flux of fixed number of simulated IntradayTicker emitted intervally.
     *      * {@link MediaType#APPLICATION_STREAM_JSON_VALUE} is not supported by Safari.
     *      * Safari will wait for Flux to complete before showing the result on the browser
     */
    @GetMapping(value = "/simulate/intraday/{id}/limit/{limit}/interval/{interval}/sorted",
            produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<IntradayTicker> simulateSortedStocks(@PathVariable String id,
            @PathVariable int limit, @PathVariable int interval) {

        return simulatedStockService.simulate(id, limit)
                .delayElements(Duration.ofMillis(interval))
                .doOnNext(t -> {
                    try { stockKeyedProducer.send(id, WriteValue(t));}
                    catch (JsonProcessingException e) { e.printStackTrace();}
                });
    }
}
