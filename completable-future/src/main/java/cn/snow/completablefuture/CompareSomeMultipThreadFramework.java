package cn.snow.completablefuture;

import cn.snow.singlethread.DemoTask;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.*;

@Slf4j
public class CompareSomeMultipThreadFramework {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        /**
         * 看结果，最新的，最便捷的框架不一定就是性能最好的。
         * 自定义连接池是多么的重要，哪怕是你觉得只是简单的指定了一下池中core的数量。
         * 这是设置线程池线程数与任务数一致的情况。
         * 18:39:53.610  runByCountDownLatch====2009
         * 18:39:57.617  runByCompletableFuture====4004 默认采用cpu核数
         * 18:39:59.620  runByCompletableFutureWithExecutor====2002 fix
         * 18:40:19.627  runByCompletableFutureWithExecutor====20007 single
         * 18:40:21.629  runByCompletableFutureWithExecutor====2002 cached
         * 18:40:23.631  runByCompletableFutureWithExecutor====2002 workstealing
         * 18:40:25.668  runByListenableFuture====2037
         * 这是设置线程池线程数与电脑cpu核数一致的情况
         * 19:50:02.286  - runByCountDownLatch====2008
         * 19:50:06.292  - runByCompletableFuture====4004
         * 19:50:10.293  - runByCompletableFutureWithExecutor====4001 workstealing
         * 19:50:14.297  - runByCompletableFutureWithExecutor====4001 fix
         * 19:50:34.304  - runByCompletableFutureWithExecutor====20007 single
         * 19:50:36.307  - runByCompletableFutureWithExecutor====2002 cached
         * 19:50:40.345  - runByListenableFuture====4038
         */
        runByCountDownLatch();
        runByCompletableFuture();
        runByCompletableFutureWithStealingExecutor();
        runByCompletableFutureWithFixExecutor();
        runByCompletableFutureWithSingleExecutor();
        runByCompletableFutureWithCachedExecutor();
        runByListenableFuture();
    }

    private static void runByCompletableFutureWithStealingExecutor() {
        ExecutorService executor = Executors.newWorkStealingPool(7);
        runByCompletableFutureWithExecutor(executor, "workstealing");
        executor.shutdown();
    }

    private static void runByCompletableFutureWithCachedExecutor() {
        ExecutorService executor = Executors.newCachedThreadPool();
        runByCompletableFutureWithExecutor(executor, "cached");
        executor.shutdown();
    }

    private static void runByCompletableFutureWithSingleExecutor() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        runByCompletableFutureWithExecutor(executor, "single");
        executor.shutdown();
    }

    private static void runByCompletableFutureWithFixExecutor() {
        final ExecutorService executor = Executors.newFixedThreadPool(7);
        runByCompletableFutureWithExecutor(executor, "fix");
        executor.shutdown();
    }

    private static void runByCompletableFutureWithExecutor(final Executor executor, String executorName){
        LocalDateTime start = LocalDateTime.now();
        CompletableFuture[] taskArray = new CompletableFuture[10];
        for (int i = 0; i < 10; i++) {
            taskArray[i] = CompletableFuture.runAsync(new DemoTask(2000), executor);
        }
        CompletableFuture.allOf(taskArray).join();
        LocalDateTime end = LocalDateTime.now();
        log.info("runByCompletableFutureWithExecutor====" + Duration.between(start, end).toMillis() + " " + executorName);
    }

    private static void runByListenableFuture() throws ExecutionException, InterruptedException {
        LocalDateTime start = LocalDateTime.now();
        ExecutorService fixPool = Executors.newFixedThreadPool(7);
        ListeningExecutorService guaveFixPool = MoreExecutors.listeningDecorator(fixPool);
        ListenableFuture[] futureArray = new ListenableFuture[10];
        for (int i = 0; i < 10; i++) {
            futureArray[i] = guaveFixPool.submit(new DemoTask(2000));
        }
        ListenableFuture l = Futures.allAsList(futureArray);
        l.get();
        LocalDateTime end = LocalDateTime.now();
        log.info("runByListenableFuture====" + Duration.between(start, end).toMillis());
        guaveFixPool.shutdown();
    }

    private static void runByCountDownLatch() throws InterruptedException {
        LocalDateTime start = LocalDateTime.now();
        CountDownLatch latch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new CountDownDemoTask(2000, latch).start();
        }
        latch.await();
        LocalDateTime end = LocalDateTime.now();
        log.info("runByCountDownLatch====" + Duration.between(start, end).toMillis());
    }

    private static void runByCompletableFuture() {
        LocalDateTime start = LocalDateTime.now();
        CompletableFuture[] taskArray = new CompletableFuture[10];
        for (int i = 0; i < 10; i++) {
            taskArray[i] = CompletableFuture.runAsync(new DemoTask(2000));
        }
        CompletableFuture.allOf(taskArray).join();
        LocalDateTime end = LocalDateTime.now();
        log.info("runByCompletableFuture====" + Duration.between(start, end).toMillis());
    }

    private static class CountDownDemoTask extends DemoTask {

        private final CountDownLatch latch;

        public CountDownDemoTask(long delayMs, CountDownLatch latch) {
            super(delayMs);
            this.latch = latch;
        }

        @Override
        public void run() {
            super.run();
            latch.countDown();
        }
    }
}
