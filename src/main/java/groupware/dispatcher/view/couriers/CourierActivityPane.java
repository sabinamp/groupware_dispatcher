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

        courierIdTxt = new Text("...");
        courierNameTxt = new Text("...");
        assignedOrdersTxt = new Text("...");

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


        addRow(1, courierIdL, courierIdTxt);
        addRow(2, courierNameL, courierNameTxt);
        addRow(3, assignedOrdersL, assignedOrdersTxt);
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
        CourierPM current= getCurrentCourierPM();
        courierIdTxt = new Text(getCurrentCourierPM().getCourierId());
        courierNameTxt = new Text(current.getName());
      //  assignedOrdersTxt = new Text(current.getAssignedOrders().toString());


        add(courierIdTxt,1,1);
        add(courierNameTxt, 1, 2);
       // add(assignedOrdersTxt,1,3);
    }

    private void deleteContent() {
        getChildren().removeAll( courierIdTxt, courierNameTxt/*, assignedOrdersTxt*/);
    }

    @Override
    public void setupBindings() {
        currentCourierPMProperty().bind(this.pmodel.currentCourierPMProperty());
       CourierPM currentCourierPM = this.getCurrentCourierPM();
       if(currentCourierPM != null){
           courierIdTxt.textProperty().bind(currentCourierPM.courierIdProperty());
           courierNameTxt.textProperty().bind(currentCourierPM.nameProperty());
           //assignedOrdersTxt.textProperty().bind(currentCourierPM.assignedOrdersProperty().asString());
       }

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
