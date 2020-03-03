package cn.snow.semaphore;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

@Slf4j
public class SubTask extends Thread{

    private final Semaphore semaphore;
    private final long delayMillis;

    public SubTask(Semaphore semaphore, long delayMillis){
        this.semaphore = semaphore;
        this.delayMillis = delayMillis;
    }

    @Override
    public void run() {
        try {
            log.info("acquire before." + delayMillis);
            semaphore.acquire();
            log.info("acquire after." + delayMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.run();
        log.info(execute(delayMillis));
        log.info("release before." + delayMillis);
        semaphore.release();
        log.info("release before." + delayMillis);
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
