package cn.snow.completablefuture;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newCachedThreadPool;

@Slf4j
public class CompletableFutureCreator {

    private static final String VALUE = "Fix Value";

    public static void main(String[] args) {
        /**
         * //直接将一个计算结果value包装成CompletableFuture
         * public static <U> CompletableFuture<U> completedFuture(U value);
         */
        createdByValue();
        /**
         * //直接将一段自行代码包装成CompletableFuture
         *         public static CompletableFuture<Void> 	runAsync(Runnable runnable)//无返回值
         *         public static CompletableFuture<Void> 	runAsync(Runnable runnable, Executor executor)//无返回值，指定线程池
         *         public static <U> CompletableFuture<U> 	supplyAsync(Supplier<U> supplier)//有返回值
         *         public static <U> CompletableFuture<U> 	supplyAsync(Supplier<U> supplier, Executor executor)//有返回值，指定连接池
         */
        CompletableFuture<Void> a = CompletableFuture.runAsync(new DemoTask());

        final ExecutorService pool = newCachedThreadPool();
        CompletableFuture<Void> b = CompletableFuture.runAsync(new DemoTask(), pool);

        CompletableFuture<LocalDateTime> c = CompletableFuture.supplyAsync(() -> {
            try {
                return getALocalDateTime();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        });

        CompletableFuture<LocalDateTime> d = CompletableFuture.supplyAsync(() -> {
            try {
                return getALocalDateTime();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }, pool);
        pool.shutdown();
    }

    private static LocalDateTime getALocalDateTime() throws InterruptedException {
        Thread.sleep(3000);
        log.info("Supplier getALocalDateTime complete");
        return LocalDateTime.now();
    }

    private static class DemoTask implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("Runnable task complete");
        }
    }

    private static void createdByValue() {
        CompletableFuture<String> convertValueToCompletableFuture =
                CompletableFuture.completedFuture(VALUE);
        try {
            log.info(convertValueToCompletableFuture.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        log.info(convertValueToCompletableFuture.join());
        log.info(convertValueToCompletableFuture.getNow("Default Value"));
    }

}
