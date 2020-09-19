package groupware.dispatcher.service;

import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import groupware.dispatcher.service.model.TaskRequest;
import groupware.dispatcher.view.util.TaskEvent;


public interface BrokerTaskRequestEventListener {

    void handleNewTaskEvent(TaskEvent event, TaskRequest task);
    void handleTimeoutTaskEvent(TaskEvent event, TaskRequest task);
    void handleTaskUpdateEvent(TaskEvent event, Mqtt3Publish publish, String taskId);


}
