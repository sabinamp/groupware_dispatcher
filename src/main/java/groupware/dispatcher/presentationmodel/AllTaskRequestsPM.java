package groupware.dispatcher.presentationmodel;


import groupware.dispatcher.service.TaskEventListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;


public class AllTaskRequestsPM {
    private final ObservableList<TaskRequestPM> allTasks = FXCollections.observableArrayList();

    private AllCouriersPM allCouriersPM;
    private AllOrdersPM allOrdersPM;

    private final ObservableList<TaskRequestPM> syncAllTasks = FXCollections.synchronizedObservableList(allTasks);

    private ObjectProperty<TaskRequestPM> currentTaskRequest = new SimpleObjectProperty<>();

    private TaskEventListener listener;

    public AllTaskRequestsPM(AllOrdersPM allOrdersPM, AllCouriersPM allCouriersPM){
        this.allCouriersPM= allCouriersPM;
        this.allOrdersPM = allOrdersPM;
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
        if(this.listener != null){
            listener.handleNewTaskEvent(TaskRequestPM.toTaskRequest(task));
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

    public TaskEventListener getListener() {
        return listener;
    }

    public void setListener(TaskEventListener listener) {
        this.listener = listener;
    }

}
