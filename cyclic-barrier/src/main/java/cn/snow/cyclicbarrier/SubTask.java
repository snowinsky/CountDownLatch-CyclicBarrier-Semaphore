package cn.snow.cyclicbarrier;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class SubTask extends Thread{

    private final CyclicBarrier barrier;
    private final long delayMillis;

    public SubTask(CyclicBarrier barrier, long delayMillis){
        this.barrier = barrier;
        this.delayMillis = delayMillis;
    }

    @Override
    public void run() {
        log.info("sub task execute start." + delayMillis);
        super.run();
        log.info(execute(delayMillis));
        try {
            log.info("sub task execute await before." + delayMillis);
            barrier.await();
        } catch (InterruptedException e) {
        } catch (BrokenBarrierException e) {
        }
        log.info("sub task execute await after." + delayMillis);
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
