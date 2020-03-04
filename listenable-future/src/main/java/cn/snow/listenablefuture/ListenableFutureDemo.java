package cn.snow.listenablefuture;

import com.google.common.util.concurrent.*;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.concurrent.*;

@Slf4j
public class ListenableFutureDemo {

    public static void main(String[] args) {
        //先创建普通的ExecutorService
        ExecutorService pool = Executors.newCachedThreadPool();
        //然后把它装饰成ListeningExecutorService
        ListeningExecutorService listeningPool = MoreExecutors.listeningDecorator(pool);
        //执行一个任务，返回的就是ListenableFuture了
        ListenableFuture<String[]> returnStringArray = listeningPool.submit(new Callable<String[]>() {
            @Override
            public String[] call() throws Exception {
                log.info("Call long time task to generate string array.");
                return new String[]{"run", "in", "process"};
            }
        });

        //加的这个监听器，有了这个监听器，终于可以在执行有结果的时候执行成功或者失败后的动作
        //一般，不用自己来定义，而可以直接采用Guava已有的实现就足够了。
        returnStringArray.addListener(new Runnable() {
            @Override
            public void run() {
                log.info("listener ");
            }
        }, pool);

        //给这个ListenableFuture加回调函数
        Futures.addCallback(returnStringArray, new FutureCallback<String[]>() {
            @Override
            public void onSuccess(String @Nullable [] result) {
                log.info("onSuccess={}", Arrays.toString(result));
            }

            @Override
            public void onFailure(Throwable t) {
                log.info("onFailure={}", t);
            }
        }, pool);

        //ListenableFutureTask是实现了ListenableFuture接口的，所以ListenableFuture也是ListenableFuture
        ListenableFutureTask task = ListenableFutureTask.create(new Callable<String[]>() {
            @Override
            public String[] call() throws Exception {
                return new String[0];
            }
        });

        //还可以从Future直接转化成ListenableFuture
        Future<String[]> jdkFuture = Executors.newCachedThreadPool().submit(new Callable<String[]>() {
            @Override
            public String[] call() throws Exception {
                return new String[0];
            }
        });
        ListenableFuture<String[]> listenableFuture = JdkFutureAdapters.listenInPoolThread(jdkFuture);


        Futures.FutureCombiner<String[]> ret = Futures.whenAllComplete(listenableFuture);
        ret.call(new Callable<LocalDate>() {
            @Override
            public LocalDate call() throws Exception {
                return null;
            }
        }, pool);
        ret.callAsync(new AsyncCallable<LocalDate>() {
            @Override
            public ListenableFuture<LocalDate> call() throws Exception {
                return null;
            }
        }, pool);
        ret.run(new Runnable() {
            @Override
            public void run() {

            }
        }, pool);
    }
}
