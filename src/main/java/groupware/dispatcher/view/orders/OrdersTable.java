package groupware.dispatcher.view.orders;

import groupware.dispatcher.presentationmodel.AllOrdersPM;
import groupware.dispatcher.presentationmodel.OrderPM;
import groupware.dispatcher.service.model.ContactInfo;
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

        TableColumn<OrderPM, String> columnId = new TableColumn<>("OrderID");
        columnId.setCellValueFactory(cell -> cell.getValue().orderIdProperty());
        columnId.setCellFactory(cell -> new OrderIDCell());
        columnId.setMinWidth(100);

        TableColumn<OrderPM, ContactInfo> columnAddress = new TableColumn<>("Address");
        columnAddress.setCellValueFactory(cell -> cell.getValue().destinationAddressProperty());
        columnAddress.setCellFactory(cell -> new DestinationAddressCell());
        columnAddress.setMinWidth(200);

        getColumns().addAll(Arrays.asList(columnId, columnAddress));
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