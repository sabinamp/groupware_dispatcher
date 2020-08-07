package groupware.dispatcher.service;

import groupware.dispatcher.presentationmodel.TaskRequestPM;
import groupware.dispatcher.service.model.TaskRequest;
import groupware.dispatcher.view.util.TaskEvent;


public interface TaskEventListener {

    void handleNewTaskEvent( TaskRequestPM task);
    void handleTaskUpdateEvent(TaskRequestPM taskRequest);
}
