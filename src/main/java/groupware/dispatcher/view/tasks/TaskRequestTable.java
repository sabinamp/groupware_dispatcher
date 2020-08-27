package groupware.dispatcher.view.tasks;

import groupware.dispatcher.presentationmodel.*;
import groupware.dispatcher.service.model.DeliveryType;
import groupware.dispatcher.service.model.RequestReply;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableObjectValue;
import javafx.beans.value.WritableStringValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.skin.TableViewSkinBase;
import javafx.util.Callback;

import java.util.Arrays;
import java.util.Map;


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

        TableColumn<TaskRequestPM, RequestReply> replyColumn = new TableColumn<>("Reply");
        replyColumn.setCellValueFactory(cell->  cell.getValue().requestReplyProperty());
        replyColumn.setCellFactory(cell-> new ReplyCell());
      /* replyColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TaskRequestPM, RequestReply>, ObservableValue<RequestReply>>() {
            @Override
            public ObservableValue<RequestReply> call(TableColumn.CellDataFeatures<TaskRequestPM, RequestReply> param) {
                WritableObjectValue<RequestReply> result=param.getValue().requestReplyProperty();
                return (ObservableValue<RequestReply>) result;
            }
        });*/
        replyColumn.setMinWidth(100);

        TableColumn<TaskRequestPM, Boolean> columnDone = new TableColumn<>("Status");
        columnDone.setCellValueFactory(cell-> cell.getValue().doneProperty());
        columnDone.setCellFactory(cell-> new BooleanCell());
        columnDone.setMinWidth(100);

        getColumns().addAll(Arrays.asList(columnTaskId, columnCourierId, columnOrderID, columnType, replyColumn, columnDone));


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

    }



  @Override
    public void setupBindings() {

        itemsProperty().bind(taskPModel.allTaskEntriesProperty());

    }


   /* @Override
    public void handlePMUpdateEvent() {
        Platform.runLater(new Runnable() {
            public void run() {
                updateMyTableView();
            }
        });
    }

    private void updateMyTableView() {
      getItems().clear();
      getItems().addAll(taskPModel.getSyncAllTasks());
    }*/
}
