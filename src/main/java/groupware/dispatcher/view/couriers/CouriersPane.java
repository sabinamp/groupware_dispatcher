package groupware.dispatcher.view.couriers;


import groupware.dispatcher.presentationmodel.RootPM;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class CouriersPane extends HBox implements ViewMixin {
    private RootPM rootPM;
    private CouriersTable table;

    public CouriersPane(RootPM rootPM){
        this.rootPM = rootPM;
        init();

    }

    @Override
    public void initializeSelf() {

        getStyleClass().add("couriers-pane");

    }

    @Override
    public void initializeParts() {
     table = new CouriersTable(rootPM);
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
