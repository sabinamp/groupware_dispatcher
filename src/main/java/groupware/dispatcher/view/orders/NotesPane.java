package groupware.dispatcher.view.orders;


import groupware.dispatcher.presentationmodel.AllOrdersPM;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.scene.layout.VBox;

public class NotesPane extends VBox implements ViewMixin {
    private AllOrdersPM pmodel;
    public NotesPane(AllOrdersPM allOrdersPM){
        this.pmodel = allOrdersPM;
        init();
    }


    @Override
    public void initializeSelf() {
        getStyleClass().add("activity-pane");
        setPrefWidth(250);
    }

    @Override
    public void initializeParts() {

    }

    @Override
    public void layoutParts() {

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

    @Override
    public void addStylesheetFiles(String... stylesheetFile) {

    }
}
