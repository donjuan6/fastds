package per.th.fastds.util;

import sun.misc.Unsafe;

/**
 * @author th
 * @date 2021/10/6
 * @see
 * @since
 */
public class UnsafeUtils {

    private static final Unsafe INSTANCE = getInstance();

    private static Unsafe getInstance() {
        try {
            return ReflectUtils.getValue(Unsafe.class, "theUnsafe");
        } catch (Exception e) {
            throw ExceptionUtils.rethrow(e);
        }
    }

    public static Unsafe getUnsafe() {
        return INSTANCE;
    }

    public static long objectFieldOffset(Class<?> clazz, String name) {
        try {
            return INSTANCE.objectFieldOffset(
                    ReflectUtils.getField(clazz, name, true));
        } catch (NoSuchFieldException e) {
            throw ExceptionUtils.rethrow(e);
        }
    }

    public static long arrayIndexShift(Class<?> clazz) {
        int scale = INSTANCE.arrayIndexScale(clazz);
        if ((scale & (scale - 1)) != 0) {
            throw new Error("data type scale not a power of two");
        }
        return 31 - Integer.numberOfLeadingZeros(scale);
    }

}
