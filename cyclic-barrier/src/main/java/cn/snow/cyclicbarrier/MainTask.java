package cn.snow.cyclicbarrier;

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
        super.run();
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.interrupted();
        }
        log.info(execute(delayMillis));
    }

    public String execute(long delayMillis){
        try {
            Thread.sleep(delayMillis);
        } catch (InterruptedException e) {
            log.error("interrupted exception", e);
            Thread.interrupted();
        }
        return "main task execute complete.";
    }
}
