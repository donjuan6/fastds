package per.th.fastds.util;

/**
 * @author th
 * @date 2021/10/6
 * @see
 * @since
 */
public class ThreadUtils {

    public static boolean isInterrupted() {
        return Thread.currentThread().isInterrupted();
    }

    public static void interrupt() {
        Thread.currentThread().interrupt();
    }

}
