package groupware.dispatcher.view;

import groupware.dispatcher.presentationmodel.AllCouriersPM;
import groupware.dispatcher.service.CourierService;
import groupware.dispatcher.service.CourierServiceImpl;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class ApplicationUI extends BorderPane implements ViewMixin {

        public MainHeader header;
        private StackPane couriersPane;
        private AllCouriersPM allCouriersPM;

        public ApplicationUI(CourierService courierService) {
                allCouriersPM = new AllCouriersPM(courierService);
                init();
        }

        @Override
        public void initializeSelf() {
                addStylesheetFiles("/fonts/fonts.css", "style.css");
                getStyleClass().add("root_pane");
                }

        @Override
        public void initializeParts() {
                header = new MainHeader();

                couriersPane = new CourierListView(allCouriersPM);

        }

        @Override
        public void layoutParts() {

                this.setRight(new Text("Right "));
                this.setCenter(new Text("Center"));
                this.setBottom(new Text(" Courier Service Dispatcher"));
                this.setLeft(new Text(" Left"));

                //to be added: the orders view and the detailed order view
                this.setTop(header);
                BorderPane.setAlignment(header, Pos.CENTER);

        }

        @Override
        public void setupBindings() {

        }

        public void addClockToHeader(Text txtTimer){
                header.addClockDemoToHeader(txtTimer);
        }

        public void addExitButton(Button exitBtn) {
                header.addExitBtn(exitBtn);
        }
}