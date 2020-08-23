package groupware.dispatcher.view.orders;

import groupware.dispatcher.presentationmodel.AllCouriersPM;
import groupware.dispatcher.presentationmodel.AllOrdersPM;
import groupware.dispatcher.view.couriers.CouriersTable;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class OrdersPane extends BorderPane implements ViewMixin {
    private AllOrdersPM allOrdersPM;
    OrdersTable table;
    //NotesPane notesBox;
    public OrdersPane(AllOrdersPM allOrdersPM){
        this.allOrdersPM = allOrdersPM;
        init();
    }
    @Override
    public void initializeSelf() {
        getStyleClass().add("orders_pane");
        setPrefWidth(800);
    }

    @Override
    public void initializeParts() {

        table = new OrdersTable(allOrdersPM);
        //notesBox = new NotesPane(allOrdersPM);
    }

    @Override
    public void layoutParts() {
        setCenter(table);

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
