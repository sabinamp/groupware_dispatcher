package groupware.dispatcher.view.couriers;


import groupware.dispatcher.presentationmodel.AllCouriersPM;
import groupware.dispatcher.presentationmodel.CourierPM;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Set;

public class CourierProfilePane extends GridPane implements ViewMixin {

    private Text paneTitle;
    private AllCouriersPM pmodel;
    private ObjectProperty<CourierPM> currentCourierPM = new SimpleObjectProperty<>();
    private ImageView courierAvatar;
    private Label courierIdL;

    private Label courierPhoneL;
    private Label assignedOrdersL;
   // private Label currentTaskRequestsL;

    private Text courierIdTxt ;
    private Text courierNameTxt;
    private Text assignedOrdersTxt;
    private Text courierPhoneTxt;
  //  private Text currentTaskRequestsTxt;



    public CourierProfilePane(AllCouriersPM allCouriersPM){
        this.pmodel = allCouriersPM;
        setCurrentCourierPM(allCouriersPM.getCurrentCourierPM());
        init();

    }

    @Override
    public void initializeSelf() {
        getStyleClass().add("activity-pane");
        setPrefWidth(350);
    }

    @Override
    public void initializeParts() {
        setPadding(new Insets(10, 10,10,8));
        paneTitle= new Text("COURIER PROFILE");
        paneTitle.setFont(Font.font ("Roboto Regular", FontWeight.BOLD, FontPosture.REGULAR, 16));

        courierPhoneL = new Label("Phone Number");
        courierIdL = new Label("Courier ID");
        assignedOrdersL = new Label("Current Orders: ");

        courierIdTxt = new Text("...");
        courierNameTxt = new Text("...");
        assignedOrdersTxt = new Text("...");
        courierPhoneTxt = new Text("...");
        courierIdL.setLabelFor(courierIdTxt);

        assignedOrdersL.setLabelFor(assignedOrdersTxt);
        courierPhoneL.setLabelFor(courierPhoneTxt);
        courierAvatar = new ImageView();

        courierAvatar.setFitHeight(50);
        courierAvatar.setFitWidth(50);
    }

    @Override
    public void layoutParts() {
        this.getChildren().add(paneTitle);
        ColumnConstraints firstLabelCol = new ColumnConstraints();
        firstLabelCol.setMaxWidth(ConstraintsBase.CONSTRAIN_TO_PREF);
        firstLabelCol.setMinWidth(140);
        firstLabelCol.setPrefWidth(150);

        ColumnConstraints firstTxtCol = new ColumnConstraints();
        firstTxtCol.setMinWidth(140);
        firstTxtCol.setFillWidth(true);
        firstTxtCol.setHgrow(Priority.ALWAYS);

       getColumnConstraints().addAll(firstLabelCol, firstTxtCol);

        Color greenOnline = Color.rgb(102, 255, 102);
        Circle circle= new Circle(30,30,8, greenOnline);
        addRow(1, courierAvatar, courierNameTxt);
        addRow(2, courierIdL, courierIdTxt);

        addRow(4, assignedOrdersL, assignedOrdersTxt);
        addRow(5, courierPhoneL, courierPhoneTxt);
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
        String courierId = current.getCourierId();


        //Setting the image view
        courierAvatar = new ImageView(CourierImage.getImage(courierId));

        //Setting the position of the image

        courierIdTxt = new Text(getCurrentCourierPM().getCourierId());
        courierNameTxt = new Text(current.getName());
        String phone= current.getCourierPhoneNumber();
        Set<String> assignedOrders= current.getAssignedOrders();
        String assignedOrdersTxtContent = "No assigned orders yet.";
        if(assignedOrders != null && !assignedOrders.isEmpty()){
            assignedOrdersTxtContent = assignedOrders.toString();
        }
        assignedOrdersTxt = new Text(assignedOrdersTxtContent);

        courierPhoneTxt= new Text(phone);
        add(courierAvatar, 0, 1);
        add(courierIdTxt,1,2);
        add(courierNameTxt, 1, 1);
        add(assignedOrdersTxt,1,4);
        add(courierPhoneTxt, 1, 5);
    }

    private void deleteContent() {
        getChildren().removeAll( courierIdTxt, courierNameTxt, assignedOrdersTxt, courierPhoneTxt, courierAvatar);
    }

    @Override
    public void setupBindings() {
        currentCourierPMProperty().bind(this.pmodel.currentCourierPMProperty());
       CourierPM currentCourierPM = this.getCurrentCourierPM();
       if(currentCourierPM != null){
           courierIdTxt.textProperty().bind(currentCourierPM.courierIdProperty());
           courierNameTxt.textProperty().bind(currentCourierPM.nameProperty());
           assignedOrdersTxt.textProperty().bind(currentCourierPM.assignedOrdersProperty().asString());
           courierPhoneTxt.textProperty().bind(currentCourierPM.courierPhoneNumberProperty());
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