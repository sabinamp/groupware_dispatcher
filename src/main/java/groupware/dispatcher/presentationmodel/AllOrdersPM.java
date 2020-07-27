package groupware.dispatcher.presentationmodel;


import groupware.dispatcher.service.model.OrderDescriptiveInfo;
import groupware.dispatcher.service.model.OrderStatus;
import groupware.dispatcher.service.util.ModelObjManager;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.*;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AllOrdersPM {
    private final StringProperty paneTitle = new SimpleStringProperty("Orders");
    private final IntegerProperty orderCount = new SimpleIntegerProperty();

    private final ObservableList<OrderPM> allOrders = FXCollections.observableArrayList();



    private final ObservableList<OrderPM> syncAllOrders = FXCollections.synchronizedObservableList(allOrders);
    public AllOrdersPM(){
        setupValueChangedListeners();

    }

    private void setupValueChangedListeners() {

        allOrders.addListener((ListChangeListener.Change<? extends OrderPM> change) -> {

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
    }

    public ObservableList<OrderPM> getSyncAllOrders() {
        return syncAllOrders;
    }

    // Show an Info Alert with default header Text
    private void showAlertWithDefaultHeaderText(boolean updated, OrderPM changedOrder) {
        final String NotifyICON = "\uf0f3";
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Notification");

        String headerTxt ="Order Notification";
        alert.setHeaderText(headerTxt);
        alert.setResizable(true);
        StringBuilder content = new StringBuilder(changedOrder.getOrderId());
           // if(changedOrder.getOrderStatus().equals(OrderStatus.PENDING)) {
           if( updated) {
               content.append("Order updated "+ changedOrder.getOrderId() )
                       .append(" Order Status: ")
                       .append(changedOrder.getOrderStatus())
                       .append(" Assigned to the Courier " +changedOrder.getCurrentAssignee()
                       );

            }else{
               content.append( "New order: ")
                       .append(changedOrder.getOrderId())
                       .append(" Order Status ")
                       .append(changedOrder.getOrderStatus() )
                       .append(" Order Placed on :")
                       .append(changedOrder.getOrderPlacedWhen().format(DateTimeFormatter.ofPattern("yyyy-mm-dd"))

                       );
            }


        alert.setContentText(content.toString());
        alert.showAndWait();
    }
}
