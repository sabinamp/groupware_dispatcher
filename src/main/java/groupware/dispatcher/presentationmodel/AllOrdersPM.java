package groupware.dispatcher.presentationmodel;


import groupware.dispatcher.service.OrderEventListener;
import groupware.dispatcher.service.model.OrderDescriptiveInfo;
import groupware.dispatcher.service.model.OrderStatus;
import groupware.dispatcher.service.util.ModelObjManager;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AllOrdersPM implements OrderEventListener {
    private final StringProperty paneTitle = new SimpleStringProperty("Orders");
    private final IntegerProperty orderCount = new SimpleIntegerProperty();

    private final ObservableList<OrderPM> allOrders = FXCollections.observableArrayList();
    private final ObservableMap<String, OrderPM> allOrdersMap = FXCollections.observableHashMap();


    private final ObservableMap<String, OrderPM> syncAllOrdersMap = FXCollections.synchronizedObservableMap(allOrdersMap);
    private final ObservableList<OrderPM> syncAllOrders = FXCollections.synchronizedObservableList(allOrders);


    private final ObjectProperty<ObservableList<OrderPM>> allOrderEntries = new SimpleObjectProperty<>();
    private static final String datePattern = "M/d/YY HH:mm";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(datePattern);

    public AllOrdersPM(){
        setupValueChangedListeners();
        setAllOrderEntries(syncAllOrders);

    }

    private void setupValueChangedListeners() {
        syncAllOrdersMap.addListener((MapChangeListener<String, OrderPM>) change -> System.out.println("syncAllOrdersMap Update"+ change));

        /*syncAllOrders.addListener((ListChangeListener.Change<? extends OrderPM> change) -> {

            System.out.println("AllOrdersPM Update"+ change);
            change.next();

            boolean wasUpdated = change.wasUpdated();
            boolean wasAdded = change.wasAdded();
            List<OrderPM> listChanges = new ArrayList<>(change.getList());

                //notification popup
                Platform.runLater(()->{

                        int changeNb = change.getList().size();
                        System.out.println("showAlertWithDefaultHeaderText called. The number of updates or new orders " + changeNb);
                        if(listChanges.size() == changeNb){
                            if(changeNb > 0){
                                showAlertWithDefaultHeaderText(wasUpdated, listChanges.get(changeNb-1));
                                //changeNb--;
                            }
                        }
                });

        });*/
    }


    public void updateAllOrdersPM(OrderPM orderPM){
        String id = orderPM.getOrderId();
        OrderPM existingOrder = syncAllOrdersMap.get(id);
        if(existingOrder != null){
            syncAllOrders.remove(existingOrder);
        }
        syncAllOrders.add(orderPM);
        syncAllOrdersMap.put(id, orderPM);
    }

    public ObservableList<OrderPM> getSyncAllOrders() {
        return this.syncAllOrders;
    }
    public ObservableList<OrderPM> getAllOrders()
    {
        return this.allOrders;
    }

    // Show an Info Alert with default header Text
    private void showAlertWithDefaultHeaderText(boolean updated, OrderPM changedOrder) {
        final String NotifyICON = "\uf0f3";
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Order "+ changedOrder.getOrderId()+ " Notification");
        alert.setResizable(true);
        alert.initStyle(StageStyle.DECORATED);
        String title ="Order Notification";
        alert.setTitle(title);

        StringBuilder content = new StringBuilder(changedOrder.getOrderId());

           if( updated) {
               content.append(" Order updated ")
                       .append( changedOrder.getOrderId() )
                       .append("\n Order Status: ")
                       .append(changedOrder.getOrderStatus().toString())
                       .append("\n Assigned to the Courier: ")
                       .append(changedOrder.getCurrentAssignee());

            }else{
               content.append( " New order: ")
                       .append(changedOrder.getOrderId())
                       .append("\n Order Status ")
                       .append(changedOrder.getOrderStatus())
                       .append("\n Order Placed on : ")
                       .append(DATE_FORMATTER.format(changedOrder.getOrderPlacedWhen())
                       );
            }
        alert.setContentText(content.toString());
        alert.showAndWait();
    }


    public ObservableList<OrderPM> getAllOrderEntries() {
        return allOrderEntries.get();
    }

    public ObjectProperty<ObservableList<OrderPM>> allOrderEntriesProperty() {
        return allOrderEntries;
    }

    public void setAllOrderEntries(ObservableList<OrderPM> allOrderEntries) {
        this.allOrderEntries.set(allOrderEntries);
    }

    public ObservableMap<String, OrderPM> getSyncAllOrdersMap() {
        return syncAllOrdersMap;
    }


    @Override
    public void handleNewOrderEvent(OrderPM order) {

        this.updateAllOrdersPM(order);
        Platform.runLater(() ->showAlertWithDefaultHeaderText(false, order));
    }

    @Override
    public void handleOrderUpdateEvent(OrderPM orderPM) {
        this.updateAllOrdersPM(orderPM);
        Platform.runLater(() ->showAlertWithDefaultHeaderText(true, orderPM));
    }


}



