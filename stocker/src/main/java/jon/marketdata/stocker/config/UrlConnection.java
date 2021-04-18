package jon.marketdata.stocker.config;

import lombok.Data;

@Data
public class UrlConnection {
    public String url;
    public String uri;

    protected String getFullUrl(){
        return new StringBuilder(this.url)
                .append(uri)
                .toString();
    }
}
