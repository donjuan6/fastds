package per.th.fastds.util;

import java.util.concurrent.TimeUnit;

/**
 * @author th
 * @date 2021/10/6
 * @see
 * @since
 */
public class TimeUtils {

    public static String toString(long time, TimeUnit unit) {
        switch (unit) {
            case NANOSECONDS: return time + "ns";
            case MICROSECONDS: return time + "us";
            case MILLISECONDS: return time + "ms";
            case SECONDS: return time + "s";
            case MINUTES: return time + "m";
            case HOURS: return time + "h";
            case DAYS: return time + "d";
            default: throw new IllegalArgumentException("Unknown TimeUnit: " + unit);
        }
    }

}
