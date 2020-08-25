package groupware.dispatcher.view.util;

import javafx.event.Event;
import javafx.event.EventType;

public class TaskEvent extends Event {

    public static final EventType<TaskEvent> ANY = new EventType<>(Event.ANY, "ANY");

    public static final EventType<TaskEvent> NEW_TASK = new EventType<>(ANY, "NEW_TASK");

    public static final EventType<TaskEvent> TASK_TIMEOUT = new EventType<>(ANY, "TASK_TIMEOUT");

    public static final EventType<TaskEvent> UPDATE = new EventType<>(ANY, "UPDATE");

    public TaskEvent(EventType<? extends Event> eventType) {

        super(eventType);
    }

    // any other fields of importance, e.g. data, timestamp
}
