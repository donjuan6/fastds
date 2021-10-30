package per.th.fastds.event;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;

/**
 * @author th
 * @date 2021/10/8
 * @see
 * @since
 */
public class PooledConnectionReleaseEvent extends PooledConnectionEvent {

    public PooledConnectionReleaseEvent(ConnectionPoolDataSource source, PooledConnection connection) {
        super(source, connection);
    }

}
