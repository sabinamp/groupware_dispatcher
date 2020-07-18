package groupware.dispatcher.view;


import groupware.dispatcher.service.CourierService;
import groupware.dispatcher.service.CourierServiceImpl;
import groupware.dispatcher.service.OrderService;
import groupware.dispatcher.service.model.Courier;
import groupware.dispatcher.service.model.OrderDescriptiveInfo;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import java.util.Map;

public class CourierListView extends ListView<String> implements ViewMixin {
    private Map<String, Courier> courierMap;
    private CourierService courierService;
    public static final ObservableList<Courier> data =
            FXCollections.observableArrayList();

    public CourierListView(){
        init();
    }

    @Override
    public void init() {
        courierService= new CourierServiceImpl();
        courierService.getCouriers();

    }

    @Override
    public void initializeSelf() {

    }

    @Override
    public void initializeParts() {
        ListView<Courier> list = new ListView<>();
        for(String each : courierMap.keySet()){
            System.out.println("The dispatcher has received info about the courier: "+ each);
            data.add(courierMap.get(each));
        }

        list.setItems(data);
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
