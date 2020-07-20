package groupware.dispatcher.view;


import groupware.dispatcher.presentationmodel.AllCouriersPM;
import groupware.dispatcher.presentationmodel.CourierPM;
import groupware.dispatcher.service.CourierService;
import groupware.dispatcher.service.CourierServiceImpl;
import groupware.dispatcher.service.OrderService;
import groupware.dispatcher.service.model.Courier;
import groupware.dispatcher.service.model.OrderDescriptiveInfo;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CourierListView extends StackPane implements ViewMixin {

    public static ObservableList<CourierPM> items =
            FXCollections.observableArrayList( );

    private  ListView<CourierPM> listView;

    public CourierListView(AllCouriersPM data){

        System.out.println("AllCouriersPM getAllCouriers() method called");
      //  items.addAll( data.getAllCouriers());
        items.addAll(data.getAllCouriers());
        init();
        /*"Red", "Blue", "Yellow", "Green")*/
    }



    @Override
    public void initializeSelf() {
        addStylesheetFiles("/fonts/fonts.css", "style.css");
        getStyleClass().add("couriers-pane");

    }

    @Override
    public void initializeParts() {
        listView = new ListView<CourierPM>(items);
        listView.setCellFactory((ListView<CourierPM> param) -> {
            return new ListCell<>() {
                @Override
                public void updateItem(CourierPM item, boolean empty) {
                    super.updateItem(item, empty);
                    if (! (empty || item == null)) {
                        // adding new item
                        setGraphic(new Rectangle(30, 30, Color.web("Blue")));
                        setText("Courier ID: "+item.toString());
                    } else {
                        setText(null);
                        setGraphic(null);
                    }
                }
            };
        });


    }

    @Override
    public void layoutParts() {
        this.getChildren().add(listView);
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
