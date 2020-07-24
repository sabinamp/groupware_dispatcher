package groupware.dispatcher.view.couriers;


import groupware.dispatcher.presentationmodel.AllCouriersPM;
import groupware.dispatcher.presentationmodel.CourierPM;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class CourierActivityPane extends GridPane implements ViewMixin {

    private Text paneTitle;
    private AllCouriersPM pmodel;
    private ObjectProperty<CourierPM> currentCourierPM = new SimpleObjectProperty<>();

    private Label courierIdL /*= new Label("Courier ID")*/;
    private Label courierNameL /*= new Label("Courier Name")*/;
    private Label assignedOrdersL /*= new Label("Activity")*/;
   // private Label currentTaskRequestsL;

    private Text courierIdTxt /*= new Text("...")*/;
    private Text courierNameTxt /*= new Text("...")*/;
    private Text assignedOrdersTxt /*= new Text("...")*/;
  //  private Text currentTaskRequestsTxt;



    public CourierActivityPane(AllCouriersPM allCouriersPM){
        this.pmodel = allCouriersPM;
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

        courierNameL = new Label("Courier Name");

        courierIdL = new Label("Courier ID");
        assignedOrdersL = new Label("Activity");
        /*currentTaskRequestsL = new Label("Task Requests (Not confirmed)");*/
        courierIdTxt = new Text("...");
        courierNameTxt = new Text("...");
        assignedOrdersTxt = new Text("...");
        //currentTaskRequestsTxt = new Text("...");
        courierIdL.setLabelFor(courierIdTxt);
        courierNameL.setLabelFor(courierNameL);
        assignedOrdersL.setLabelFor(assignedOrdersTxt);

    }

    @Override
    public void layoutParts() {
        this.getChildren().add(paneTitle);
        ColumnConstraints firstLabelCol = new ColumnConstraints();
        firstLabelCol.setMaxWidth(ConstraintsBase.CONSTRAIN_TO_PREF);
        firstLabelCol.setMinWidth(130);
        firstLabelCol.setPrefWidth(130);

        ColumnConstraints firstTxtCol = new ColumnConstraints();
        firstTxtCol.setMinWidth(120);
        firstTxtCol.setFillWidth(true);
        firstTxtCol.setHgrow(Priority.ALWAYS);




       getColumnConstraints().addAll(firstLabelCol, firstTxtCol);
        Region spacer = new Region();

      /*  addRow(0, courierIdL, courierIdTxt, spacer );
        addRow(1, courierNameL, courierNameTxt, spacer);
        addRow(2, assignedOrdersL, assignedOrdersTxt, spacer);*/
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
        add(courierIdTxt,1,0);
        add(courierNameTxt, 1, 1);
        add(assignedOrdersTxt,1,2);
    }

    private void deleteContent() {
        getChildren().removeAll( courierIdTxt, courierNameTxt, assignedOrdersTxt);
    }

    @Override
    public void setupBindings() {
        currentCourierPMProperty().bind(this.pmodel.currentCourierPMProperty());
       /* CourierPM currentCourierPM = this.getCurrentCourierPM();
        courierIdL.textProperty().bind(currentCourierPM.courierIdProperty());
        courierNameL.textProperty().bind(currentCourierPM.nameProperty());
        assignedOrdersL.textProperty().bind(currentCourierPM.assignedOrdersProperty().asString());
*/


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
