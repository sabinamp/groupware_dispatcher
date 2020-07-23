package groupware.dispatcher.view.couriers;


import groupware.dispatcher.presentationmodel.AllCouriersPM;
import groupware.dispatcher.presentationmodel.RootPM;
import groupware.dispatcher.service.CourierServiceImpl;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class CouriersPane extends HBox implements ViewMixin {

    public CouriersTable table;
    private AllCouriersPM allCouriersPM;

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
    }

    @Override
    public void layoutParts() {
        this.getChildren().add(table);

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
