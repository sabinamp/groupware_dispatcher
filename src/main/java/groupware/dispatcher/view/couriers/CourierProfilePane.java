package groupware.dispatcher.view.couriers;


import groupware.dispatcher.presentationmodel.AllCouriersPM;
import groupware.dispatcher.presentationmodel.CourierPM;
import groupware.dispatcher.service.model.Conn;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.Set;

public class CourierProfilePane extends GridPane implements ViewMixin {

    private Text paneTitle;
    private AllCouriersPM pmodel;
    private ObjectProperty<CourierPM> currentCourierPM = new SimpleObjectProperty<>();
    private ImageView courierAvatar;
    private Label courierIdL;

    private Label courierPhoneL;
    private Label assignedOrdersL;

    private Text courierIdTxt ;
    private Text courierNameTxt;
    private Text assignedOrdersTxt;
    private Text courierPhoneTxt;

    private Text courierStatusTxt;
    private HBox connectionStatusHBox;
    private Circle onlineCircle;
    private Circle offlineCircle;

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
        setPadding(new Insets(10, 10,10,10));
        paneTitle= new Text("COURIER PROFILE");
        paneTitle.setFont(Font.font ("Roboto Regular", FontWeight.BOLD, FontPosture.REGULAR, 16));

        courierPhoneL = new Label("Phone Number");
        courierIdL = new Label("Courier ID");
        assignedOrdersL = new Label("Current Orders: ");

        courierIdTxt = new Text("...");
        courierNameTxt = new Text("...");
        assignedOrdersTxt = new Text("...");
        courierPhoneTxt = new Text("...");
        courierStatusTxt = new Text("...");
        courierIdL.setLabelFor(courierIdTxt);

        assignedOrdersL.setLabelFor(assignedOrdersTxt);
        courierPhoneL.setLabelFor(courierPhoneTxt);
        courierAvatar = new ImageView();

        courierAvatar.setFitHeight(40);
        courierAvatar.setFitWidth(40);
        connectionStatusHBox = new HBox();
        connectionStatusHBox.setPadding(new Insets(0, 4, 4, 0));
        connectionStatusHBox.setSpacing(5);

        Color onlineColor = Color.rgb(25, 75, 102);
        Color offlineColor = Color.rgb(255, 102, 102);
        onlineCircle= new Circle(30,30,8, onlineColor);
        offlineCircle= new Circle(30,30,8, offlineColor);
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

        addRow(1, courierAvatar, courierNameTxt);
        addRow(2, connectionStatusHBox);
        addRow(3, courierIdL, courierIdTxt);
        addRow(4, courierPhoneL, courierPhoneTxt);
        addRow(5, new Label("Activity"));
        addRow(6, assignedOrdersL, assignedOrdersTxt);


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
        courierIdTxt = new Text(current.getCourierId());
        courierNameTxt = new Text(current.getName());
        String phone= current.getCourierPhoneNumber();
        Set<String> assignedOrders= current.getAssignedOrders();
        String assignedOrdersTxtContent = "No assigned orders yet.";
        if(assignedOrders != null && !assignedOrders.isEmpty()){
            assignedOrdersTxtContent = assignedOrders.toString();
        }
        assignedOrdersTxt = new Text(assignedOrdersTxtContent);

        courierPhoneTxt= new Text(phone);

        courierStatusTxt = new Text(current.getCourierConnectionStatus().toString());
        add(courierAvatar, 0, 1);
        add(courierNameTxt, 1, 1);
        add(connectionStatusHBox, 1,2);
        add(courierIdTxt,1,3);

        add(courierPhoneTxt, 1, 4);
        add(new Label("Activity"),0, 5);
        add(assignedOrdersTxt,1,6);

        if(current.getCourierConnectionStatus().equals(Conn.ONLINE)){
           connectionStatusHBox.getChildren().add(onlineCircle);
        }else{
           connectionStatusHBox.getChildren().add(offlineCircle);
        }
        connectionStatusHBox.getChildren().add(courierStatusTxt);
    }

    private void deleteContent() {
        connectionStatusHBox.getChildren().removeAll(offlineCircle, courierStatusTxt);
        connectionStatusHBox.getChildren().remove(onlineCircle);
        getChildren().removeAll( courierIdTxt, courierNameTxt, assignedOrdersTxt, courierPhoneTxt, courierAvatar, connectionStatusHBox);
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
           courierStatusTxt.textProperty().bind(currentCourierPM.courierConnectionStatusProperty().asString());
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
