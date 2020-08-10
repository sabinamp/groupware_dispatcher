package groupware.dispatcher.presentationmodel;


import groupware.dispatcher.service.TaskRequestPMEventListener;
import groupware.dispatcher.service.TaskRequestServiceImpl;
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

    private void showAlertWithNoHeaderText(TaskRequestPM taskRequestPM) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Task Request Update");

        // Header Text: null
        alert.setHeaderText(null);
        alert.setContentText("Task request PM Update. Task id: "+taskRequestPM.getTaskId()
        +"\n task assignee : "+ taskRequestPM.getAssigneeId() + " \n task for order id: "+taskRequestPM.getOrderId());
        alert.showAndWait();
    }

    public ObservableList<TaskRequestPM> getSyncAllTasks() {
        return syncAllTasks;
    }


    public void updateAllTaskRequestsPMAndService(TaskRequestPM task){
        updateAllTaskRequestsPM(task);
        taskRequestService.updateTaskRequest(task.getTaskId(), TaskRequestPM.toTaskRequest(task));
    }

    public void updateAllTaskRequestsPM(TaskRequestPM task){
        boolean updating= false;
       for(int i =0; i < syncAllTasks.size(); i++){
           if(syncAllTasks.get(i).getTaskId().equals(task.getTaskId())){
               syncAllTasks.set(i, task);
               updating = true;
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
    public void handleNewTaskEvent(TaskRequestPM task) {
        this.updateAllTaskRequestsPMAndService(task);
    }

    @Override
    public void handleTaskUpdateEvent(TaskRequestPM taskRequest) {
        this.updateAllTaskRequestsPM(taskRequest);
        Platform.runLater(() -> showAlertWithNoHeaderText(taskRequest));
    }


}
