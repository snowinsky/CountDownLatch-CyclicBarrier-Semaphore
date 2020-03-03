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
public class CompletableFutureDemo {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        /**
         * 看结果，最新的，最便捷的框架不一定就是性能最好的。
         18:24:06.969 [main] INFO cn.snow.completablefuture.CompletableFutureDemo - runByCountDownLatch====2007
         18:24:10.976 [main] INFO cn.snow.completablefuture.CompletableFutureDemo - runByCompletableFuture====4005
         18:24:13.010 [main] INFO cn.snow.completablefuture.CompletableFutureDemo - runByListenableFuture====2034
         */
        runByCountDownLatch();
        runByCompletableFuture();
        runByListenableFuture();
    }

    private static void runByListenableFuture() throws ExecutionException, InterruptedException {
        LocalDateTime start = LocalDateTime.now();
        ExecutorService fixPool = Executors.newFixedThreadPool(10);
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
