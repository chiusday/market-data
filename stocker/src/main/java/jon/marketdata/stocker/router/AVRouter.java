package jon.marketdata.stocker.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * As of the writing of this class, Router and RouterHandlers don't support application/stream+json anymore.
 * Hence, {@link jon.marketdata.stocker.controller.AlphavantageController} should be used.
 * @see <a href="https://github.com/spring-projects/spring-framework/issues/21283">discussion</a>
 * This class will be updated upated once a native support for object to json stream response.
 *
 * @author chiusday
 *
 */
@Configuration
public class AVRouter {

    @Bean
    public RouterFunction<ServerResponse> routeAV(AVRouterHandler handler) {
        return RouterFunctions.route(GET("/rx/intraday/{id}")
                .and(accept(MediaType.APPLICATION_JSON)), handler::getAVIntradayTickers);
    }
}
