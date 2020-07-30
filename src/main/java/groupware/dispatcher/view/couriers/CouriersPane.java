package groupware.dispatcher.view.couriers;


import groupware.dispatcher.presentationmodel.AllCouriersPM;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CouriersPane extends HBox implements ViewMixin {

    public CouriersTable table;
    private AllCouriersPM allCouriersPM;
    private CourierProfilePane courierActivityPane;

    public CouriersPane( AllCouriersPM allCouriersPM){
        this.allCouriersPM = allCouriersPM;
        init();
    }

    @Override
    public void initializeSelf() {
        getStyleClass().add("couriers-pane");

    }

    @Override
    public void initializeParts() {
         table = new CouriersTable(allCouriersPM);
         courierActivityPane = new CourierProfilePane(allCouriersPM);
    }

    @Override
    public void layoutParts() {
        this.getChildren().addAll(table, courierActivityPane);

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
