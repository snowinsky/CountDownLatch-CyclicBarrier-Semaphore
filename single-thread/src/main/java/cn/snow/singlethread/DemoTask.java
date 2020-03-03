package cn.snow.singlethread;

public class DemoTask extends Thread {

    private final long delayMs;

    public DemoTask(long delayMs) {
        this.delayMs = delayMs;
    }

    @Override
    public void run() {
        super.run();
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
