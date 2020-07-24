package groupware.dispatcher.presentationmodel;


import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.*;



public class AllCouriersPM {

    private final StringProperty paneTitle = new SimpleStringProperty("Couriers");

    private final IntegerProperty courierCount = new SimpleIntegerProperty();

    private ObservableList<CourierPM> allCouriers = FXCollections.observableArrayList();
    private ObservableList<CourierPM> syncAllCouriers = FXCollections.synchronizedObservableList(allCouriers);


    public AllCouriersPM(){
        setupValueChangedListeners();
        setupBindings();

    }

    private void setupValueChangedListeners() {
        allCouriers.addListener((ListChangeListener.Change<? extends CourierPM> change) -> {
            System.out.println("AllCouriersPM Update "+ change);

            //todo - notification popup
        });

    }

    public ObservableList<CourierPM> getAllCouriers() {
        return syncAllCouriers;
    }


    public void updateAllCouriersPM(CourierPM pm){
        syncAllCouriers.add(pm);
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
