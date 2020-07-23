package groupware.dispatcher.presentationmodel;

import groupware.dispatcher.service.CourierService;
import groupware.dispatcher.service.CourierServiceImpl;
import groupware.dispatcher.service.model.Courier;
import groupware.dispatcher.service.model.CourierInfo;
import javafx.beans.binding.Bindings;
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

    private ObservableList<CourierPM> allCouriers = FXCollections.observableArrayList();
//    private final ObservableList<String> allCourierIDs = FXCollections.observableArrayList();

    public AllCouriersPM(/*CourierServiceImpl courierService*/){
        setupValueChangedListeners();
        setupBindings();
      /*  if(courierService != null){
            Map<String, CourierInfo> couriersMap = CourierServiceImpl.getCouriers();
            for(String each : couriersMap.keySet()){
                CourierPM currentCourierPM = CourierPM.of(each, couriersMap.get(each));
                if(currentCourierPM != null){
                    allCouriers.add(currentCourierPM);
                }else{
                    System.out.println("AllCouriersPM Constructor called. the currentCourierPM is null");
                }
                System.out.println("AllCouriersPM - the courier: "+ each +" added to the observable list");
            }

            setupValueChangedListeners();
            setupBindings();
        }
      else{
          System.out.println("AllCouriersPM Constructor called but the courier service arg is null.");
        }*/

    }

    private void setupValueChangedListeners() {
        allCouriers.addListener((ListChangeListener.Change<? extends CourierPM> change) -> {
            System.out.println("AllCouriersPM Update "+ change);

            //todo - notification popup
        });

    }

    public ObservableList<CourierPM> getAllCouriers() {

        return allCouriers;
    }


    public void updateAllCouriersPM(CourierPM pm){
        allCouriers.add(pm);

        //allCourierIDs.add(pm.getCourierId());
    }
    private void setupBindings() {
        courierCountProperty().bind(Bindings.size(allCouriers));

    }
    public int getCourierCount() {
        return courierCount.get();
    }

    public IntegerProperty courierCountProperty() {
        return courierCount;
    }

    public void setCourierCount(int courierCount) {
        this.courierCount.set(courierCount);
    }

    public String getPaneTitle() {
        return paneTitle.get();
    }

    public StringProperty paneTitleProperty() {
        return paneTitle;
    }

    public void setPaneTitle(String paneTitle) {
        this.paneTitle.set(paneTitle);
    }

}
