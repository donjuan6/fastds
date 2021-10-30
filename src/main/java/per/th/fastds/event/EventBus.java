package per.th.fastds.event;

import java.util.concurrent.Executor;

/**
 * @author th
 * @date 2021/10/6
 * @see
 * @since
 */
public interface EventBus extends Executor {

    void fire(ConnectionPoolDataSourceEvent event);

}
