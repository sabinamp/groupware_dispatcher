package groupware.dispatcher.view.couriers;


import groupware.dispatcher.presentationmodel.AllCouriersPM;
import groupware.dispatcher.presentationmodel.CourierPM;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class CourierActivityPane extends HBox implements ViewMixin {

    private Text paneTitle;
    private AllCouriersPM allCouriersPM;
    private ObjectProperty<CourierPM> currentCourierPM = new SimpleObjectProperty<>();
    public CourierActivityPane(AllCouriersPM allCouriersPM){
        this.allCouriersPM = allCouriersPM;
        setCurrentCourierPM(allCouriersPM.getCurrentCourierPM());
        init();

    }

    @Override
    public void initializeSelf() {

        getStyleClass().add("activity-pane");
        setPrefWidth(725);

    }

    @Override
    public void initializeParts() {
        setPadding(new Insets(10, 10,10,8));
        paneTitle= new Text("Courier Activity");
        paneTitle.setFont(Font.font ("Roboto Regular", FontWeight.BOLD, FontPosture.REGULAR, 16));
    }

    @Override
    public void layoutParts() {
        this.getChildren().add(paneTitle);

    }

    @Override
    public void setupEventHandlers() {

    }

    @Override
    public void setupValueChangedListeners() {
        currentCourierPMProperty().addListener((observable, oldValue, newValue) -> {
            deleteContent();
            updateContent();
        });
    }

    private void updateContent() {
    }

    private void deleteContent() {
    }

    @Override
    public void setupBindings() {

    }

    public CourierPM getCurrentCourierPM() {
        return currentCourierPM.get();
    }

    public ObjectProperty<CourierPM> currentCourierPMProperty() {
        return currentCourierPM;
    }

    public void setCurrentCourierPM(CourierPM currentCourierPM) {
        this.currentCourierPM.set(currentCourierPM);
    }

}
