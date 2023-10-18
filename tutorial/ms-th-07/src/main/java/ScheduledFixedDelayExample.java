import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

class SchedulerFixedDelayExample {

    public static void main(String[] args) throws InterruptedException {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
        Runnable task = SchedulerFixedDelayExample::task;
        ScheduledFuture<?> future = pool.scheduleWithFixedDelay(task, 0, 2, TimeUnit.SECONDS);
        pool.schedule(() -> {
            future.cancel(true);
            pool.shutdown();
        }, 20, TimeUnit.SECONDS);
    }

    private static void task() {
        final LocalTime now = LocalTime.now();
        final int sleepTime = 2 + now.toSecondOfDay() % 5;
        System.out.format("Start at %s. Sleep for %d seconds. ", now, sleepTime);
        try {
            Thread.sleep(sleepTime * 1000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        System.out.format("Done at %s!%n", LocalTime.now());
    }
}
