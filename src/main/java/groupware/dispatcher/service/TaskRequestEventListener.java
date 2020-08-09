package groupware.dispatcher.service;

import groupware.dispatcher.presentationmodel.TaskRequestPM;
import groupware.dispatcher.service.model.TaskRequest;


public interface TaskRequestEventListener {

    void handleNewTaskEvent(TaskRequest task);
    void handleTaskUpdateEvent(TaskRequest taskRequest);
}
