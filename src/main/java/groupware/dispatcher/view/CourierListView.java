package groupware.dispatcher.view;


import groupware.dispatcher.presentationmodel.AllCouriersPM;
import groupware.dispatcher.presentationmodel.CourierPM;
import groupware.dispatcher.service.CourierService;
import groupware.dispatcher.service.CourierServiceImpl;
import groupware.dispatcher.service.OrderService;
import groupware.dispatcher.service.model.Courier;
import groupware.dispatcher.service.model.OrderDescriptiveInfo;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;

import java.util.Map;

public class CourierListView extends StackPane implements ViewMixin {

    public static ObservableList<CourierPM> items =
            FXCollections.observableArrayList();

    private  ListView<CourierPM> list;

    public CourierListView(AllCouriersPM data){
        items = data.getAllCouriers();
        init();
    }

    @Override
    public void init() {

        list = new ListView<>();
        list.setItems(items);

    }

    @Override
    public void initializeSelf() {

    }

    @Override
    public void initializeParts() {

     this.getChildren().add(list);

    }

    @Override
    public void layoutParts() {

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
