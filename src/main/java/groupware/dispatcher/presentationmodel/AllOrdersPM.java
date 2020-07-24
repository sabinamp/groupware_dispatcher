package groupware.dispatcher.presentationmodel;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.*;

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

            //todo - notification popup
        });
    }

    public void updateAllOrdersPM(OrderPM orderPM){
        syncAllOrders.add(orderPM);
    }

    public ObservableList<OrderPM> getSyncAllOrders() {
        return syncAllOrders;
    }
}
