package groupware.dispatcher.view.tasks;

import groupware.dispatcher.presentationmodel.TaskRequestPM;
import groupware.dispatcher.service.model.TaskRequest;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Notifications {

    public final static String EVENT_MODEL_UPDATE = "modelUpdate";
    public static ObservableList<TaskRequestPM> updatedList= FXCollections.observableArrayList();

    private final Map<String , List<SubscriberObject>> subscribers = new LinkedHashMap<>();

    private static Notifications instance = new Notifications();

    public void publish(String  event, ObservableList<TaskRequestPM> updatedList) {

        Platform.runLater( () -> {
            List<SubscriberObject> subscriberList = instance.subscribers.get(event);

            if (subscriberList != null) {

                subscriberList.forEach(
                        subscriberObject -> subscriberObject.getCb().accept(updatedList)
                );

                // event ends after last subscriber gets callback
            }
        } );
    }

    public void subscribe(String event, Object subscriber, Consumer<ObservableList<TaskRequestPM>> cb) {

        if( !instance.subscribers.containsKey(event) ) {
            List<SubscriberObject> slist = new ArrayList<>();
            instance.subscribers.put( event, slist );
        }

        List<SubscriberObject> subscriberList = instance.subscribers.get( event );

        subscriberList.add( new SubscriberObject(subscriber, cb) );
    }

    public void unsubscribe(String event, Object subscriber) {

        List<SubscriberObject> subscriberList = instance.subscribers.get( event );

        if (subscriberList != null) {
            subscriberList.remove( subscriber );
        }
    }

    static class SubscriberObject {

        private final Object subscriber;
        private final Consumer<ObservableList<TaskRequestPM>> cb;

        public SubscriberObject(Object subscriber,
                                Consumer<ObservableList<TaskRequestPM>> cb) {
            this.subscriber = subscriber;
            this.cb = cb;
        }

        public Object getSubscriber() {
            return subscriber;
        }

        public Consumer<ObservableList<TaskRequestPM> > getCb() {
            return cb;
        }

        @Override
        public int hashCode() {
            return subscriber.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return subscriber.equals(obj);
        }
    }
}
