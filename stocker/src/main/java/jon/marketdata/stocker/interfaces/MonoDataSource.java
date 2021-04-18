package jon.marketdata.stocker.interfaces;

import reactor.core.publisher.Mono;

public interface MonoDataSource {
    default Mono<String> getStringData() {
        throw new UnsupportedOperationException("getStringData() is not supoorted.");
    }

    Mono<String> getStringData(String symbol);
}
