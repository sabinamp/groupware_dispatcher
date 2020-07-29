package groupware.dispatcher.view.tasks;

import groupware.dispatcher.presentationmodel.AllCouriersPM;
import groupware.dispatcher.presentationmodel.AllOrdersPM;
import groupware.dispatcher.presentationmodel.TaskRequestPM;
import groupware.dispatcher.view.util.SimpleTextControl;
import groupware.dispatcher.view.util.ViewMixin;
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

    private TextField orderIdField;
    private TextField courierIdField;
    private TextField addressLineField;
    private AllOrdersPM allOrdersPM;
    private AllCouriersPM allCouriersPM;
    private DatePicker datePicker;
    private ChoiceBox<String> orderIdChoiceBox;
    private ChoiceBox<String> courierIdChoiceBox;
    private javafx.scene.control.Button addBtn;

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
     addBtn = new javafx.scene.control.Button("New Task");
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

        //date picker to choose date
        datePicker = new DatePicker();
        orderIdChoiceBox = new ChoiceBox<>();
        orderIdChoiceBox.getItems().addAll
                (allOrdersPM.getSyncAllOrdersMap().keySet());
        courierIdChoiceBox = new ChoiceBox<>();
        courierIdChoiceBox.getItems().addAll(allCouriersPM.getAllCouriers().stream().map(c->c.getCourierId()).collect(Collectors.toSet()));
    }

    @Override
    public void layoutParts() {
         add(courierIdLabel, 1, 1);
         add(orderIdLabel, 1, 2);
         add(taskDueOnLabel, 1, 3);
         add(addressLineLabel, 1, 4);
        add(courierIdChoiceBox, 2,1);
         add(orderIdChoiceBox, 2, 2);

         add(datePicker, 2, 3);
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
}
