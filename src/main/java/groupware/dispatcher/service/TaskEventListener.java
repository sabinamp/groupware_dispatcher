package groupware.dispatcher.service;

import groupware.dispatcher.service.model.TaskRequest;

public interface TaskEventListener {

    void handleNewTaskEvent(TaskRequest task);
    void handleTaskUpdateEvent(TaskRequest taskRequest);
}
