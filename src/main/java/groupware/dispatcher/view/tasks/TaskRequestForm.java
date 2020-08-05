package groupware.dispatcher.view.tasks;

import groupware.dispatcher.presentationmodel.*;
import groupware.dispatcher.service.model.CourierInfo;
import groupware.dispatcher.service.model.DeliveryType;
import groupware.dispatcher.service.model.TaskRequest;
import groupware.dispatcher.service.model.TaskType;
import groupware.dispatcher.view.util.SimpleTextControl;
import groupware.dispatcher.view.util.UserEvent;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TaskRequestForm extends GridPane implements ViewMixin {
    private static int currentTaskIdentifier = 0;
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
    private javafx.scene.control.Button addBtn;




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
        //Setting the vertical and horizontal gaps between the columns
        setVgap(5);
        setHgap(5);

        //Setting the Grid alignment
        setAlignment(Pos.CENTER);
    }

    @Override
    public void initializeParts() {
     addBtn = new javafx.scene.control.Button("Send Request");


        courierIdLabel = new Text("Courier ID");
        orderIdLabel = new Text("Order ID");

        taskDueOnLabel = new Text("Due ");

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
        /*assets.add("C100");
        assets.add("C101");
        assets.add("C102");
        assets.add("C103");
        assets.add("C104");
        assets.add("C105");
        assets.add("C106");
        assets.add("C107");*/
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
         add(courierIdLabel, 1, 1);
         add(orderIdLabel, 1, 2);
         add(taskDueOnLabel, 1, 3);

         add(deliveryTypeLabel, 1, 5);
         add(taskTypeLabel, 1,6);

         add(courierIdChoiceBox, 2,1);
         add(orderIdChoiceBox, 2, 2);
         add(datePicker, 2, 3);

         add(deliveryTypeChoiceBox, 2,5);
         add(taskTypeChoiceBox,2,6);

         add(addBtn,1, 9);
    }

    @Override
    public void setupEventHandlers() {
        addBtn.setOnAction(this::send);
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

    private void send(ActionEvent evt) {
        currentTaskIdentifier++;
        String relatedOrderId= orderIdChoiceBox.getValue().getOrderId();
        String assigneeId = courierIdChoiceBox.getValue().getCourierId();
        System.out.println("TaskRequestForm - Saving " + datePicker.getValue() + taskTypeChoiceBox.getValue()+ deliveryTypeChoiceBox.getValue()
        + "orderId "+relatedOrderId + "courierID " + assigneeId);
        if(relatedOrderId != null && assigneeId != null){
            TaskRequestPM task = new TaskRequestPM();
            task.setTaskId("T"+currentTaskIdentifier);
            task.setTaskType(taskTypeChoiceBox.getValue());
            task.setDeliveryType(deliveryTypeChoiceBox.getValue());
            task.setAccepted(false);
            task.setOrderId(orderIdChoiceBox.getValue().getOrderId());
            task.setAssigneeId(assigneeId);
            LocalDate date=datePicker.getValue();
            task.setDueOn( LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(),
                    LocalDateTime.MIN.getHour()+17, 0, 0));
            task.setDone(false);

            this.allTaskRequestsPM.updateAllTaskRequestsPM(task);
            UserEvent taskEvent= new UserEvent(UserEvent.NEW_TASK);
            this.fireEvent(taskEvent);
        }else{
           System.out.println("Please choose an order and a courier id.");
        }

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
