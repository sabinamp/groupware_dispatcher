package groupware.dispatcher.service.util;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Timer service implementation.
 */
public class TimerService {

    // Scheduled executor service used to create the timers.
    private ScheduledExecutorService scheduledExecutorService;

    // Map of scheduled fixed rate timers.
    private final HashMap<Integer, ScheduledFuture<?>> timerIdMap = new HashMap<>();

    /**
     * Initializes the timer service.
     * Must be called before any timer task can be scheduled.
     */
    public void init() {
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
    }

    /**
     * Disposes the timer service.
     * Must be called before the application is shut down.
     */
    public void dispose() {
        // Cancels all fixed rate timers.
        for (Integer timerId : timerIdMap.keySet()) {
            cancel(timerId, true);
        }

        // Shuts down the executor service.
        scheduledExecutorService.shutdown();

        boolean isInterrupted = false;

        try {
            try {
                scheduledExecutorService.awaitTermination(2, TimeUnit.SECONDS);
            }
            catch (InterruptedException e) {
                scheduledExecutorService.shutdownNow();
            }
        }
        finally {
            if (isInterrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Schedules the given timer task to run repeatedly.
     *
     * @param task                The task to run.
     * @param delayInMilliseconds Time interval, in milliseconds, at which the task
     *                            is repeated.
     * @return The ID of the scheduled timer. Can be used to cancel the timer.
     */
    public Integer scheduleAtFixedRate(final Runnable task, long delayInMilliseconds) {
        // Schedules the task.
        ScheduledFuture<?> future = scheduledExecutorService.scheduleAtFixedRate(
                task, delayInMilliseconds, delayInMilliseconds, TimeUnit.MILLISECONDS);

        Integer timerId = timerIdMap.size() + 1;
        timerIdMap.put(timerId, future);

        return timerId;
    }

    /**
     * Cancels the timer with the given ID.
     *
     * @param timerId               The ID of the timer to cancel.
     * @param mayInterruptIfRunning May interrupt if running boolean
     * @return True if the cancellation was successful, false otherwise.
     */
    public boolean cancel(Integer timerId, boolean mayInterruptIfRunning) {
        ScheduledFuture<?> future = timerIdMap.get(timerId);
        return future != null ? future.cancel(mayInterruptIfRunning) : false;
    }

}
