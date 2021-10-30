package per.th.fastds.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author tanghuang@sunline.cn create on 2019/6/19
 */
public class Looper {

    public static CountDownLatch loop(int concurrent, Runnable task) {
        return loop(concurrent, 1, task);
    }

    public static CountDownLatch loop(int concurrent, int count, Runnable task) {
        Executor executor = newExecutor(concurrent);
        try {
            return loop(executor, concurrent, count, task);
        } finally {
            if (executor instanceof ExecutorService) {
                ((ExecutorService) executor).shutdown();
            }
        }
    }

    private static Executor newExecutor(int nThreads) {
        if (nThreads > 1) {
            return Executors.newFixedThreadPool(nThreads);
        } else if (nThreads == 1) {
            return Executors.newSingleThreadExecutor();
        } else if (nThreads == 0) {
            return Executors.newCachedThreadPool();
        } else {
            return task -> task.run();
        }
    }

    public static CountDownLatch loop(Executor executor, int concurrent, int count, Runnable task) {
        if (concurrent > 1) {
            CountDownLatch latch = new CountDownLatch(concurrent);
            CountDownLatch start = new CountDownLatch(1);
            for (int i = 0; i < concurrent; i++) {
                executor.execute(() -> {
                    await(start);
                    runTask(task, count, latch);
                });
            }
            start.countDown();
            return latch;
        } else {
            CountDownLatch latch = new CountDownLatch(1);
            runTask(task, count, latch);
            return latch;
        }
    }

    private static void runTask(Runnable task, int count, CountDownLatch latch) {
        for (int j = 0; j < count; j++) {
            try {
                task.run();
            } catch (Throwable e) {
                System.err.println(e);
            }
        }
        latch.countDown();
    }

    private static void await(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}