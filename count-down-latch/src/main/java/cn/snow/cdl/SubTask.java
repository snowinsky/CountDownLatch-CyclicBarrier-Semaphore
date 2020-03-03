package cn.snow.cdl;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class SubTask extends Thread{

    private final CountDownLatch latch;
    private final long delayMillis;

    public SubTask(CountDownLatch latch, long delayMillis){
        this.latch = latch;
        this.delayMillis = delayMillis;
    }

    @Override
    public void run() {
        super.run();
        log.info(execute(delayMillis));
        latch.countDown();
    }

    public String execute(long delayMillis){
        try {
            Thread.sleep(delayMillis);
        } catch (InterruptedException e) {
            log.error("interrupted exception", e);
            Thread.interrupted();
        }
        return "sub task execute complete." + delayMillis;
    }

}
