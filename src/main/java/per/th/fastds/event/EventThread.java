package per.th.fastds.event;

import per.th.fastds.util.ExceptionUtils;

import java.util.concurrent.BlockingQueue;

/**
 * @author th
 * @date 2021/10/6
 * @see
 * @since
 */
class EventThread extends Thread {

    private final BlockingQueue<Runnable> queue;

    public EventThread(BlockingQueue<Runnable> queue) {
        super("EventThread");
        this.setDaemon(true);
        this.queue = queue;
        this.start();
    }

    @Override
    public void run() {
        for (;;) {
            try {
                queue.take().run();
            } catch (Throwable e) {
                ExceptionUtils.uncaughtedException(e);
            }
        }
    }

}
