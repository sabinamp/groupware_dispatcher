package groupware.dispatcher.presentationmodel;

import groupware.dispatcher.service.CourierService;
import groupware.dispatcher.service.OrderService;
import groupware.dispatcher.service.model.Courier;
import groupware.dispatcher.service.model.OrderDescriptiveInfo;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.Map;

public class AllOrdersPM {
    private final StringProperty paneTitle = new SimpleStringProperty("Orders");
    private final IntegerProperty orderCount = new SimpleIntegerProperty();
    private final OrderService orderService;
    private final ObservableList<OrderPM> allOrders = FXCollections.observableArrayList();

    public AllOrdersPM(OrderService orderService){
        this.orderService = orderService;
        Map<String, OrderDescriptiveInfo> ordersMap = orderService.getOrders();
        for(String each : ordersMap.keySet()){
            System.out.println("The dispatcher has received info about the order: "+ each);
            allOrders.add(new OrderPM(ordersMap.get(each)));
        }
        setupValueChangedListeners();

    }

    private void setupValueChangedListeners() {
        allOrders.addListener((ListChangeListener.Change<? extends OrderPM> change) -> {
            // this will write something like
            System.out.println("Updated received"+ change);
            allOrders.stream().forEach(System.out::println);
            //todo - notification popup
        });
    }

}
