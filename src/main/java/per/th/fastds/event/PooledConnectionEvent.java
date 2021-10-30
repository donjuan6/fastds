package per.th.fastds.event;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;

/**
 * @author th
 * @date 2021/10/8
 * @see
 * @since
 */
public class PooledConnectionEvent extends ConnectionPoolDataSourceEvent {

    private final PooledConnection connection;

    public PooledConnectionEvent(ConnectionPoolDataSource source, PooledConnection connection) {
        super(source);
        this.connection = connection;
    }

    public PooledConnection getPooledConnection() {
        return connection;
    }

}
