package jon.marketdata.stocker.service.webdatasource;

import jon.marketdata.stocker.config.AlphavantageIntradayConn;
import jon.marketdata.stocker.interfaces.WebMonoDataSource;
import org.springframework.stereotype.Component;

@Component
public class IntradayWebData extends WebMonoDataSource<AlphavantageIntradayConn> {
    private AlphavantageIntradayConn alphavantageIntradayConn;

    public IntradayWebData(AlphavantageIntradayConn alphavantageRestApiConn) {
        this.alphavantageIntradayConn = alphavantageRestApiConn;
    }

    @Override
    protected void setAlphavantageRestApiConn() {
        this.alphavantageRestApiConn = this.alphavantageIntradayConn;
    }
}
