package groupware.dispatcher.view.tasks;

import groupware.dispatcher.presentationmodel.AllCouriersPM;
import groupware.dispatcher.presentationmodel.AllOrdersPM;
import groupware.dispatcher.view.util.SimpleTextControl;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.awt.*;

public class TaskRequestForm extends GridPane implements ViewMixin {

    private Text taskDueOnLabel;
    private Text courierIdLabel;
    private Text orderIdLabel;
    private Text addressLineLabel;

    private TextField orderIdField;
    private TextField courierIdField;
    private TextField addressLineField;
    private AllOrdersPM allOrdersPM;

    private DatePicker datePicker;

    @Override
    public void init() {

    }

    @Override
    public void initializeSelf() {
        setMinSize(500, 500);
        setStyle("-fx-background-color: BEIGE;");
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
        courierIdLabel = new Text("Courier ID");

        orderIdLabel = new Text("Order ID");
        //orderIdLabel.setPreferredSize(new Dimension(200, 60));
        taskDueOnLabel = new Text("Due ");
        //taskDueOnLabel.setPreferredSize(new Dimension(200, 60));
        addressLineLabel = new Text("Address Line");
        //addressLineLabel.setPreferredSize(new Dimension(200, 60));
        //date picker to choose date
        datePicker = new DatePicker();
        /*ChoiceBox orderIdChoiceBox = new ChoiceBox();
        orderIdChoiceBox.getItems().addAll
                (allOrdersPM.getSyncAllOrdersMap().keySet());*/
    }

    @Override
    public void layoutParts() {
         add(courierIdLabel, 1, 1);
         add(orderIdLabel, 1, 2);
         add(taskDueOnLabel, 1, 3);
         add(addressLineLabel, 1, 4);

         add(datePicker, 2, 3);
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
