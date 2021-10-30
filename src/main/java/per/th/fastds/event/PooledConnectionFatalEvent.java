package per.th.fastds.event;

import javax.sql.ConnectionPoolDataSource;

/**
 * @author th
 * @date 2021/10/8
 * @see
 * @since
 */
public class PooledConnectionFatalEvent extends ConnectionPoolDataSourceEvent {

    private final Throwable cause;

    public PooledConnectionFatalEvent(ConnectionPoolDataSource source, Throwable cause) {
        super(source);
        this.cause = cause;
    }

    public Throwable getCause() {
        return cause;
    }

}
