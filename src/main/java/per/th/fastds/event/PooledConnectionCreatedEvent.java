package per.th.fastds.event;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;

/**
 * @author th
 * @date 2021/10/8
 * @see
 * @since
 */
public class PooledConnectionCreatedEvent extends PooledConnectionEvent {

    public PooledConnectionCreatedEvent(ConnectionPoolDataSource dataSource, PooledConnection connection) {
        super(dataSource, connection);
    }

}
