package per.th.fastds.event;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;

/**
 * @author th
 * @date 2021/10/8
 * @see
 * @since
 */
public class PooledConnectionAcquiredEvent extends PooledConnectionEvent {

    public PooledConnectionAcquiredEvent(ConnectionPoolDataSource dataSource, PooledConnection connection) {
        super(dataSource, connection);
    }

}
