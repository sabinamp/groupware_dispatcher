package groupware.dispatcher.view.tasks;

import groupware.dispatcher.presentationmodel.*;
import groupware.dispatcher.service.model.TaskRequest;
import groupware.dispatcher.view.couriers.IDCell;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Arrays;


public class TaskRequestsTable extends TableView<TaskRequestPM> implements ViewMixin {
    private AllTaskRequestsPM pm;
    public static ObservableList<TaskRequestPM> items =
            FXCollections.observableArrayList( );


    private ObservableList<TaskRequestPM> selectedEntries;
    private ObservableList<Integer> selectedEntryIndex;
    private  TableViewSelectionModel<TaskRequestPM> tsm;

    public TaskRequestsTable(AllTaskRequestsPM pm){
        super();
       this.pm = pm;
        init();
    }

    @Override
    public void initializeParts() {


        TableColumn<TaskRequestPM, String> columnId= new TableColumn<>("Task ID");
        columnId.setCellValueFactory(cell->cell.getValue().taskIdProperty());
        columnId.setCellFactory(cell -> new TaskIDCell());
        columnId.setMinWidth(100);

        TableColumn<TaskRequestPM, String> columnOrderID= new TableColumn<>("Order ID");
        columnId.setCellValueFactory(cell->cell.getValue().orderIdProperty());
        columnId.setCellFactory(cell -> new TaskIDCell());
        columnId.setMinWidth(100);

        TableColumn<TaskRequestPM, String> columnCourierId= new TableColumn<>("Courier ID");
        columnId.setCellValueFactory(cell->cell.getValue().assigneeIdProperty());
        columnId.setCellFactory(cell -> new TaskIDCell());
        columnId.setMinWidth(100);

        TableColumn<TaskRequestPM, Boolean> columnAccepted = new TableColumn<>("Confirmed");
        columnAccepted.setCellValueFactory(cell-> cell.getValue().acceptedProperty());
        columnAccepted.setCellFactory(cell-> new BooleanCell());
        columnId.setMinWidth(100);

        TableColumn<TaskRequestPM, Boolean> columnDone = new TableColumn<>("Done");
        columnAccepted.setCellValueFactory(cell-> cell.getValue().doneProperty());
        columnAccepted.setCellFactory(cell-> new BooleanCell());
        columnId.setMinWidth(100);

        getColumns().addAll(Arrays.asList(columnId, columnCourierId, columnOrderID, columnAccepted, columnDone));
        setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        setItems(pm.getSyncAllTasks());

        tsm = getSelectionModel();

        tsm.setSelectionMode(SelectionMode.SINGLE);
        setItems( pm.getSyncAllTasks());

        // getting selected items
        selectedEntries = tsm.getSelectedItems();
        selectedEntryIndex = tsm.getSelectedIndices();


    }

    @Override
    public void layoutParts() {
    }

    @Override
    public void setupBindings() {
        // tracking selection
        tsm.selectedIndexProperty().addListener((obs) -> {

            System.out.println("Selected: "+ tsm.getSelectedItems().get(0));
            System.out.println("Focused: " +
                    getFocusModel().getFocusedItem());
        });
    }




}
