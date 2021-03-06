package cn.snow.cdl;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class MainTask extends Thread{

    private final CountDownLatch latch;
    private final long delayMillis;

    public MainTask(CountDownLatch latch, long delayMillis){
        this.latch = latch;
        this.delayMillis = delayMillis;
    }

    @Override
    public void run() {
        log.info("main task start");
        super.run();
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.interrupted();
        }
        log.info("main task end");
    }
}
