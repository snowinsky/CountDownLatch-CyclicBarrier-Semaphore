package cn.snow.cdl;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch一般用于某个线程A等待若干个其他线程执行完任务之后，它才执行；
 * 它会阻塞主线程。
 */
@Slf4j
public class CountDownLatchDemo {

    public static void main(String[] args) {
        CountDownLatch cdl = new CountDownLatch(3);
        new MainTask(cdl, 2000).start();
        new SubTask(cdl, 3000).start();
        new SubTask(cdl, 4000).start();
        new SubTask(cdl, 5000).start();
        new SubTask(cdl, 4500).start();
        new SubTask(cdl, 4501).start();
        new SubTask(cdl, 4502).start();
        new SubTask(cdl, 4503).start();
    }

    /**
     * CountDownLatch是阻塞主线程，放开子线程的。7个线程开始跑，只要3个线程跑到了countDown的位置，主线程就立刻启动。
     *   看子线程，countDown之后，后面的after count down立刻执行了，并未被阻塞。
     *   看主线程，在await方法上阻塞，直到有3个线程跑到countDown。够数了，才开始继续跑。
     * 注意：
     *   如果子线程的数量小于CountDownLatch设置的int时，主线程会一直阻塞，永不停止。为了避免，请为await设置超时时间。
     *   如果子线程的数量大于CountDownLatch设置的int时，主线程也只会执行一次。这就是所谓的CountDownLatch不可复用。
     *
     * 15:01:26.512 [Thread-0] INFO cn.snow.cdl.MainTask - main task start
     * 15:01:29.510 [Thread-1] INFO cn.snow.cdl.SubTask - sub task execute complete.3000
     * 15:01:29.510 [Thread-1] INFO cn.snow.cdl.SubTask - after count down---3000
     * 15:01:30.510 [Thread-2] INFO cn.snow.cdl.SubTask - sub task execute complete.4000
     * 15:01:30.510 [Thread-2] INFO cn.snow.cdl.SubTask - after count down---4000
     * 15:01:31.012 [Thread-4] INFO cn.snow.cdl.SubTask - sub task execute complete.4500
     * 15:01:31.012 [Thread-4] INFO cn.snow.cdl.SubTask - after count down---4500
     * 15:01:31.012 [Thread-0] INFO cn.snow.cdl.MainTask - main task end
     * 15:01:31.013 [Thread-5] INFO cn.snow.cdl.SubTask - sub task execute complete.4501
     * 15:01:31.013 [Thread-5] INFO cn.snow.cdl.SubTask - after count down---4501
     * 15:01:31.014 [Thread-6] INFO cn.snow.cdl.SubTask - sub task execute complete.4502
     * 15:01:31.014 [Thread-6] INFO cn.snow.cdl.SubTask - after count down---4502
     * 15:01:31.015 [Thread-7] INFO cn.snow.cdl.SubTask - sub task execute complete.4503
     * 15:01:31.015 [Thread-7] INFO cn.snow.cdl.SubTask - after count down---4503
     * 15:01:31.512 [Thread-3] INFO cn.snow.cdl.SubTask - sub task execute complete.5000
     * 15:01:31.512 [Thread-3] INFO cn.snow.cdl.SubTask - after count down---5000
     */

}
