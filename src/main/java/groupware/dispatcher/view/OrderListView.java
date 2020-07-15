package groupware.dispatcher.view;

import groupware.dispatcher.presentationmodel.util.OrderPM;
import groupware.dispatcher.service.OrderService;
import groupware.dispatcher.service.model.OrderDescriptiveInfo;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import java.util.Map;

public class OrderListView extends ListView<String> implements ViewMixin {
    private Map<String, OrderDescriptiveInfo> orders;
    private OrderService orderService;
    public static final ObservableList<OrderPM> data =
            FXCollections.observableArrayList();

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
        ListView<OrderPM> list = new ListView<>();
        for(String each : orders.keySet()){
            System.out.println("The dispatcher part has received info about the order: "+ each);
        }

        list.setItems(data);
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
