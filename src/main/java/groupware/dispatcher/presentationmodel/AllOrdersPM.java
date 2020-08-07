package groupware.dispatcher.presentationmodel;


import groupware.dispatcher.service.model.OrderDescriptiveInfo;
import groupware.dispatcher.service.model.OrderStatus;
import groupware.dispatcher.service.util.ModelObjManager;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AllOrdersPM {
    private final StringProperty paneTitle = new SimpleStringProperty("Orders");
    private final IntegerProperty orderCount = new SimpleIntegerProperty();

    private final ObservableList<OrderPM> allOrders = FXCollections.observableArrayList();
    private final ObservableMap<String, OrderPM> allOrdersMap = FXCollections.observableHashMap();


    private final  ObservableList<String> allOrderIDs = FXCollections.observableArrayList(allOrdersMap.keySet());


    private final ObservableMap<String, OrderPM> syncAllOrdersMap = FXCollections.synchronizedObservableMap(allOrdersMap);
    private final ObservableList<OrderPM> syncAllOrders = FXCollections.synchronizedObservableList(allOrders);



    private final ObjectProperty<ObservableList<OrderPM>> allOrderEntries = new SimpleObjectProperty<>();

    public AllOrdersPM(){
        setupValueChangedListeners();
        setAllOrderEntries(syncAllOrders);

    }

    private void setupValueChangedListeners() {
        syncAllOrdersMap.addListener(new MapChangeListener<>() {
            @Override
            public void onChanged(Change<? extends String, ? extends OrderPM> change) {
                System.out.println("syncAllOrdersMap Update"+ change);
            }
        });


        syncAllOrders.addListener((ListChangeListener.Change<? extends OrderPM> change) -> {

            System.out.println("AllOrdersPM Update"+ change);
            change.next();

            boolean wasUpdated = change.wasUpdated();
            boolean wasAdded = change.wasAdded();
            List<OrderPM> listChanges = new ArrayList<>(change.getList());

            //todo - notification popup
            Platform.runLater(()->{
                int changeNb = change.getList().size();
                    System.out.println("showAlertWithDefaultHeaderText called. The number of updates or new orders " + changeNb);
                    if(listChanges.size() == changeNb){
                        while(changeNb > 0){
                            showAlertWithDefaultHeaderText(wasUpdated, listChanges.get(changeNb-1));
                            changeNb--;
                        }
                    }
                    });

        });
    }

    public void updateAllOrdersPM(OrderPM orderPM){
        syncAllOrders.add(orderPM);
        syncAllOrdersMap.put(orderPM.getOrderId(), orderPM);

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
        String title ="Order Notification";
        alert.setTitle(title);

        StringBuilder content = new StringBuilder(changedOrder.getOrderId());
           // if(changedOrder.getOrderStatus().equals(OrderStatus.PENDING)) {
           if( updated) {
               content.append(" Order updated ")
                       .append( changedOrder.getOrderId() )
                       .append(" \n Order Status: ")
                       .append(changedOrder.getOrderStatus())
                       .append(" \n Assigned to the Courier ")
                       .append(changedOrder.getCurrentAssignee());

            }else{
               content.append( " New order: ")
                       .append(changedOrder.getOrderId())
                       .append(" \n Order Status ")
                       .append(changedOrder.getOrderStatus() )
                       .append(" \n Order Placed on : ")
                       .append(changedOrder.getOrderPlacedWhen().format(DateTimeFormatter.ofPattern("yyyy-mm-dd"))
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





}
