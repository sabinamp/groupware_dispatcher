package groupware.dispatcher.view.orders;

import groupware.dispatcher.presentationmodel.AllOrdersPM;
import groupware.dispatcher.presentationmodel.OrderPM;
import groupware.dispatcher.service.model.ContactInfo;
import groupware.dispatcher.service.model.DeliveryType;
import groupware.dispatcher.service.model.OrderStatus;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Arrays;

public class OrdersTable extends TableView<OrderPM> implements ViewMixin {

    private AllOrdersPM ordersPModel;
    private ObservableList<OrderPM> selectedEntries;
    private ObservableList<Integer> selectedEntryIndex;

    public OrdersTable(AllOrdersPM allOrdersPM) {
        super();
        this.ordersPModel = allOrdersPM;
        init();
    }

    @Override
    public void initializeSelf() {
        this.setPrefSize(900, 400);

    }

    @Override
    public void initializeParts() {

        TableColumn<OrderPM, String> columnId = new TableColumn<>("Order ID");
        columnId.setCellValueFactory(cell -> cell.getValue().orderIdProperty());
        columnId.setCellFactory(cell -> new OrderIDCell());
        columnId.setMinWidth(100);

        TableColumn<OrderPM, DeliveryType> typeColumn = new TableColumn<>("Delivery Type");
        typeColumn.setCellValueFactory(cell -> cell.getValue().orderTypeProperty());
        typeColumn.setCellFactory(cell -> new DeliveryTypeCell());
        typeColumn.setMinWidth(100);

        TableColumn<OrderPM, ContactInfo> columnAddress = new TableColumn<>("Destination Address");
        columnAddress.setCellValueFactory(cell -> cell.getValue().destinationAddressProperty());
        columnAddress.setCellFactory(cell -> new DestinationAddressCell());
        columnAddress.setMinWidth(200);

        TableColumn<OrderPM, OrderStatus> statusColumn = new TableColumn<>("Order Status");
        statusColumn.setCellValueFactory(cell -> cell.getValue().orderStatusProperty());
        statusColumn.setCellFactory(cell -> new OrderStatusCell());
        statusColumn.setMinWidth(100);

        TableColumn<OrderPM, String> assigneeColumn = new TableColumn<>("Assigned To");
        assigneeColumn.setCellValueFactory(cell -> cell.getValue().currentAssigneeProperty());
        assigneeColumn.setCellFactory(cell -> new AssigneeCell());
        assigneeColumn.setMinWidth(100);


        getColumns().addAll(Arrays.asList(columnId, typeColumn, columnAddress, statusColumn, assigneeColumn));
        setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        TableViewSelectionModel<OrderPM> tsm = getSelectionModel();

        tsm.setSelectionMode(SelectionMode.SINGLE);
        setItems(ordersPModel.getSyncAllOrders());
        selectedEntryIndex = tsm.getSelectedIndices();
        selectedEntries = tsm.getSelectedItems();
    }

    @Override
    public void layoutParts() {

    }

    @Override
    public void setupValueChangedListeners() {


    }
}