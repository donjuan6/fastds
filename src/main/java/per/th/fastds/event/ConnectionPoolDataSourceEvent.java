package per.th.fastds.event;

import javax.sql.ConnectionPoolDataSource;
import java.util.EventObject;

/**
 * @author th
 * @date 2021/10/8
 * @see
 * @since
 */
public class ConnectionPoolDataSourceEvent extends EventObject {

    public ConnectionPoolDataSourceEvent(ConnectionPoolDataSource source) {
        super(source);
    }

    @Override
    public ConnectionPoolDataSource getSource() {
        return (ConnectionPoolDataSource) super.getSource();
    }

}
