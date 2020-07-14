package groupware.dispatcher.view;

import groupware.dispatcher.service.OrderService;
import groupware.dispatcher.service.model.OrderDescriptiveInfo;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.util.List;
import java.util.Map;

public class OrderListView extends ListView<String> implements ViewMixin {
    private Map<String, OrderDescriptiveInfo> orders;
    private OrderService orderService;

    public OrderListView(){
        init();
    }

    @Override
    public void init() {
        orderService = new OrderService();
        orders= orderService.getOrders();

    }

    @Override
    public void initializeSelf() {
        //getStyleClass().add("order-pane");
    }

    @Override
    public void initializeParts() {
        ListView<String> list = new ListView<String>();
        for(String each : orders.keySet()){
            System.out.println("The dispatcher part has received info about the order: "+each);
        }
        ObservableList<String> items = FXCollections.observableArrayList (
                "Single", "Double", "Suite", "Family App");
        list.setItems(items);
    }

    @Override
    public void layoutParts() {

    }

    @Override
    public void setupEventHandlers() {

    }

    @Override
    public void setupValueChangedListeners() {

    }

    @Override
    public void setupBindings() {

    }

    @Override
    public void addStylesheetFiles(String... stylesheetFile) {

    }


}
