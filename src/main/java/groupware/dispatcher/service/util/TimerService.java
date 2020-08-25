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
    private final HashMap<String, ScheduledFuture<?>> timerIdMap = new HashMap<>();

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
        for (String timerId : timerIdMap.keySet()) {
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
     */
    public void scheduleAtFixedRate(final Runnable task, long delayInMilliseconds, String timerId) {
        // Schedules the task.
        ScheduledFuture<?> future = scheduledExecutorService.scheduleAtFixedRate(
                task, delayInMilliseconds, delayInMilliseconds, TimeUnit.MILLISECONDS);

        //
        timerIdMap.put(timerId, future);

    }

    /**
     * Cancels the timer with the given ID.
     *
     * @param timerId               The ID of the timer to cancel.
     * @param mayInterruptIfRunning May interrupt if running boolean
     * @return True if the cancellation was successful, false otherwise.
     */
    public boolean cancel(String timerId, boolean mayInterruptIfRunning) {
        ScheduledFuture<?> future = timerIdMap.get(timerId);
        return future != null ? future.cancel(mayInterruptIfRunning) : false;
    }


    /**
     * Schedules the given timer task to run a single time.
     *
     * @param task                The task to run.
     * @param delayInMilliseconds Delay, in milliseconds, after which to run the
     *                            task.
     */
    public void schedule(final Runnable task, long delayInMilliseconds, String timerId) {
        ScheduledFuture<?> future = scheduledExecutorService.schedule(task, delayInMilliseconds, TimeUnit.MILLISECONDS);

        timerIdMap.put(timerId, future);

    }

}
