package org.basicmon.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * The BasicMon utilities.
 */
public class BasicMonUtil {

    private static final int UNIT_PREFIX_FACTOR = 1000;
    private static final DecimalFormatSymbols DECIMAL_FORMAT_SYMBOLS = new DecimalFormatSymbols(Locale.US);
    private static final int TEN = 10;
    private static final DecimalFormat UNDER_TEN_FORMAT = new DecimalFormat("0.00", DECIMAL_FORMAT_SYMBOLS);
    private static final int HUNDRED = 100;
    private static final DecimalFormat UNDER_HUNDRED_FORMAT = new DecimalFormat("00.0", DECIMAL_FORMAT_SYMBOLS);
    private static final DecimalFormat DEFAULT_FORMAT = new DecimalFormat("000", DECIMAL_FORMAT_SYMBOLS);
    private static final String UNDEF_STRING = "undef";

    public static double calcStdDev(double variance, long counter) {

        double stdDev = Double.NaN;
        if (counter > 0) {
            if (counter > 1) {
                stdDev = Math.sqrt(variance);
            } else {
                stdDev = 0.0;
            }
        }
        return stdDev;

    }

    /**
     * Code based on javasimon SimonUtil.
     * @param decimal The decimal value to format.
     * @return The formatted decimal.
     */
    public static String formatDecimal(double decimal) {

        if (decimal < TEN) {
            return UNDER_TEN_FORMAT.format(decimal);
        }
        if (decimal < HUNDRED) {
            return UNDER_HUNDRED_FORMAT.format(decimal);
        }
        return DEFAULT_FORMAT.format(decimal);

    }

    /**
     * Code based on javasimon SimonUtil.
     * @param time The time to format.
     * @param unit The unit the time is given in.
     * @return The formatted time.
     */
    public static String formatTime(double time, String unit) {

        if (time < TEN) {
            return UNDER_TEN_FORMAT.format(time) + unit;
        }
        if (time < HUNDRED) {
            return UNDER_HUNDRED_FORMAT.format(time) + unit;
        }
        return DEFAULT_FORMAT.format(time) + unit;

    }

    /**
     * Returns nano-time in human readable form with unit. Number is always from 10 to 9999
     * except for seconds that are the biggest unit used.
     * <p>
     * Code based on javasimon SimonUtil.
     *
     * @param nanos time in nanoseconds
     * @return human readable time string
     */
    public static String presentNanoTime(long nanos) {

        if (nanos == Long.MAX_VALUE) {
            return UNDEF_STRING;
        }
        if (nanos < UNIT_PREFIX_FACTOR) {
            return nanos + " ns";
        }

        double time = nanos;
        time /= UNIT_PREFIX_FACTOR;
        if (time < UNIT_PREFIX_FACTOR) {
            return formatTime(time, " us");
        }

        time /= UNIT_PREFIX_FACTOR;
        if (time < UNIT_PREFIX_FACTOR) {
            return formatTime(time, " ms");
        }

        time /= UNIT_PREFIX_FACTOR;
        return formatTime(time, " s");

    }

}
