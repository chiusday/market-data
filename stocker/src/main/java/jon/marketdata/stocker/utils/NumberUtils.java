package jon.marketdata.stocker.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Helper class for Number related functions
 *
 * @author chiusday
 */
public class NumberUtils {

    /**
     * Round the given double value by the given decimal places and rounding mode is
     * also taken from the {@link RoundingMode} enum input
     *
     * @param value double to trim
     * @param places decimal places to trim down to
     * @param roundingMode rounding behaviour according to {@link RoundingMode}
     * @return trimmed down value of a double down to the given decimal places with a
     * rounding behaviour according to the given roundingMode
     */
    public static double Round(double value, int places, RoundingMode roundingMode) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
