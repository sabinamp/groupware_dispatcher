package groupware.dispatcher.presentationmodel;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class AllOrdersPM {
    private final StringProperty paneTitle = new SimpleStringProperty("Orders");
    private final IntegerProperty orderCount = new SimpleIntegerProperty();
    /*private final OrderService orderService;*/
    private final ObservableList<OrderPM> allOrders = FXCollections.observableArrayList();

    public AllOrdersPM(){

        /*Map<String, OrderDescriptiveInfo> ordersMap = orderService.getOrders();
        for(String each : ordersMap.keySet()){
            System.out.println("The dispatcher has received info about the order: "+ each);
            allOrders.add(new OrderPM(ordersMap.get(each)));
        }*/
        setupValueChangedListeners();

    }

    private void setupValueChangedListeners() {
        allOrders.addListener((ListChangeListener.Change<? extends OrderPM> change) -> {

            System.out.println("AllOrdersPM Update"+ change.toString());

            //todo - notification popup
        });
    }

    public void updateAllOrdersPM(OrderPM orderPM){
        allOrders.add(orderPM);
    }

}
