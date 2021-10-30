package per.th.fastds.pool;

/**
 * @author th
 * @date 2021/10/8
 * @see
 * @since
 */
public class ThreadIndex {

    private final ThreadLocal<int[]> local;
    private final int capacity;

    public ThreadIndex(int initialValue, int capacity) {
        this.local = ThreadLocal.withInitial(() -> new int[] { initialValue });
        this.capacity = capacity;
    }

    public int get() {
        return value()[0];
    }

    public void set(int value) {
        value()[0] = value % capacity;
    }

    public int next() {
        return value()[0] = (value()[0] + 1) % capacity;
    }

    public void remove() {
        local.remove();
    }

    private int[] value() {
        return local.get();
    }

    @Override
    public String toString() {
        return String.valueOf(get());
    }

}
