package groupware.dispatcher.presentationmodel;


import groupware.dispatcher.service.TaskRequestPMEventListener;
import groupware.dispatcher.service.TaskRequestServiceImpl;
import groupware.dispatcher.view.util.TaskEvent;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;
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
            change.next();
            boolean wasUpdated = change.wasUpdated();
            boolean wasAdded = change.wasAdded();
            List<TaskRequestPM> listChanges = new ArrayList<>(change.getList());

            //notification popup
          /*  if(wasUpdated) {
                Platform.runLater(() -> {
                    int changeNb = change.getList().size();
                    System.out.println("The number of updates " + changeNb);
                    if (listChanges.size() == changeNb) {
                        while (changeNb > 0) {

                            showAlertWithNoHeaderText(listChanges.get(changeNb - 1));
                            System.out.println("showAlertWithNoHeaderText - task id" + listChanges.get(changeNb - 1).getTaskId());

                            changeNb--;
                        }
                    }
                });
            }*/

        });
    }

    private void showAlertWithNoHeaderText(TaskEvent event, TaskRequestPM taskRequestPM, String update) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Task Request Update");
        alert.setResizable(true);
        String content= "";
        // Header Text: null
        alert.setHeaderText(null);
        if(event.getEventType().equals(TaskEvent.NEW_TASK)){
            content= " Task Request "+"\n Task id: "+taskRequestPM.getTaskId()+"  has been sent to the task assignee."
                     +"\n Task assignee ID: "+ taskRequestPM.getAssigneeId();
        }else if(event.getEventType().equals(TaskEvent.UPDATE)){
            content= "A Task Request Update has been received. Topic: "+update+"\n Task id: "+taskRequestPM.getTaskId()
                    +"\n task assignee : "+ taskRequestPM.getAssigneeId() + "\n Order ID: "+taskRequestPM.getOrderId();
        }

        alert.setContentText(content);
        alert.showAndWait();
    }

    public ObservableList<TaskRequestPM> getSyncAllTasks() {
        return syncAllTasks;
    }




    public void updateAllTaskRequestsPM(TaskRequestPM task){
        boolean updating= false;
       for(int i =0; i < syncAllTasks.size(); i++){
           if(syncAllTasks.get(i).getTaskId().equals(task.getTaskId())){
               updating = true;
               syncAllTasks.set(i, task);
           }
       }
        if(!updating){
            syncAllTasks.add(task);
        }

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
    public void handleNewTaskEvent(TaskEvent event, TaskRequestPM task) {
        if(event.getEventType().equals(TaskEvent.NEW_TASK)){
            this.updateAllTaskRequestsPM(task);
            taskRequestService.updateTaskRequest(task.getTaskId(), TaskRequestPM.toTaskRequest(task));
            Platform.runLater(() -> showAlertWithNoHeaderText(event, task, "New task sent"));
        }

    }

    @Override
    public void handleTaskUpdateEvent(TaskEvent event, TaskRequestPM taskRequest, String update) {
        this.updateAllTaskRequestsPM(taskRequest);
        Platform.runLater(() -> showAlertWithNoHeaderText(event, taskRequest, update));
    }


}
