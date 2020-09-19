package groupware.dispatcher.presentationmodel;


import groupware.dispatcher.service.TaskRequestPMEventListener;
import groupware.dispatcher.service.TaskRequestServiceImpl;
import groupware.dispatcher.view.tasks.Notifications;
import groupware.dispatcher.view.util.TaskEvent;
import javafx.application.Platform;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;



public class AllTaskRequestsPM implements TaskRequestPMEventListener {
    private final ObservableList<TaskRequestPM> allTasks = FXCollections.observableArrayList(/*new Callback<>() {
        @Override
        public Observable[] call(TaskRequestPM param) {
            return new Observable[]{param.taskIdProperty(),param.assigneeIdProperty(),
            param.orderIdProperty(), param.deliveryTypeProperty(), param.requestReplyProperty(),
            param.doneProperty()};
        }
    }*/);

    private AllCouriersPM allCouriersPM;
    private AllOrdersPM allOrdersPM;
    private TaskRequestServiceImpl taskRequestService;
    private final Notifications notifications =
            new Notifications();
    private final ObservableList<TaskRequestPM> syncAllTasks = FXCollections.synchronizedObservableList(allTasks);

    private final ObservableMap<String, TaskRequestPM> allTasksMap = FXCollections.observableHashMap();
    private final ObservableMap<String, TaskRequestPM> syncAllTasksMap = FXCollections.synchronizedObservableMap(allTasksMap);
    private ObjectProperty<ObservableList<TaskRequestPM>> allTaskEntries = new SimpleObjectProperty<>();
    private ObjectProperty<TaskRequestPM> currentTaskRequest = new SimpleObjectProperty<>();


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

    private void showAlertWithNoHeaderText(TaskEvent event, TaskRequestPM taskRequestPM, String update) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Task Request Update");
        alert.setResizable(true);
        alert.initStyle(StageStyle.DECORATED);

        String content= "";
        // Header Text: null
        alert.setHeaderText(null);
        if(event.getEventType().equals(TaskEvent.NEW_TASK)){
            content= " Task Request "+"\n Task id: "+taskRequestPM.getTaskId()+"  has been sent to the task assignee."
                     +"\n Task assignee ID: "+ taskRequestPM.getAssigneeId();
        }else if(event.getEventType().equals(TaskEvent.UPDATE)){
            content= "A Task Request Update has been received. Topic: "+update+"\n Task id: "+taskRequestPM.getTaskId()
                    +"\n Task assignee : "+ taskRequestPM.getAssigneeId() + "\n Order ID: "+taskRequestPM.getOrderId();
        }else if(event.getEventType().equals(TaskEvent.TASK_TIMEOUT)){
            content= "Task Request Timeout. Topic: "+update+"\n Task id: "+taskRequestPM.getTaskId()
                    +"\n Task assignee : "+ taskRequestPM.getAssigneeId() + "\n Order ID: "+taskRequestPM.getOrderId();
        }
        alert.setHeight(220);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public void updateAllTaskRequestsPM(TaskRequestPM aTaskPM){
        String id = aTaskPM.getTaskId();
        TaskRequestPM existingTask = syncAllTasksMap.get(id);
        synchronized (syncAllTasks){
            if(existingTask != null){
                syncAllTasks.remove(existingTask);
            }
            syncAllTasks.add(aTaskPM);
        }
        syncAllTasksMap.put(id, aTaskPM);
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
            taskRequestService.updateTaskRequest(task.getTaskId(), TaskRequestPM.toTaskRequest(task), null);

            Platform.runLater(() -> {
                this.updateAllTaskRequestsPM(task);
                showAlertWithNoHeaderText(event, task, "New delivery task sent");
                ObservableList<TaskRequestPM> tasks;
                synchronized (this.syncAllTasks){
                     tasks= FXCollections.observableList(this.getSyncAllTasks());
                }
                notifications.publish(Notifications.EVENT_MODEL_UPDATE, tasks);
            });

        }
    }

    @Override
    public void handleTaskUpdateEvent(TaskEvent event, TaskRequestPM taskRequest, String update) {
        if(event.getEventType().equals(TaskEvent.UPDATE)){

                Platform.runLater(() -> {
                this.updateAllTaskRequestsPM(taskRequest);
                showAlertWithNoHeaderText(event, taskRequest, update);
                    ObservableList<TaskRequestPM> tasks;
                    synchronized (this.syncAllTasks){
                        tasks= FXCollections.observableList(this.getSyncAllTasks());
                    }
                    notifications.publish(Notifications.EVENT_MODEL_UPDATE, tasks);
            });

        }
       else if( event.getEventType().equals(TaskEvent.TASK_TIMEOUT)){

            Platform.runLater(() -> {
                this.updateAllTaskRequestsPM(taskRequest);
                showAlertWithNoHeaderText(event, taskRequest, "Task Request Timeout");
                ObservableList<TaskRequestPM> tasks;
                synchronized (this.syncAllTasks){
                    tasks= FXCollections.observableList(this.getSyncAllTasks());
                }
                notifications.publish(Notifications.EVENT_MODEL_UPDATE, tasks);
            });

        }

    }

    public ObservableList<TaskRequestPM> getAllTaskEntries() {
        return allTaskEntries.get();
    }

    public ObjectProperty<ObservableList<TaskRequestPM>> allTaskEntriesProperty() {
        return allTaskEntries;
    }

    public void setAllTaskEntries(ObservableList<TaskRequestPM> allTaskEntries) {
        this.allTaskEntries.set(allTaskEntries);
    }

    public ObservableMap<String, TaskRequestPM> getSyncAllTasksMap() {
            return syncAllTasksMap;
    }


    public ObservableList<TaskRequestPM> getSyncAllTasks() {
           return this.syncAllTasks;

    }
}
