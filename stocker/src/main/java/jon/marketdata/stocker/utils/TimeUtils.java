package jon.marketdata.stocker.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Helper class for Time related functions. Ideally this should be in a common/core
 * module or jar.
 *
 * @author chiusday
 */
public class TimeUtils {
    public static final ZoneId ZONE_ID_SG = ZoneId.of("Asia/Singapore");

    /**
     * Get timestamp on Singapore time zone
     * @return current timestamp on Singapore timezone
     */
    public static ZonedDateTime GetZonedNow() {
        return ZonedDateTime.ofInstant(Instant.now(), ZONE_ID_SG);
    }
}
