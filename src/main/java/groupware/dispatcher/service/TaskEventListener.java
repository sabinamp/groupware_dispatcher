package groupware.dispatcher.service;

import groupware.dispatcher.service.model.TaskRequest;
import groupware.dispatcher.view.util.TaskEvent;


public interface TaskEventListener {

    void handleNewTaskEvent( TaskRequest task);
    void handleTaskUpdateEvent(TaskRequest taskRequest);
}
