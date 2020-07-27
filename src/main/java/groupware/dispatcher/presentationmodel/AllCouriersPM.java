package groupware.dispatcher.presentationmodel;


import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.scene.control.Alert;


public class AllCouriersPM {

    private final StringProperty paneTitle = new SimpleStringProperty("Couriers");

    private final IntegerProperty courierCount = new SimpleIntegerProperty();

    private ObservableList<CourierPM> allCouriers = FXCollections.observableArrayList();
    private ObservableList<CourierPM> syncAllCouriers = FXCollections.synchronizedObservableList(allCouriers);

    private ObjectProperty<CourierPM> currentCourierPM =  new SimpleObjectProperty<>();

    public AllCouriersPM(){
        setupValueChangedListeners();
        setupBindings();

    }

    private void setupValueChangedListeners() {
        allCouriers.addListener((ListChangeListener.Change<? extends CourierPM> change) -> {
            System.out.println("AllCouriersPM Update "+ change);
            change.next();
            boolean wasUpdated = change.wasUpdated();
            //todo - notification popup
            Platform.runLater(()->{

                    if(wasUpdated )
                    showAlertWithDefaultHeaderText(wasUpdated, change.toString());
            });
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


    public CourierPM getCurrentCourierPM() {
        return currentCourierPM.get();
    }

    public ObjectProperty<CourierPM> currentCourierPMProperty() {
        return currentCourierPM;
    }

    public void setCurrentCourierPM(CourierPM currentCourierPM) {
        this.currentCourierPM.set(currentCourierPM);
    }

    // Show a Information Alert with default header Text
    private void showAlertWithDefaultHeaderText(boolean wasUpdated, String change) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Courier Update");

         alert.setHeaderText("Courier Update");

        StringBuilder content= new StringBuilder("Courier Notification");
        if(wasUpdated){
            content.append("Courier Update.").append(change);
        }
        alert.setContentText(content.toString());

        alert.showAndWait();
    }
}
