package jon.marketata.manager.model;

import lombok.Data;

@Data
public class IntradayTicker {

    private long recordId;
    private String symbol;
    private double open;
    private double close;
    private double high;
    private double low;
    private String priceTime;
}
