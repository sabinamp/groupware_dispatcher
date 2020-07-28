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
    private OrderServiceImpl orderService;
    private CourierServiceImpl courierService;

    private final ObservableList<TaskRequestPM> syncAllTasks = FXCollections.synchronizedObservableList(allTasks);

    
    public AllTaskRequestsPM(OrderServiceImpl orders, CourierServiceImpl couriers){
        this.courierService= couriers;
        this.orderService = orders;
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

    public void setUpFirstTask(){
        TaskRequestPM task1= new TaskRequestPM();
        task1.setTaskId("request1");
        task1.setAssigneeId("C105");
        task1.setOrderId("OR1125");
        OrderDescriptiveInfo order1125 = orderService.getOrder("OR1125");
        if(order1125 != null){
            OrderInfo info=order1125.getOrderInfo();
            if(info != null){
                task1.setDeliveryType(info.getDeliveryType());
                task1.setTaskType( TaskType.PARCEL_COLLECTION);
                updateAllTaskRequestsPM(task1);
            }
            else{
                System.out.println("setUpFirstTask -  order1125 has null info");
            }

        }else{
            System.out.println("setUpFirstTask - null order order1125");
        }

    }

}
