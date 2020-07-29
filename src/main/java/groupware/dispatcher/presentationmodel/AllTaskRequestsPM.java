package groupware.dispatcher.presentationmodel;


import groupware.dispatcher.service.CourierServiceImpl;
import groupware.dispatcher.service.OrderServiceImpl;
import groupware.dispatcher.service.model.OrderDescriptiveInfo;
import groupware.dispatcher.service.model.OrderInfo;
import groupware.dispatcher.service.model.TaskType;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;


public class AllTaskRequestsPM {
    private final ObservableList<TaskRequestPM> allTasks = FXCollections.observableArrayList();
   /* private OrderServiceImpl orderService;
    private CourierServiceImpl courierService;*/
    private AllCouriersPM allCouriersPM;
    private AllOrdersPM allOrdersPM;
    private final ObservableList<TaskRequestPM> syncAllTasks = FXCollections.synchronizedObservableList(allTasks);

    
    public AllTaskRequestsPM(AllOrdersPM allOrdersPM, AllCouriersPM allCouriersPM){
     /*   this.courierService= couriers;
        this.orderService = orders;*/
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
        OrderPM order1125 = allOrdersPM.getSyncAllOrdersMap().get("OR1125");
        if(order1125 != null){

                task1.setDeliveryType(order1125.getOrderType());
                task1.setTaskType( TaskType.PARCEL_COLLECTION);
                updateAllTaskRequestsPM(task1);

        }else{
            System.out.println("setUpFirstTask - null order order1111");
        }

    }

}
