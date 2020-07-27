package groupware.dispatcher.presentationmodel;


import groupware.dispatcher.service.model.OrderDescriptiveInfo;
import groupware.dispatcher.service.model.OrderStatus;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.*;
import javafx.scene.control.Alert;

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
            //todo - notification popup
            Platform.runLater(()->{
                    int changeNb = change.getList().size();
                    boolean wasUpdated = change.wasUpdated();
                    boolean wasAdded = change.wasAdded();
                    System.out.println("showAlertWithDefaultHeaderText called. The number of updates or new orders " + changeNb);

                        String changedOrder= change.toString();
                        showAlertWithDefaultHeaderText(wasUpdated,changedOrder);
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
    private void showAlertWithDefaultHeaderText(boolean updated, String change /*OrderPM changedOrder*/) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Notification");
        alert.setHeaderText("Order Update");

        String content =change;
           // if(changedOrder.getOrderStatus().equals(OrderStatus.PENDING)) {
          /*  if( !updated) {
                content = "New order added. " + changedOrder.getOrderId() + " Order Status"
                        + changedOrder.getOrderStatus() + "\\n Order Placed on " + changedOrder.getOrderPlacedWhen();
            }else if(updated){
                content = "Order updated "+ changedOrder.getOrderId() +" Order Status "
                        +changedOrder.getOrderStatus()+"\\n Assigned to the Courier" +changedOrder.getCurrentAssignee() ;
            }
*/

       /* if( wasAdded) {
            int changeNb = change.getList().size();
            System.out.println("showAlertWithDefaultHeaderText called. The number of added orders" + changeNb);
            for(int i= 0; i < changeNb; i++){
                OrderPM newOrder= change.getList().get(i);
                content = "New order added. "+ newOrder.getOrderId() +" Order Status"
                        +newOrder.getOrderStatus() +"\n Order Placed on " +newOrder.getOrderPlacedWhen();
            }

        }else if(wasUpdated){
            content = "Order updated "+ updatedOrder.getOrderId() +" Order Status "
                    +updatedOrder.getOrderStatus()+ updatedOrder.getCurrentAssignee() ;

        }*/
        alert.setContentText(content);
        alert.showAndWait();
    }
}
