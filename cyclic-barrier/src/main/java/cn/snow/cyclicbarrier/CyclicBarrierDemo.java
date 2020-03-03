package cn.snow.cyclicbarrier;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier会保证多个线程一起到达某种状态，然后同时做某件事情。
 * 它阻塞子线程
 */
@Slf4j
public class CyclicBarrierDemo {

    public static void main(String[] args) {
        CyclicBarrier cb = new CyclicBarrier(3, () -> log.info("The post process is running."));
        new SubTask(cb, 1000).start();
        new SubTask(cb, 3000).start();
        new SubTask(cb, 2000).start();
        new SubTask(cb, 2400).start();
        new SubTask(cb, 1400).start();
        new SubTask(cb, 2800).start();
        new SubTask(cb, 3500).start();
        new SubTask(cb, 2500).start();
        new SubTask(cb, 1500).start();
    }

    /**
     * CyclicBarrier可以设置到达barrier的个数和数量够了之后立刻执行的任务。
     *   1.子线程在调用await方式时阻塞，一直等到调用同一个barrier的await方法线程个数够数。
     *   2.当到达await位置的线程数够数时，立即执行的是barrier设置的后继执行的任务。而不是各个子线程的await之后的代码。
     *   3.执行完够数后的代码后，立刻同时执行await之后的代码。
     * 注意：
     *   1.CyclicBarrier是可以复用的，也就是说够数了执行一次barrier的后继处理，然后继续计数，又够数了，又执行一次barrier的后继处理。
     *   2.麻烦的是，总的子任务数必须得是barrier设置数的整数倍，否则，又是无穷的等待。为了避免傻等，请为await方法设置超时时间。
     *
     * 这玩意的应用场景更像是公共汽车，人满了就立刻发车，然后继续等车上人满，人满了又发一车。可惜，一旦人一直不满，就一直不走。
     * 我还想到的一个应用场景就是数据分页，每页50行，每满50行写一页，每满50行写一页。
     *
     *
     * D:\dev_tools\java8\jdk\bin\java.exe -javaagent:D:\dev_tools\ideaIU-2018.2.8.win\lib\idea_rt.jar=60362:D:\dev_tools\ideaIU-2018.2.8.win\bin -Dfile.encoding=UTF-8 -classpath D:\dev_tools\java8\jdk\jre\lib\charsets.jar;D:\dev_tools\java8\jdk\jre\lib\deploy.jar;D:\dev_tools\java8\jdk\jre\lib\ext\access-bridge-64.jar;D:\dev_tools\java8\jdk\jre\lib\ext\cldrdata.jar;D:\dev_tools\java8\jdk\jre\lib\ext\dnsns.jar;D:\dev_tools\java8\jdk\jre\lib\ext\jaccess.jar;D:\dev_tools\java8\jdk\jre\lib\ext\jfxrt.jar;D:\dev_tools\java8\jdk\jre\lib\ext\localedata.jar;D:\dev_tools\java8\jdk\jre\lib\ext\nashorn.jar;D:\dev_tools\java8\jdk\jre\lib\ext\sunec.jar;D:\dev_tools\java8\jdk\jre\lib\ext\sunjce_provider.jar;D:\dev_tools\java8\jdk\jre\lib\ext\sunmscapi.jar;D:\dev_tools\java8\jdk\jre\lib\ext\sunpkcs11.jar;D:\dev_tools\java8\jdk\jre\lib\ext\zipfs.jar;D:\dev_tools\java8\jdk\jre\lib\javaws.jar;D:\dev_tools\java8\jdk\jre\lib\jce.jar;D:\dev_tools\java8\jdk\jre\lib\jfr.jar;D:\dev_tools\java8\jdk\jre\lib\jfxswt.jar;D:\dev_tools\java8\jdk\jre\lib\jsse.jar;D:\dev_tools\java8\jdk\jre\lib\management-agent.jar;D:\dev_tools\java8\jdk\jre\lib\plugin.jar;D:\dev_tools\java8\jdk\jre\lib\resources.jar;D:\dev_tools\java8\jdk\jre\lib\rt.jar;D:\idea20180208ws\CountDownLatch-CyclicBarrier-Semaphore\cyclic-barrier\target\classes;D:\dev_tools\maven-local-repo\m2\com\google\guava\guava\28.1-jre\guava-28.1-jre.jar;D:\dev_tools\maven-local-repo\m2\com\google\guava\failureaccess\1.0.1\failureaccess-1.0.1.jar;D:\dev_tools\maven-local-repo\m2\com\google\guava\listenablefuture\9999.0-empty-to-avoid-conflict-with-guava\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;D:\dev_tools\maven-local-repo\m2\com\google\code\findbugs\jsr305\3.0.2\jsr305-3.0.2.jar;D:\dev_tools\maven-local-repo\m2\org\checkerframework\checker-qual\2.8.1\checker-qual-2.8.1.jar;D:\dev_tools\maven-local-repo\m2\com\google\errorprone\error_prone_annotations\2.3.2\error_prone_annotations-2.3.2.jar;D:\dev_tools\maven-local-repo\m2\com\google\j2objc\j2objc-annotations\1.3\j2objc-annotations-1.3.jar;D:\dev_tools\maven-local-repo\m2\org\codehaus\mojo\animal-sniffer-annotations\1.18\animal-sniffer-annotations-1.18.jar;D:\dev_tools\maven-local-repo\m2\org\projectlombok\lombok\1.18.10\lombok-1.18.10.jar;D:\dev_tools\maven-local-repo\m2\org\slf4j\slf4j-api\1.7.30\slf4j-api-1.7.30.jar;D:\dev_tools\maven-local-repo\m2\ch\qos\logback\logback-core\1.2.3\logback-core-1.2.3.jar;D:\dev_tools\maven-local-repo\m2\ch\qos\logback\logback-classic\1.2.3\logback-classic-1.2.3.jar cn.snow.cyclicbarrier.CyclicBarrierDemo
     * 15:10:56.642 [Thread-6] INFO cn.snow.cyclicbarrier.SubTask - sub task execute start.3500
     * 15:10:56.642 [Thread-7] INFO cn.snow.cyclicbarrier.SubTask - sub task execute start.2500
     * 15:10:56.642 [Thread-4] INFO cn.snow.cyclicbarrier.SubTask - sub task execute start.1400
     * 15:10:56.642 [Thread-1] INFO cn.snow.cyclicbarrier.SubTask - sub task execute start.3000
     * 15:10:56.642 [Thread-5] INFO cn.snow.cyclicbarrier.SubTask - sub task execute start.2800
     * 15:10:56.642 [Thread-3] INFO cn.snow.cyclicbarrier.SubTask - sub task execute start.2400
     * 15:10:56.642 [Thread-8] INFO cn.snow.cyclicbarrier.SubTask - sub task execute start.1500
     * 15:10:56.642 [Thread-2] INFO cn.snow.cyclicbarrier.SubTask - sub task execute start.2000
     * 15:10:56.642 [Thread-0] INFO cn.snow.cyclicbarrier.SubTask - sub task execute start.1000
     * 15:10:57.646 [Thread-0] INFO cn.snow.cyclicbarrier.SubTask - sub task execute complete.1000
     * 15:10:57.646 [Thread-0] INFO cn.snow.cyclicbarrier.SubTask - sub task execute await before.1000
     * 15:10:58.046 [Thread-4] INFO cn.snow.cyclicbarrier.SubTask - sub task execute complete.1400
     * 15:10:58.046 [Thread-4] INFO cn.snow.cyclicbarrier.SubTask - sub task execute await before.1400
     * 15:10:58.145 [Thread-8] INFO cn.snow.cyclicbarrier.SubTask - sub task execute complete.1500
     * 15:10:58.145 [Thread-8] INFO cn.snow.cyclicbarrier.SubTask - sub task execute await before.1500
     * 15:10:58.145 [Thread-8] INFO cn.snow.cyclicbarrier.CyclicBarrierDemo - The post process is running.
     * 15:10:58.145 [Thread-8] INFO cn.snow.cyclicbarrier.SubTask - sub task execute await after.1500
     * 15:10:58.145 [Thread-0] INFO cn.snow.cyclicbarrier.SubTask - sub task execute await after.1000
     * 15:10:58.145 [Thread-4] INFO cn.snow.cyclicbarrier.SubTask - sub task execute await after.1400
     * 15:10:58.645 [Thread-2] INFO cn.snow.cyclicbarrier.SubTask - sub task execute complete.2000
     * 15:10:58.645 [Thread-2] INFO cn.snow.cyclicbarrier.SubTask - sub task execute await before.2000
     * 15:10:59.046 [Thread-3] INFO cn.snow.cyclicbarrier.SubTask - sub task execute complete.2400
     * 15:10:59.046 [Thread-3] INFO cn.snow.cyclicbarrier.SubTask - sub task execute await before.2400
     * 15:10:59.145 [Thread-7] INFO cn.snow.cyclicbarrier.SubTask - sub task execute complete.2500
     * 15:10:59.145 [Thread-7] INFO cn.snow.cyclicbarrier.SubTask - sub task execute await before.2500
     * 15:10:59.145 [Thread-7] INFO cn.snow.cyclicbarrier.CyclicBarrierDemo - The post process is running.
     * 15:10:59.145 [Thread-7] INFO cn.snow.cyclicbarrier.SubTask - sub task execute await after.2500
     * 15:10:59.145 [Thread-2] INFO cn.snow.cyclicbarrier.SubTask - sub task execute await after.2000
     * 15:10:59.145 [Thread-3] INFO cn.snow.cyclicbarrier.SubTask - sub task execute await after.2400
     * 15:10:59.445 [Thread-5] INFO cn.snow.cyclicbarrier.SubTask - sub task execute complete.2800
     * 15:10:59.445 [Thread-5] INFO cn.snow.cyclicbarrier.SubTask - sub task execute await before.2800
     * 15:10:59.646 [Thread-1] INFO cn.snow.cyclicbarrier.SubTask - sub task execute complete.3000
     * 15:10:59.646 [Thread-1] INFO cn.snow.cyclicbarrier.SubTask - sub task execute await before.3000
     * 15:11:00.146 [Thread-6] INFO cn.snow.cyclicbarrier.SubTask - sub task execute complete.3500
     * 15:11:00.146 [Thread-6] INFO cn.snow.cyclicbarrier.SubTask - sub task execute await before.3500
     * 15:11:00.146 [Thread-6] INFO cn.snow.cyclicbarrier.CyclicBarrierDemo - The post process is running.
     * 15:11:00.146 [Thread-6] INFO cn.snow.cyclicbarrier.SubTask - sub task execute await after.3500
     * 15:11:00.146 [Thread-5] INFO cn.snow.cyclicbarrier.SubTask - sub task execute await after.2800
     * 15:11:00.146 [Thread-1] INFO cn.snow.cyclicbarrier.SubTask - sub task execute await after.3000
     *
     * Process finished with exit code 0
     */
}
