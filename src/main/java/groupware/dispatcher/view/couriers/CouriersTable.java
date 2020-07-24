package groupware.dispatcher.view.couriers;

import groupware.dispatcher.presentationmodel.AllCouriersPM;
import groupware.dispatcher.presentationmodel.CourierPM;
import groupware.dispatcher.presentationmodel.RootPM;
import groupware.dispatcher.service.model.ContactInfo;
import groupware.dispatcher.service.model.CourierStatus;
import groupware.dispatcher.service.model.Phone;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Arrays;


public class CouriersTable extends TableView<CourierPM> implements ViewMixin {

    private AllCouriersPM couriersPModel;

    public CouriersTable(AllCouriersPM allCouriersPM){
        super();
        this.couriersPModel = allCouriersPM;
        init();
    }

    @Override
    public void initializeSelf() {
        this.setPrefWidth(750);

    }

    @Override
    public void initializeParts() {

        TableColumn<CourierPM, String> columnId= new TableColumn<>("Courier ID");
        columnId.setCellValueFactory(cell->cell.getValue().courierIdProperty());
        columnId.setCellFactory(cell -> new IDCell());
        columnId.setMinWidth(150);

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
        columnPhoneNumber.setMinWidth(200);

        getColumns().addAll(Arrays.asList(columnId, columnName, columnStatus, columnPhoneNumber));
        setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        TableViewSelectionModel<CourierPM> tsm = getSelectionModel();
        tsm.setSelectionMode(SelectionMode.SINGLE);
        setItems(couriersPModel.getAllCouriers());
    }

    @Override
    public void layoutParts() {

    }

    public void setCouriersPModel(AllCouriersPM pm){
        this.couriersPModel = pm;
    }


}
