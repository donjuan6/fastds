package per.th.fastds.util;

import sun.misc.Unsafe;

import java.lang.reflect.Array;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

/**
 * @author th
 * @date 2021/10/7
 * @see
 * @since
 */
public class AtomicObjectArrayUpdater<E> {

    private final Unsafe unsafe;

    private final long base;
    private final long shift;
    private final int length;

    private final E[] array;

    public AtomicObjectArrayUpdater(E[] array) {
        Objects.requireNonNull(array, "Argument 'array'");
        this.unsafe = UnsafeUtils.getUnsafe();
        this.base = unsafe.arrayBaseOffset(array.getClass());
        this.shift = arrayIndexShift(array.getClass());
        this.length = Array.getLength(array);
        this.array = array;
    }

    private int arrayIndexShift(Class<?> clazz) {
        int scale = unsafe.arrayIndexScale(clazz);
        if ((scale & (scale - 1)) != 0) {
            throw new Error("data type scale not a power of two");
        }
        return 31 - Integer.numberOfLeadingZeros(scale);
    }

    private long offset(int index) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException(
                    "index " + index + " out of bound: " + length);
        }
        return ((long) index << shift) + base;
    }

    public E get(int index) {
        return (E) unsafe.getObjectVolatile(array, offset(index));
    }

    public void set(int index, E update) {
        unsafe.putObjectVolatile(array, offset(index), update);
    }

    public boolean compareAndSet(int index, E expect, E update) {
        return unsafe.compareAndSwapObject(array, offset(index), expect, update);
    }

    public E getAndSet(int index, E newValue) {
        E prev;
        do {
            prev = get(index);
        } while (!compareAndSet(index, prev, newValue));
        return prev;
    }

    public final E updateAndGet(int index, UnaryOperator<E> updateFunction) {
        E prev, next;
        do {
            prev = get(index);
            next = updateFunction.apply(prev);
        } while (!compareAndSet(index, prev, next));
        return next;
    }

    public final E getAndAccumulate(int index, E x, BinaryOperator<E> accumulatorFunction) {
        E prev, next;
        do {
            prev = get(index);
            next = accumulatorFunction.apply(prev, x);
        } while (!compareAndSet(index, prev, next));
        return prev;
    }

    public final E accumulateAndGet(int index, E x,
                                    BinaryOperator<E> accumulatorFunction) {
        E prev, next;
        do {
            prev = get(index);
            next = accumulatorFunction.apply(prev, x);
        } while (!compareAndSet(index, prev, next));
        return next;
    }

}
