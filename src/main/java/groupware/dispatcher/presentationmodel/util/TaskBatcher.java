package groupware.dispatcher.presentationmodel.util;

import javafx.application.Platform;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

public class TaskBatcher {
    private final Duration delay;
    private Timer timer;

    public TaskBatcher(Duration delay){
        this.delay = delay;
    }

    public void batch(Runnable toDo) {
        System.out.println("entering the batch");
        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(toDo);
                timer.cancel();
            }
        };

        timer.schedule(task, delay.toMillis());
    }
}
