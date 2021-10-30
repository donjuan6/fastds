package per.th.fastds.event;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author th
 * @date 2021/10/6
 * @see
 * @since
 */
public class DefaultEventBus implements EventBus {

    private final BlockingQueue<Runnable> queue;
    private final EventThread eventThread;

    public DefaultEventBus() {
        this.queue = new LinkedBlockingDeque<>();
        this.eventThread = new EventThread(queue);
    }

    @Override
    public void fire(ConnectionPoolDataSourceEvent event) {
//        System.out.println(event);
    }

    @Override
    public void execute(Runnable command) {
        queue.offer(command);
    }

}
