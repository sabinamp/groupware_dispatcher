package groupware.dispatcher.view.tasks;

import groupware.dispatcher.presentationmodel.*;
import groupware.dispatcher.service.model.DeliveryType;
import groupware.dispatcher.service.model.TaskType;
import groupware.dispatcher.view.util.TaskEvent;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class TaskRequestForm extends VBox implements ViewMixin {
    private static int currentTaskIdentifier = 0;
    private GridPane gp;
    private Text taskDueOnLabel;
    private Text courierIdLabel;
    private Text orderIdLabel;

    private Text deliveryTypeLabel;
    private Text taskTypeLabel;

    private AllOrdersPM allOrdersPM;
    private AllCouriersPM allCouriersPM;
    private AllTaskRequestsPM allTaskRequestsPM;
    private DatePicker datePicker;
    private ChoiceBox<OrderPM> orderIdChoiceBox;
    private ChoiceBox<CourierPM> courierIdChoiceBox;
    private ChoiceBox<DeliveryType> deliveryTypeChoiceBox;
    private ChoiceBox<TaskType> taskTypeChoiceBox;
    private javafx.scene.control.Button saveBtn;
    private Button resetBtn;


    private ObjectProperty<TaskRequestPM> currentTask= new SimpleObjectProperty<>();

    public TaskRequestForm(AllOrdersPM allOrdersPM, AllCouriersPM allCouriersPM, AllTaskRequestsPM allTasks){
        this.allOrdersPM = allOrdersPM;
        this.allCouriersPM = allCouriersPM;
        this.allTaskRequestsPM = allTasks;
        currentTaskProperty().setValue(allTasks.getCurrentTaskRequest());

        init();
    }


    @Override
    public void initializeSelf() {
        setMinSize(200, 500);
        setStyle("-fx-background-color: WHITE;");
        //Setting the padding
        setPadding(new Insets(10));
        //Setting the Grid alignment
        setAlignment(Pos.TOP_CENTER);
    }

    @Override
    public void initializeParts() {
        this.gp = new GridPane();
        //Setting the vertical and horizontal gaps between the columns
        gp.setVgap(10);
        gp.setHgap(5);
        saveBtn = new javafx.scene.control.Button("Send Request");
        resetBtn = new Button("Reset");

        courierIdLabel = new Text("Courier ID");
        orderIdLabel = new Text("Order ID");
        taskDueOnLabel = new Text("Due Date");

        taskTypeLabel = new Text("Task");
        //date picker to choose date
        datePicker = new DatePicker();
        orderIdChoiceBox = new ChoiceBox<>();

        courierIdChoiceBox = new ChoiceBox<>();

        deliveryTypeLabel = new Text("Delivery Type");
        deliveryTypeChoiceBox = new ChoiceBox<>();
        deliveryTypeChoiceBox.getItems().addAll(DeliveryType.STANDARD, DeliveryType.URGENT);
        taskTypeChoiceBox = new ChoiceBox<>();
        taskTypeChoiceBox.getItems().addAll(TaskType.DELIVERY_FIRST, TaskType.DELIVERY_SECOND, TaskType.PARCEL_COLLECTION);


    }

    private void initOrderIdChoiceBox() {
        Set<OrderPM> orderAssets= new HashSet<>();
       orderIdChoiceBox.setConverter(new StringConverter<>() {
           @Override
           public String toString(OrderPM object) {
               return object.getOrderId();
           }

           @Override
           public OrderPM fromString(String string) {
               return null;
           }
       });

        orderIdChoiceBox.getItems().addAll(orderAssets);
        orderIdChoiceBox.setValue(null);
    }

    private void initCourierIdChoiceBox() {
        Set<CourierPM> courierInfos= new HashSet<>();

        System.out.println("initCourierIdChoiceBox - Courier list size: "+courierInfos.size());
        courierIdChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(CourierPM object) {
                return object.getCourierId();
            }

            @Override
            public CourierPM fromString(String string) {
                return null;
            }
        });

        courierIdChoiceBox.getItems().addAll(courierInfos);
        courierIdChoiceBox.setValue(null);
    }

    @Override
    public void layoutParts() {
        initOrderIdChoiceBox();
        initCourierIdChoiceBox();
        gp.add(courierIdLabel, 1, 1);
        gp.add(orderIdLabel, 1, 2);
        gp.add(taskDueOnLabel, 1, 3);

        gp.add(deliveryTypeLabel, 1, 5);
        gp.add(taskTypeLabel, 1,6);

        gp.add(courierIdChoiceBox, 2,1);
        gp.add(orderIdChoiceBox, 2, 2);
        gp.add(datePicker, 2, 3);

        gp.add(deliveryTypeChoiceBox, 2,5);
        gp.add(taskTypeChoiceBox,2,6);


        ButtonBar buttonBar = new ButtonBar();
        buttonBar.setPadding( new Insets(10) );

        ButtonBar.setButtonData(saveBtn, ButtonBar.ButtonData.OK_DONE);
        ButtonBar.setButtonData(resetBtn, ButtonBar.ButtonData.CANCEL_CLOSE);

        buttonBar.getButtons().addAll(saveBtn, resetBtn);

        this.getChildren().addAll( gp, buttonBar );
    }

    @Override
    public void setupEventHandlers() {
        saveBtn.setOnAction(this::save);
        resetBtn.setOnAction(this::reset);
    }


    @Override
    public void setupValueChangedListeners() {

    }

    @Override
    public void setupBindings() {
        orderIdChoiceBox.itemsProperty().bind( allOrdersPM.allOrderEntriesProperty());
        courierIdChoiceBox.itemsProperty().bind(allCouriersPM.allCourierEntriesProperty());
    }

    @Override
    public void addStylesheetFiles(String... stylesheetFile) {

    }

    private void save(ActionEvent evt) {
        currentTaskIdentifier++;
        if(orderIdChoiceBox.getValue() != null && courierIdChoiceBox.getValue() != null && taskTypeChoiceBox.getValue() != null){
            String relatedOrderId = orderIdChoiceBox.getValue().getOrderId();
            String assigneeId = courierIdChoiceBox.getValue().getCourierId();
            System.out.println("TaskRequestForm - Saving " + datePicker.getValue() + taskTypeChoiceBox.getValue()+ deliveryTypeChoiceBox.getValue()
                    + " orderId "+relatedOrderId + " courierID " + assigneeId);
            TaskRequestPM task = new TaskRequestPM();
            task.setTaskId("T"+currentTaskIdentifier);
            task.setOrderId(relatedOrderId);
            task.setAssigneeId(assigneeId);
            task.setTaskType(taskTypeChoiceBox.getValue());
            task.setDeliveryType(deliveryTypeChoiceBox.getValue());
            task.setAccepted(false);
            task.setSentWhen(LocalDateTime.now());

            LocalDate date = datePicker.getValue();
            task.setDueOn( LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(),
                    LocalDateTime.MIN.getHour()+17, 0, 0));
            task.setDone(false);

            this.allTaskRequestsPM.updateAllTaskRequestsPMAndService(task);
            TaskEvent taskEvent= new TaskEvent(TaskEvent.NEW_TASK);
            this.fireEvent(taskEvent);
            resetFields();
        }else{
            showAlertNoCurrentTask();
        }
    }

    private void reset(ActionEvent e) {
           resetFields();
    }

    private void resetFields() {
        //If the Reset button is pressed after filling in the choice boxes, the form is restored to its original values.
        courierIdChoiceBox.setValue(null);
        orderIdChoiceBox.setValue(null);
        deliveryTypeChoiceBox.setValue(null);
        taskTypeChoiceBox.setValue(null);
    }

    private void showAlertNoCurrentTask() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("No current task request filled in.");
        alert.setContentText("There is no current task. \n Fill in the form first. An Order id and a courier id are required.");
        alert.showAndWait();
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
