package per.th.fastds.pool;

import per.th.fastds.jdbc.DrivierDataSource;
import per.th.fastds.util.ExceptionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author th
 * @date 2021/10/11
 * @see
 * @since
 */
public class Connector extends Thread {

    private final DataSource dataSource;
    private final BlockingQueue<Runnable> workQueue;

    public Connector(DrivierDataSource dataSource) {
        super("Connection-Create-Thread");
        this.workQueue = new LinkedBlockingQueue<>();
        this.dataSource = Objects.requireNonNull(dataSource);
        this.setDaemon(true);
        start();
    }

    @Override
    public final void run() {
        for (;;) {
            Runnable cmd = takeCommand();
            if (cmd == null) {
                break;
            }
            execute(cmd);
        }
    }

    private Runnable takeCommand() {
        try {
            return workQueue.take();
        } catch (InterruptedException e) {
            return null;
        }
    }

    private void execute(Runnable task) {
        try {
            task.run();
        } catch (Throwable e) {
            ExceptionUtils.uncaughtedException(e);
        }
    }

    public Future<Connection> acquire() {
        return new FutureTask<>(this::connect);
    }

    private Connection connect() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            FutureTask task;
            while (!workQueue.isEmpty()) {
                task = (FutureTask) workQueue.poll();
                task.cancel(false);
            }
            throw ExceptionUtils.rethrow(e);
        }
    }

}
