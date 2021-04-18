package jon.marketdata.stocker.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AlphavantageUrlConn extends UrlConnection {
    protected String function;
    protected String symbol;
    protected String apikey;

    public String getQueryFullUri(String symbol){
        return new StringBuilder(super.getUri())
                .append("?")
                .append(this.function)
                .append("&")
                .append(this.symbol)
                .append(symbol)
                .toString();
    }

    public String getQueryFullUrl(String symbol){
        return new StringBuilder(this.url)
                .append(this.getQueryFullUri(symbol))
                .toString();
    }
}
