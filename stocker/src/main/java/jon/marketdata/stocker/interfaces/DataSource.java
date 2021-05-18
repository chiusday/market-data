package jon.marketdata.stocker.interfaces;

public interface DataSource {
    default String getStringData(String symbol) {
        throw new UnsupportedOperationException("getStringData(String) is not suppoted.");
    }

    default String getStringData() {
        throw new UnsupportedOperationException("getStringData() is not supported.");
    }
}
