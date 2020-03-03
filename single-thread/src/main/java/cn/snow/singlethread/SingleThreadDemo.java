package cn.snow.singlethread;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
public class SingleThreadDemo {

    public static void main(String[] args) {
        LocalDateTime start = LocalDateTime.now();
        for (int i = 0; i < 10; i++) {
            new DemoTask(2000).run();
        }
        LocalDateTime end = LocalDateTime.now();
        log.info("====" + Duration.between(start, end).toMillis());
    }

    /**
     * 单线程执行，共耗时。一个任务睡2000ms，是个任务就是20000ms，13则损耗和组合的时间。
     * 17:36:46.860 [main] INFO cn.snow.singlethread.SingleThreadDemo - ====20013
     */

}
