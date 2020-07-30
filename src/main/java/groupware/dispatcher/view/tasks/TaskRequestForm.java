package groupware.dispatcher.view.tasks;

import groupware.dispatcher.presentationmodel.AllCouriersPM;
import groupware.dispatcher.presentationmodel.AllOrdersPM;
import groupware.dispatcher.presentationmodel.TaskRequestPM;
import groupware.dispatcher.service.model.DeliveryType;
import groupware.dispatcher.service.model.TaskRequest;
import groupware.dispatcher.service.model.TaskType;
import groupware.dispatcher.view.util.SimpleTextControl;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TaskRequestForm extends GridPane implements ViewMixin {

    private Text taskDueOnLabel;
    private Text courierIdLabel;
    private Text orderIdLabel;
    private Text addressLineLabel;
    private Text deliveryTypeLabel;
    private Text taskTypeLabel;

    private Text addressLineField;
    private AllOrdersPM allOrdersPM;
    private AllCouriersPM allCouriersPM;
    private DatePicker datePicker;
    private ChoiceBox<String> orderIdChoiceBox;
    private ChoiceBox<String> courierIdChoiceBox;
    private ChoiceBox<DeliveryType> deliveryTypeChoiceBox;
    private ChoiceBox<TaskType> taskTypeChoiceBox;
    private javafx.scene.control.Button addBtn;




    private ObjectProperty<TaskRequestPM> currentTask= new SimpleObjectProperty<>();

    public TaskRequestForm(AllOrdersPM allOrdersPM, AllCouriersPM allCouriersPM){
        this.allOrdersPM = allOrdersPM;
        this.allCouriersPM = allCouriersPM;
        init();
    }


    @Override
    public void initializeSelf() {
        setMinSize(200, 500);
        setStyle("-fx-background-color: WHITE;");
        //Setting the padding
        setPadding(new Insets(10));
        //Setting the vertical and horizontal gaps between the columns
        setVgap(5);
        setHgap(5);

        //Setting the Grid alignment
        setAlignment(Pos.CENTER);
    }

    @Override
    public void initializeParts() {
     addBtn = new javafx.scene.control.Button("Send Task Request");
     addBtn.setOnAction(e -> {
            TaskRequestPM newTask = new TaskRequestPM();
            // just a way to generate a new task request

            //set task fields
            //to do add a task AllTaskRequestsPM
        });

        courierIdLabel = new Text("Courier ID");
        orderIdLabel = new Text("Order ID");

        taskDueOnLabel = new Text("Due ");

        addressLineLabel = new Text("Address Line");
        taskTypeLabel = new Text("Task");
        //date picker to choose date
        datePicker = new DatePicker();
        orderIdChoiceBox = new ChoiceBox<>();
        orderIdChoiceBox.getItems().addAll
                (allOrdersPM.getSyncAllOrdersMap().keySet());
        courierIdChoiceBox = new ChoiceBox<>();
        addressLineField = new Text("...");
        deliveryTypeLabel = new Text("Delivery Type");
        deliveryTypeChoiceBox = new ChoiceBox<DeliveryType>();
        deliveryTypeChoiceBox.getItems().addAll(DeliveryType.STANDARD, DeliveryType.URGENT);
        taskTypeChoiceBox = new ChoiceBox<>();
        taskTypeChoiceBox.getItems().addAll(TaskType.DELIVERY_FIRST, TaskType.DELIVERY_SECOND, TaskType.PARCEL_COLLECTION);
        //courierIdChoiceBox.getItems().addAll(allCouriersPM.getAllCouriers().stream().map(c->c.getCourierId()).collect(Collectors.toSet()));
    }

    @Override
    public void layoutParts() {
         add(courierIdLabel, 1, 1);
         add(orderIdLabel, 1, 2);
         add(taskDueOnLabel, 1, 3);
         add(addressLineLabel, 1, 4);
         add(deliveryTypeLabel, 1, 5);
         add(taskTypeLabel, 1,6);

         add(courierIdChoiceBox, 2,1);
         add(orderIdChoiceBox, 2, 2);
         add(datePicker, 2, 3);
         add(addressLineField, 2,4);
         add(deliveryTypeChoiceBox, 2,5);
         add(taskTypeChoiceBox,2,6);

         add(addBtn,1, 9);
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

    public TaskRequestPM getCurrentTask() {
        return currentTask.get();
    }

    public ObjectProperty<TaskRequestPM> currentTaskProperty() {
        return currentTask;
    }

    public void setCurrentTask(TaskRequestPM currentTask) {
        this.currentTask.set(currentTask);
    }
}
