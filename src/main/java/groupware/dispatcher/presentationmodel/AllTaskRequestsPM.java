package groupware.dispatcher.presentationmodel;


import groupware.dispatcher.service.model.TaskType;
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

    public AllTaskRequestsPM(AllOrdersPM allOrdersPM, AllCouriersPM allCouriersPM){

        this.allCouriersPM= allCouriersPM;
        this.allOrdersPM = allOrdersPM;
        setupValueChangedListeners();
        setUpFirstTask();
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

    public void setUpFirstTask(){
        TaskRequestPM task1= new TaskRequestPM();
        task1.setTaskId("TaskRequest1");
        task1.setAssigneeId("C101");
        task1.setOrderId("OR1111");
        OrderPM order1125 = allOrdersPM.getSyncAllOrdersMap().get("OR1111");
        if(order1125 != null){

                task1.setDeliveryType(order1125.getOrderType());
                task1.setTaskType( TaskType.PARCEL_COLLECTION);
                updateAllTaskRequestsPM(task1);

        }else{
            System.out.println("setUpFirstTask - null order order1111");
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
}
