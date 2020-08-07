package groupware.dispatcher.presentationmodel;


import groupware.dispatcher.service.TaskEventListener;
import groupware.dispatcher.service.TaskRequestServiceImpl;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;


public class AllTaskRequestsPM implements TaskEventListener{
    private final ObservableList<TaskRequestPM> allTasks = FXCollections.observableArrayList();

    private AllCouriersPM allCouriersPM;
    private AllOrdersPM allOrdersPM;
    private TaskRequestServiceImpl taskRequestService;

    private final ObservableList<TaskRequestPM> syncAllTasks = FXCollections.synchronizedObservableList(allTasks);

    private ObjectProperty<TaskRequestPM> currentTaskRequest = new SimpleObjectProperty<>();

    private TaskEventListener listener;

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
    public void updateItemInAllTaskRequestsPM(TaskRequestPM task){
        syncAllTasks.forEach(a->{
            if(a.getTaskId().equals(task.getTaskId())){
                removeCurrentItem(a);
            }
        });
    }

    private void removeCurrentItem(TaskRequestPM a) {
        syncAllTasks.remove(a);
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

    public TaskEventListener getListener() {
        return listener;
    }

    public void setListener(TaskEventListener listener) {
        this.listener = listener;
    }

    @Override
    public void handleNewTaskEvent(TaskRequestPM task) {
        updateAllTaskRequestsPM(task);
    }

    @Override
    public void handleTaskUpdateEvent(TaskRequestPM taskRequest) {
        updateItemInAllTaskRequestsPM(taskRequest);
    }

    public void updateTaskRequestService(TaskRequestPM task) {
        this.taskRequestService.updateTaskRequest(task.getTaskId(), TaskRequestPM.toTaskRequest(task));
    }
}
