package groupware.dispatcher.presentationmodel;


import groupware.dispatcher.service.CourierEventListener;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.scene.control.Alert;


public class AllCouriersPM implements CourierEventListener {

    private final StringProperty paneTitle = new SimpleStringProperty("Couriers");
    private final IntegerProperty courierCount = new SimpleIntegerProperty();
    private final ObservableList<CourierPM> allCouriers = FXCollections.observableArrayList();
    private ObservableList<CourierPM> syncAllCouriers = FXCollections.synchronizedObservableList(allCouriers);

    private ObjectProperty<CourierPM> currentCourierPM =  new SimpleObjectProperty<>();



    private final ObjectProperty<ObservableList<CourierPM>> allCourierEntries = new SimpleObjectProperty<>();

    public AllCouriersPM(){
        setupValueChangedListeners();
        setupBindings();
        setAllCourierEntries(syncAllCouriers);
    }

    private void setupValueChangedListeners() {
        syncAllCouriers.addListener((ListChangeListener.Change<? extends CourierPM> change) -> {
            System.out.println("AllCouriersPM Update "+ change);
            change.next();
            boolean wasUpdated = change.wasUpdated();
            //todo - notification popup
            Platform.runLater(()->{

                    if(wasUpdated ){
                        showAlertWithDefaultHeaderText(change.toString());
                    }

            });
        });

    }

    public ObservableList<CourierPM> getAllCouriers() {
        return syncAllCouriers;
    }

    public void updateItemInAllCouriersPM(CourierPM pm){
        for(int i=0; i < syncAllCouriers.size(); i++){
            if(syncAllCouriers.get(i).equals(pm)){
                syncAllCouriers.set(i, pm);
            }
        }
    }

    public void updateAllCouriersPM(CourierPM pm){

        syncAllCouriers.add(pm);
    }



    private void removeCourierToBeReplaced(CourierPM c){
        syncAllCouriers.remove(c);
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
    private void showAlertWithDefaultHeaderText( String change) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Courier Update");
         alert.setHeaderText("Courier Update");

        String content= "Courier Update "+change;

        alert.setContentText(content);

        alert.showAndWait();
    }

    public ObservableList<CourierPM> getAllCourierEntries() {
        return allCourierEntries.get();
    }

    public ObjectProperty<ObservableList<CourierPM>> allCourierEntriesProperty() {
        return allCourierEntries;
    }

    public void setAllCourierEntries(ObservableList<CourierPM> allCourierEntries) {
        this.allCourierEntries.set(allCourierEntries);
    }

    @Override
    public void handleNewCourierEvent(CourierPM courierInfo) {
        updateAllCouriersPM(courierInfo);
    }

    @Override
    public void handleCourierUpdateEvent(CourierPM courierInfo) {
        updateItemInAllCouriersPM(courierInfo);
    }
}
