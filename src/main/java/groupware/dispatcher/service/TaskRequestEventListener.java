package groupware.dispatcher.service;

import groupware.dispatcher.presentationmodel.TaskRequestPM;
import groupware.dispatcher.service.model.TaskRequest;
import groupware.dispatcher.view.util.TaskEvent;


public interface TaskRequestEventListener {

    void handleNewTaskEvent(TaskEvent event, TaskRequestPM task);
    void handleTaskUpdateEvent(TaskEvent event,TaskRequestPM taskRequest, String update);
}
