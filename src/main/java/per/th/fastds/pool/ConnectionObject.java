package per.th.fastds.pool;

import per.th.fastds.jdbc.ConnectionHolder;

import javax.sql.ConnectionEventListener;
import javax.sql.PooledConnection;
import javax.sql.StatementEventListener;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author th
 * @date 2021/10/6
 * @see
 * @since
 */
public
class ConnectionObject extends ConnectionHolder implements PooledConnection {

    public static final int IN_USED = 2;
    public static final int ACTIVED = 1;
    public static final int CLOSED = 8;

    protected ConnectionPool connectionPool;
    protected volatile ConnectionObject next;
    protected volatile int state;

    public ConnectionObject(ConnectionPool connectionPool, Connection connection) {
        super(connection);
        this.connectionPool = connectionPool;
    }

    @Override
    public void close() throws SQLException {
        connectionPool.release(this);
    }

    @Override
    public void addConnectionEventListener(ConnectionEventListener listener) {

    }

    @Override
    public void removeConnectionEventListener(ConnectionEventListener listener) {

    }

    @Override
    public void addStatementEventListener(StatementEventListener listener) {

    }

    @Override
    public void removeStatementEventListener(StatementEventListener listener) {

    }

}
