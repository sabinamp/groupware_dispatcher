package groupware.dispatcher.view.couriers;

import groupware.dispatcher.presentationmodel.AllCouriersPM;
import groupware.dispatcher.presentationmodel.CourierPM;
import groupware.dispatcher.presentationmodel.RootPM;
import groupware.dispatcher.service.model.ContactInfo;
import groupware.dispatcher.service.model.CourierStatus;
import groupware.dispatcher.service.model.Email;
import groupware.dispatcher.service.model.Phone;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Arrays;
import java.util.List;


public class CouriersTable extends TableView<CourierPM> implements ViewMixin {

    private AllCouriersPM couriersPModel;
    private ObservableList<CourierPM> selectedEntries;
    private ObservableList<Integer> selectedEntryIndex;

    public CouriersTable(AllCouriersPM allCouriersPM){
        super();
        this.couriersPModel = allCouriersPM;
        init();
    }

    @Override
    public void initializeSelf() {
        this.setPrefSize(900, 400);

    }

    @Override
    public void initializeParts() {

        TableColumn<CourierPM, String> columnId= new TableColumn<>("Courier ID");
        columnId.setCellValueFactory(cell->cell.getValue().courierIdProperty());
        columnId.setCellFactory(cell -> new IDCell());
        columnId.setMinWidth(100);

        TableColumn<CourierPM, String> columnName = new TableColumn<>("Courier Name");
        columnName.setCellValueFactory(cell->cell.getValue().nameProperty());
        columnName.setCellFactory(cell -> new NameCell());
        columnName.setMinWidth(180);


        TableColumn<CourierPM, CourierStatus> columnStatus = new TableColumn<>("Courier Status");
        columnStatus.setCellValueFactory(cell->cell.getValue().courierStatusProperty());
        columnStatus.setCellFactory(cell-> new CourierStatusCell());
        columnStatus.setMinWidth(200);

        TableColumn<CourierPM, String> columnPhoneNumber = new TableColumn<>("Phone Number");
        columnPhoneNumber.setCellValueFactory(cell->cell.getValue().courierPhoneNumberProperty());
        columnPhoneNumber.setCellFactory(cell -> new PhoneNumberCell());
        columnPhoneNumber.setMinWidth(180);

        TableColumn<CourierPM, Email> columnEmail = new TableColumn<>("Email");
        columnEmail.setCellValueFactory(cell->cell.getValue().courierEmailProperty());
        columnEmail.setCellFactory(cell -> new EmailCell());
        columnEmail.setMinWidth(200);

        getColumns().addAll(Arrays.asList(columnId, columnName, columnStatus, columnPhoneNumber, columnEmail));
        setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        TableViewSelectionModel<CourierPM> tsm = getSelectionModel();

        tsm.setSelectionMode(SelectionMode.SINGLE);
        setItems(couriersPModel.getAllCouriers());
        selectedEntryIndex = tsm.getSelectedIndices();
        selectedEntries = tsm.getSelectedItems();
    }

    @Override
    public void layoutParts() {

    }
    @Override
    public void setupValueChangedListeners() {

        selectedEntryIndex.addListener((ListChangeListener.Change<? extends Integer> change) -> {
            List<? extends Integer> list= change.getList();
            if(list.size() > 0){
                int ix= list.get(0);
                System.out.println("Row selection has changed. Selected row index is "+ix);
                if ((ix == couriersPModel.getCourierCount())) {

                    return; // invalid data
                }
            }

            selectedEntries =getSelectionModel().getSelectedItems();
            CourierPM selectedItem = selectedEntries.get(0);
            String courierId = selectedItem.courierIdProperty().get();

            System.out.println("the selected courier is "+courierId);


            couriersPModel.setCurrentCourierPM(selectedItem);

            System.out.println("the current courier id "+couriersPModel.getCurrentCourierPM().getCourierId());
        });

    }


}
