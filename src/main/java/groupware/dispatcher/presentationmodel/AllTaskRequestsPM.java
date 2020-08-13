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
import javafx.collections.ObservableMap;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;



public class AllTaskRequestsPM implements TaskRequestPMEventListener {
    private final ObservableList<TaskRequestPM> allTasks = FXCollections.observableArrayList();

    private AllCouriersPM allCouriersPM;
    private AllOrdersPM allOrdersPM;
    private TaskRequestServiceImpl taskRequestService;

    private final ObservableList<TaskRequestPM> syncAllTasks = FXCollections.synchronizedObservableList(allTasks);
    private final ObservableMap<String, TaskRequestPM> allTasksMap = FXCollections.observableHashMap();

    private final ObservableMap<String, TaskRequestPM> syncAllTasksMap = FXCollections.synchronizedObservableMap(allTasksMap);
    private ObjectProperty<ObservableList<TaskRequestPM>> allTaskEntries = new SimpleObjectProperty<>();
    private ObjectProperty<TaskRequestPM> currentTaskRequest = new SimpleObjectProperty<>();

    private TaskRequestPMEventListener listener;

    public AllTaskRequestsPM(AllOrdersPM allOrdersPM, AllCouriersPM allCouriersPM, TaskRequestServiceImpl taskRequestService){
        this.allCouriersPM= allCouriersPM;
        this.allOrdersPM = allOrdersPM;
        this.taskRequestService = taskRequestService;
        setupValueChangedListeners();
        setAllTaskEntries(syncAllTasks);
    }

    private void setupValueChangedListeners() {
        syncAllTasks.addListener((ListChangeListener.Change<? extends TaskRequestPM> change) -> {
            System.out.println("AllTaskRequestsPM Update"+ change);
           /* change.next();
            boolean wasUpdated = change.wasUpdated();
            boolean wasAdded = change.wasAdded();
            List<TaskRequestPM> listChanges = new ArrayList<>(change.getList());  */
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
                    +"\n task assignee : "+ taskRequestPM.getAssigneeId() + "\n Order ID: "+taskRequestPM.getOrderId();
        }
        alert.setHeight(200);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public ObservableList<TaskRequestPM> getSyncAllTasks() {
        return syncAllTasks;
    }



    public void updateAllTaskRequestsPM(TaskRequestPM task){
        String id = task.getTaskId();
        TaskRequestPM existingTask = syncAllTasksMap.get(id);
        if(existingTask != null){
            syncAllTasks.remove(existingTask);
        }
        syncAllTasks.add(task);
        syncAllTasksMap.put(id, task);

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
            taskRequestService.updateTaskRequest(task.getTaskId(), TaskRequestPM.toTaskRequest(task), null);
            Platform.runLater(() -> showAlertWithNoHeaderText(event, task, "New task sent"));
        }
    }

    @Override
    public void handleTaskUpdateEvent(TaskEvent event, TaskRequestPM taskRequest, String update) {
        if(event.getEventType().equals(TaskEvent.UPDATE)){
            this.updateAllTaskRequestsPM(taskRequest);
            Platform.runLater(() -> showAlertWithNoHeaderText(event, taskRequest, update));
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
}
