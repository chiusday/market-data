package jon.marketdata.stocker.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaticsTimeSeries {
    private String intraday;
    private String daily;
    private String weekly;
    private String monthly;
}
