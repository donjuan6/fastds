package per.th.fastds.util;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

/**
 * @author th
 * @date 2021/10/6
 * @see
 * @since
 */
public class ReflectUtils {

    public static <T> Class<T> forName(String className) {
        ClassLoader loader;
        Exception cause = null;

        for (int i = 0; ; i++) {
            switch (i) {
                case 0: loader = Thread.currentThread().getContextClassLoader(); break;
                case 1: loader = ReflectUtils.class.getClassLoader(); break;
                case 2: loader = ClassLoader.getSystemClassLoader(); break;
                default: throw ExceptionUtils.rethrow(cause);
            }
            try {
                return (Class<T>) loader.loadClass(className);
            } catch (ClassNotFoundException e) {
                cause = e;
            }
        }
    }

    public static <T> T newInstance(Class<T> clazz) {
        Objects.requireNonNull(clazz, "clazz");
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw ExceptionUtils.rethrow(e);
        }
    }

    public static Field getField(Class<?> clazz, String name) throws NoSuchFieldException {
        return getField(clazz, name, false);
    }

    public static Field getField(Class<?> clazz, String name, boolean accessible) throws NoSuchFieldException {
        return findField(clazz, name, accessible)
                .orElseThrow(() -> new NoSuchFieldException(String.format(
                        "Field '%s' not found for %s!", name, clazz)));
    }

    public static Optional<Field> findField(Class<?> clazz, String name, boolean accessible) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(name, "name");

        Field res;
        try {
            if (accessible) {
                res = clazz.getDeclaredField(name);
                res.setAccessible(accessible);
            } else {
                res = clazz.getField(name);
            }
            return Optional.of(res);
        } catch (NoSuchFieldException e) {
            if (accessible) {
                return findField(clazz.getSuperclass(), name, accessible);
            } else {
                return Optional.empty();
            }
        }
    }

    public static <T> T getValue(Field field, Object object) throws IllegalAccessException {
        return (T) field.get(object);
    }

    public static <T> T getValue(Class<?> clazz, String name) throws NoSuchFieldException, IllegalAccessException {
        return (T) getField(clazz, name, true).get(null);
    }
}
