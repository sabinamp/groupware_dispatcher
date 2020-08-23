package groupware.dispatcher.view.orders;

import groupware.dispatcher.presentationmodel.AllOrdersPM;
import groupware.dispatcher.presentationmodel.OrderPM;
import groupware.dispatcher.service.model.ContactInfo;
import groupware.dispatcher.service.model.DeliveryStep;
import groupware.dispatcher.service.model.DeliveryType;
import groupware.dispatcher.service.model.OrderStatus;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDateTime;
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
        this.setPrefSize(800, 400);

    }

    @Override
    public void initializeParts() {

        TableColumn<OrderPM, String> columnId = new TableColumn<>("Order ID");
        columnId.setCellValueFactory(cell -> cell.getValue().orderIdProperty());
        columnId.setCellFactory(cell -> new OrderIDCell());
        columnId.setMinWidth(80);

        TableColumn<OrderPM, DeliveryType> typeColumn = new TableColumn<>("Delivery Type");
        typeColumn.setCellValueFactory(cell -> cell.getValue().orderTypeProperty());
        typeColumn.setCellFactory(cell -> new DeliveryTypeCell());
        typeColumn.setMinWidth(100);

        TableColumn<OrderPM, LocalDateTime> placedWhenColumn = new TableColumn<>("Placed");
        placedWhenColumn.setCellValueFactory(cell -> cell.getValue().orderPlacedWhenProperty());
        placedWhenColumn.setCellFactory(cell -> new OrderPlacedWhenCell());
        placedWhenColumn.setMinWidth(100);

        TableColumn<OrderPM, String> customerNameColumn = new TableColumn<>("Customer Name");
        customerNameColumn.setCellValueFactory(cell -> cell.getValue().customerNameProperty());
        customerNameColumn.setCellFactory(cell -> new TxtCell());
        customerNameColumn.setMinWidth(100);

        TableColumn<OrderPM, ContactInfo> columnAddress = new TableColumn<>("Destination Address");
        columnAddress.setCellValueFactory(cell -> cell.getValue().destinationAddressProperty());
        columnAddress.setCellFactory(cell -> new DestinationAddressCell());
        columnAddress.setMinWidth(150);

        TableColumn<OrderPM, OrderStatus> statusColumn = new TableColumn<>("Order Status");
        statusColumn.setCellValueFactory(cell -> cell.getValue().orderStatusProperty());
        statusColumn.setCellFactory(cell -> new OrderStatusCell());
        statusColumn.setMinWidth(150);

        TableColumn<OrderPM, String> assigneeColumn = new TableColumn<>("Updated by");
        assigneeColumn.setCellValueFactory(cell -> cell.getValue().currentAssigneeProperty());
        assigneeColumn.setCellFactory(cell -> new TxtCell());
        assigneeColumn.setMinWidth(80);


        TableColumn<OrderPM, LocalDateTime> updatedWhenColumn = new TableColumn<>("Updated When");
        updatedWhenColumn.setCellValueFactory(cell -> cell.getValue().orderUpdatedWhenProperty());
        updatedWhenColumn.setCellFactory(cell -> new OrderUpdatedWhenCell());
        updatedWhenColumn.setMinWidth(100);

        getColumns().addAll(Arrays.asList(columnId, typeColumn, placedWhenColumn, customerNameColumn, columnAddress, statusColumn,
                assigneeColumn, updatedWhenColumn));
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