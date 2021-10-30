package per.th.fastds.util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author th
 * @date 2021/10/6
 * @see
 * @since
 */
public class ExceptionUtils {

    public static void uncaughtedException(Throwable e) {
        Thread thread = Thread.currentThread();
        thread.getUncaughtExceptionHandler().uncaughtException(thread, e);
    }

    public static RuntimeException rethrow(Throwable e) {
        return cast(e);
    }

    public static TimeoutException newTimeoutException(long timeout, TimeUnit unit) {
        return new TimeoutException("Acquire connection timeout: " + TimeUtils.toString(timeout, unit));
    }

    public static <X extends Throwable> X cast(Throwable e) throws X {
        throw  (X) e;
    }

}
