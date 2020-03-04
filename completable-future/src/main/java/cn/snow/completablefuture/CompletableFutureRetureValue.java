package cn.snow.completablefuture;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

@Slf4j
public class CompletableFutureRetureValue {

    public static void main(String[] args) {
        CompletableFuture<ValueBean> returnValue = createFirstCompletableFuture();
        CompletableFuture<ValueBean> returnValueWhenComplete = returnValue.whenCompleteAsync((valueBean, throwable) -> {
            log.info(String.valueOf(valueBean));
            log.info(String.valueOf(throwable));
            valueBean.setBod(LocalDate.MAX);
        });
        log.info(returnValueWhenComplete.join().toString());

        CompletableFuture<LocalDate> returnValueHandle = returnValue.handleAsync(new BiFunction<ValueBean, Throwable, LocalDate>() {
            @Override
            public LocalDate apply(ValueBean valueBean, Throwable throwable) {
                log.info(String.valueOf(valueBean));
                log.info(String.valueOf(throwable));
                return valueBean.getBod() == null ? LocalDate.MIN : valueBean.getBod();
            }
        });
        log.info(String.valueOf(returnValueHandle.join()));
    }

    private static CompletableFuture<ValueBean> createFirstCompletableFuture() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.interrupted();
                log.error("InterruptedException", e);
            }
            log.info("first task complete");
            return new ValueBean(1, "name1");
        });
    }
}
