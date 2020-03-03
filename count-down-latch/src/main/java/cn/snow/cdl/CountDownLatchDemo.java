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
    }

}
