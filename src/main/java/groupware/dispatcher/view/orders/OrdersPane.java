package groupware.dispatcher.view.orders;

import groupware.dispatcher.presentationmodel.AllCouriersPM;
import groupware.dispatcher.presentationmodel.AllOrdersPM;
import groupware.dispatcher.view.couriers.CouriersTable;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class OrdersPane extends VBox implements ViewMixin {
    private AllOrdersPM allOrdersPM;

    public OrdersPane(AllOrdersPM allOrdersPM){
        this.allOrdersPM = allOrdersPM;
        init();
    }
    @Override
    public void initializeSelf() {

        getStyleClass().add("orders_pane");

    }

    @Override
    public void initializeParts() {
        //table = new OrdersTable(allOrdersPM);
    }

    @Override
    public void layoutParts() {
        //this.getChildren().add(table);

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

}
