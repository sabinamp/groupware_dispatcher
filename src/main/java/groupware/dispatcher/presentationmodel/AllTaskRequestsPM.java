package groupware.dispatcher.presentationmodel;


import groupware.dispatcher.service.TaskRequestPMEventListener;
import groupware.dispatcher.service.TaskRequestServiceImpl;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.stream.Collectors;


public class AllTaskRequestsPM implements TaskRequestPMEventListener {
    private final ObservableList<TaskRequestPM> allTasks = FXCollections.observableArrayList();

    private AllCouriersPM allCouriersPM;
    private AllOrdersPM allOrdersPM;
    private TaskRequestServiceImpl taskRequestService;

    private final ObservableList<TaskRequestPM> syncAllTasks = FXCollections.synchronizedObservableList(allTasks);

    private ObjectProperty<TaskRequestPM> currentTaskRequest = new SimpleObjectProperty<>();

    private TaskRequestPMEventListener listener;

    public AllTaskRequestsPM(AllOrdersPM allOrdersPM, AllCouriersPM allCouriersPM, TaskRequestServiceImpl taskRequestService){
        this.allCouriersPM= allCouriersPM;
        this.allOrdersPM = allOrdersPM;
        this.taskRequestService = taskRequestService;
        setupValueChangedListeners();
    }

    private void setupValueChangedListeners() {
        syncAllTasks.addListener((ListChangeListener.Change<? extends TaskRequestPM> change) -> {
            System.out.println("AllTaskRequestsPM Update"+ change);
        });
    }

    public ObservableList<TaskRequestPM> getSyncAllTasks() {
        return syncAllTasks;
    }

    public void updateAllTaskRequestsPM(TaskRequestPM task){
        syncAllTasks.add(task);
    }
    public void updateAllTaskRequestsPMAndService(TaskRequestPM task){
        syncAllTasks.add(task);
        taskRequestService.updateTaskRequest(task.getTaskId(), TaskRequestPM.toTaskRequest(task));
    }
    public void updateItemInAllTaskRequestsPM(TaskRequestPM task){
       for(int i =0; i < syncAllTasks.size(); i++){
           if(syncAllTasks.get(i).getTaskId().equals(task.getTaskId())){
               syncAllTasks.set(i, task);
           }
       }

    }

    public void updateItemInAllTaskRequestsPMAndTaskService(TaskRequestPM task){
        updateItemInAllTaskRequestsPM(task);
        taskRequestService.updateTaskRequest(task.getTaskId(), TaskRequestPM.toTaskRequest(task));

    }



    public ObjectProperty<TaskRequestPM> currentTaskRequestProperty() {
        return currentTaskRequest;
    }

    public void setCurrentTaskRequest(TaskRequestPM currentTaskRequest) {
        this.currentTaskRequest.set(currentTaskRequest);
    }
    public TaskRequestPM getCurrentTaskRequest() {
        return currentTaskRequest.get();
    }



    @Override
    public void handleNewTaskEvent(TaskRequestPM task) {
        updateAllTaskRequestsPM(task);
    }

    @Override
    public void handleTaskUpdateEvent(TaskRequestPM taskRequest) {
        updateItemInAllTaskRequestsPM(taskRequest);
    }


}
