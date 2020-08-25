package groupware.dispatcher.view.tasks;

import groupware.dispatcher.presentationmodel.*;
import groupware.dispatcher.service.model.DeliveryType;
import groupware.dispatcher.service.model.RequestReply;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.util.Arrays;


public class TaskRequestTable extends TableView<TaskRequestPM> implements ViewMixin {
    private AllTaskRequestsPM taskPModel;

    private ObservableList<TaskRequestPM> selectedEntries;
    private ObservableList<Integer> selectedEntryIndex;
    private TableViewSelectionModel<TaskRequestPM> tsm;

    public TaskRequestTable(AllTaskRequestsPM pm){
        super();
        this.taskPModel = pm;
        init();
    }

    @Override
    public void initializeSelf() {
        this.setPrefSize(500, 400);

    }
    @Override
    public void initializeParts() {

        TableColumn<TaskRequestPM, String> columnTaskId= new TableColumn<>("Task ID");
        columnTaskId.setCellValueFactory(cell->cell.getValue().taskIdProperty());
        columnTaskId.setCellFactory(cell -> new TaskIDCell());
        columnTaskId.setMinWidth(80);

        TableColumn<TaskRequestPM, String> columnOrderID= new TableColumn<>("Order ID");
        columnOrderID.setCellValueFactory(cell->cell.getValue().orderIdProperty());
        columnOrderID.setCellFactory(cell -> new OrderIDCell());
        columnOrderID.setMinWidth(100);

        TableColumn<TaskRequestPM, String> columnCourierId= new TableColumn<>("Assigned To");
        columnCourierId.setCellValueFactory(cell->cell.getValue().assigneeIdProperty());
        columnCourierId.setCellFactory(cell -> new TaskIDCell());
        columnCourierId.setMinWidth(80);

        TableColumn<TaskRequestPM, DeliveryType> columnType = new TableColumn<>("Type");
        columnType.setCellValueFactory(cell-> cell.getValue().deliveryTypeProperty());
        columnType.setCellFactory(cell-> new DeliveryTypeCell());
        columnType.setMinWidth(100);

        TableColumn<TaskRequestPM, RequestReply> columnAccepted = new TableColumn<>("Accepted");
        columnAccepted.setCellValueFactory(cell-> cell.getValue().requestReplyProperty());
        columnAccepted.setCellFactory(cell-> new AcceptedCell());
        columnAccepted.setMinWidth(100);

        TableColumn<TaskRequestPM, Boolean> columnDone = new TableColumn<>("Status");
        columnDone.setCellValueFactory(cell-> cell.getValue().doneProperty());
        columnDone.setCellFactory(cell-> new BooleanCell());
        columnDone.setMinWidth(100);

        getColumns().addAll(Arrays.asList(columnTaskId, columnCourierId, columnOrderID, columnType, columnAccepted, columnDone));


        setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tsm = getSelectionModel();
        tsm.setSelectionMode(SelectionMode.SINGLE);
        setItems(taskPModel.getSyncAllTasks());
        // getting selected items
        selectedEntries = tsm.getSelectedItems();
        selectedEntryIndex = tsm.getSelectedIndices();


    }

    @Override
    public void layoutParts() {
    }

    @Override
    public void setupValueChangedListeners() {
      /*  taskPModel.getSyncAllTasks().addListener(new ListChangeListener<TaskRequestPM>() {
            @Override
            public void onChanged(Change<? extends TaskRequestPM> c) {
                refresh();
            }
        });*/

    }
/*   @Override
    public void setupBindings() {
        // tracking selection
       *//* tsm.selectedIndexProperty().addListener((obs) -> {

            System.out.println("Selected: "+ tsm.getSelectedItems().get(0));
            System.out.println("Focused: " +
                    getFocusModel().getFocusedItem());
        });*//*
        itemsProperty().bind(taskPModel.allTaskEntriesProperty());

    }*/



}
