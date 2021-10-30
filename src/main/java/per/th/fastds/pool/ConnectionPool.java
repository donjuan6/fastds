package per.th.fastds.pool;

import per.th.fastds.event.PooledConnectionAcquiredEvent;
import per.th.fastds.event.PooledConnectionCreatedEvent;
import per.th.fastds.event.DefaultEventBus;
import per.th.fastds.event.EventBus;
import per.th.fastds.event.PooledConnectionFatalEvent;
import per.th.fastds.event.PooledConnectionReleaseEvent;
import per.th.fastds.util.AtomicObjectArrayUpdater;
import per.th.fastds.util.ExceptionUtils;
import per.th.fastds.util.ThreadUtils;

import javax.sql.ConnectionPoolDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author th
 * @date 2021/10/6
 * @see
 * @since
 */
abstract
class ConnectionPool
        extends ConnectionPoolConfig
        implements ConnectionPoolDataSource {

    private volatile int numActived;
    private volatile int numUsed;

    private EventBus eventBus;
    private ThreadIndex index;
    private ConnectionObject[] segments;
    private ConnectionObject[] connections;

    private AtomicIntegerFieldUpdater<ConnectionPool> numUsedUpdater;
    private AtomicIntegerFieldUpdater<ConnectionPool> numActivedUpdater;
    private AtomicObjectArrayUpdater<ConnectionObject> segmentsUpdater;
    private AtomicObjectArrayUpdater<ConnectionObject> connectionsUpdater;

    public void init() throws Exception {
        this.eventBus = new DefaultEventBus();
        this.segments = new ConnectionObject[maxThread];
        this.index = new ThreadIndex(0, maxThread);
        this.connections = new ConnectionObject[maxConnection];
        this.segmentsUpdater = new AtomicObjectArrayUpdater<>(segments);
        this.connectionsUpdater = new AtomicObjectArrayUpdater<>(connections);
        this.numUsedUpdater = AtomicIntegerFieldUpdater.newUpdater(ConnectionPool.class, "numUsed");
        this.numActivedUpdater = AtomicIntegerFieldUpdater.newUpdater(ConnectionPool.class, "numActived");
        if (initOnRun) {
            initCoreConnections();
        }
    }

    private void initCoreConnections() throws SQLException {
        ConnectionObject[] connections = new ConnectionObject[minConnection];
        for (int i = 0; i < minConnection; i++) {
            connections[i] = getPooledConnection();
        }
        for (int i = 0; i < minConnection; i++) {
            connections[i].close();
        }
    }

    @Override
    public ConnectionObject getPooledConnection() throws SQLException {
        try {
            return getPooledConnection(maxWaitTimeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException | InterruptedException e) {
            throw ExceptionUtils.rethrow(e);
        }
    }

    public ConnectionObject getPooledConnection(long timeout, TimeUnit unit) throws TimeoutException, InterruptedException {
        long deadline = deadline(timeout, unit);
        for (;;) {
            next();
            ConnectionObject e = head();
            if (e == null) {
                if (numActived < maxConnection) {
                    return newConnectionObject();
                } else {
                    continue;
                }
            }
            if (compareAndSetHead(e, e.next)) {
                if (e.isActived(maxWaitTimeout)) {
                    fireConnectionAcquired(e);
                    return e;
                }
            }
            if (isTimeout(deadline)) {
                throw ExceptionUtils.newTimeoutException(timeout, unit);
            }
            if (ThreadUtils.isInterrupted()) {
                throw new InterruptedException();
            }
        }
    }

    private long deadline(long timeout, TimeUnit unit) {
        return System.nanoTime() + unit.toNanos(timeout);
    }

    private boolean isTimeout(long deadline) {
        return System.nanoTime() >= deadline;
    }

    private ConnectionObject newConnectionObject() {
        ConnectionObject res = null;
        try {
            Connection conn = createConnection();
            res = new ConnectionObject(this, conn);
        } catch (SQLException e) {
            fireConnectionFatal(e);
            ExceptionUtils.rethrow(e);
        } finally {
            if (res != null) {
                incAndGetActived();
                incAndGetUsed();
                fireConnectiotnCreated(res);
            }
        }
        return res;
    }

    protected abstract Connection createConnection() throws SQLException;

    @Override
    public ConnectionObject getPooledConnection(String user, String password) throws SQLException {
        throw new UnsupportedOperationException();
    }

    void release(ConnectionObject connectionObject) {
        for (;;) {
            next();
            ConnectionObject t = head();
            connectionObject.next = t;
            if (compareAndSetHead(t, connectionObject)) {
                decAndGetUsed();
                fireConnectionReleased(t);
                return;
            }
        }
    }

    private void close(ConnectionObject connectionObject) {
        try {
            connectionObject.close();
        } catch (SQLException e) {
            ExceptionUtils.rethrow(e);
        } finally {
            decAndGetActive();
        }
    }

    // ------------------------------------------------------------ 事件相关

    private void fireConnectiotnCreated(ConnectionObject connection) {
        eventBus.fire(new PooledConnectionCreatedEvent(this, connection));
    }

    private void fireConnectionAcquired(ConnectionObject connection) {
        eventBus.fire(new PooledConnectionAcquiredEvent(this, connection));
    }

    private void fireConnectionFatal(Throwable cause) {
        eventBus.fire(new PooledConnectionFatalEvent(this, cause));
    }

    private void fireConnectionReleased(ConnectionObject connection) {
        eventBus.fire(new PooledConnectionReleaseEvent(this, connection));
    }

    // ------------------------------------------------------------  CAS操作

    private int incAndGetActived() {
        return numActivedUpdater.incrementAndGet(this);
    }

    private int decAndGetActive() {
        return numActivedUpdater.decrementAndGet(this);
    }

    private int incAndGetUsed() {
        return numUsedUpdater.incrementAndGet(this);
    }

    private int decAndGetUsed() {
        return numUsedUpdater.decrementAndGet(this);
    }

    private boolean compareAndSetHead(ConnectionObject expect, ConnectionObject update) {
        return segmentsUpdater.compareAndSet(index.get(), expect, update);
    }

    private ConnectionObject head() {
        return segmentsUpdater.get(index.get());
    }

    private int next() {
        return index.next();
    }

}
