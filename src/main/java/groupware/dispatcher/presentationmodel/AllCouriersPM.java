package groupware.dispatcher.presentationmodel;

import groupware.dispatcher.service.CourierService;
import groupware.dispatcher.service.model.Courier;
import groupware.dispatcher.service.model.CourierInfo;
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
    private CourierService courierService;
    private final ObservableList<CourierPM> allCouriers = FXCollections.observableArrayList();
    private final ObservableList<String> allCourierIDs = FXCollections.observableArrayList();

    public AllCouriersPM(CourierService courierService){
        this.courierService = courierService;
        Map<String, CourierInfo> couriersMap = courierService.getCouriers();
        for(String each : couriersMap.keySet()){
           allCouriers.add(new CourierPM(each, couriersMap.get(each)));

           allCourierIDs.add(each);
           System.out.println("AllCouriersPM - the courier: "+ each +" added to the observable list");
        }
        setupValueChangedListeners();
    }

    private void setupValueChangedListeners() {
        allCouriers.addListener((ListChangeListener.Change<? extends CourierPM> change) -> {
            System.out.println("Updated received"+ change);
            allCouriers.stream().forEach(System.out::println);
            //todo - notification popup
        });
    }

    public ObservableList<CourierPM> getAllCouriers() {
        //logger.log(Level.INFO, "AllCouriersPM getAllCouriers() method called");
        return allCouriers;
    }
    public ObservableList<String> getAllCourierIDs() {
        //logger.log(Level.INFO, "AllCouriersPM getAllCourierIDs() method called");
        return allCourierIDs;
    }

    public void updateAllCouriersPM(CourierPM pm){
        allCouriers.add(pm);
        allCourierIDs.add(pm.getCourierId());
    }

}
