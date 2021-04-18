package jon.marketdata.stocker.router;

import jon.marketdata.stocker.model.IntradayTicker;
import jon.marketdata.stocker.service.AVService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * As of the writing of this class, Router and RouterHandlers don't support application/stream+json anymore.
 * Hence, {@link jon.marketdata.stocker.controller.AlphavantageController} should be used.
 * @see <a href="https://github.com/spring-projects/spring-framework/issues/21283">discussion</a>
 * This class will be updated upated once a native support for object to json stream response.
 *
 * @author chiusday
 *
 */
@Component
public class AVRouterHandler {

    private AVService avService;

    public AVRouterHandler(AVService avService) {
        this.avService = avService;
    }

    public Mono<ServerResponse> getAVIntradayTickers(ServerRequest request) {
        return request.bodyToMono(String.class)
                .flatMap(symbol -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_STREAM_JSON)
                        .body(avService.getIntradayList(symbol), IntradayTicker.class)
                );
    }
}
