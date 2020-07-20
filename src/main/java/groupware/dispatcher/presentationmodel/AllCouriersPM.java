package groupware.dispatcher.presentationmodel;

import groupware.dispatcher.service.CourierService;
import groupware.dispatcher.service.model.Courier;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.Map;

public class AllCouriersPM {
    private final StringProperty paneTitle = new SimpleStringProperty("Couriers");
    private final IntegerProperty courierCount = new SimpleIntegerProperty();
    private final CourierService courierService;
    private final ObservableList<CourierPM> allCouriers = FXCollections.observableArrayList();

    public AllCouriersPM(CourierService courierService){
        this.courierService = courierService;
        Map<String, Courier> couriersMap = courierService.getCouriers();
        for(String each : couriersMap.keySet()){
            System.out.println("AllCouriersPM - info about the courier: "+ each);
            allCouriers.add(new CourierPM(couriersMap.get(each)));
        }
        setupValueChangedListeners();
    }

    private void setupValueChangedListeners() {
        allCouriers.addListener((ListChangeListener.Change<? extends CourierPM> change) -> {
            // this will write something like
            System.out.println("Updated received"+ change);
            allCouriers.stream().forEach(System.out::println);
            //todo - notification popup
        });
    }

    public ObservableList<CourierPM> getAllCouriers() {
        return allCouriers;
    }
}
