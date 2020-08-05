package groupware.dispatcher.view.util;

import javafx.event.Event;
import javafx.event.EventType;

public class UserEvent extends Event {

    public static final EventType<UserEvent> ANY = new EventType<>(Event.ANY, "ANY");

    public static final EventType<UserEvent> NEW_TASK = new EventType<>(ANY, "NEW_TASK");

    public static final EventType<UserEvent> TASK_UPDATE = new EventType<>(ANY, "TASK_UPDATE");

    public UserEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    // any other fields of importance, e.g. data, timestamp
}
