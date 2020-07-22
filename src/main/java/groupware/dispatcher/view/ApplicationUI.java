package groupware.dispatcher.view;

import groupware.dispatcher.presentationmodel.AllCouriersPM;
import groupware.dispatcher.presentationmodel.AllOrdersPM;
import groupware.dispatcher.presentationmodel.RootPM;
import groupware.dispatcher.service.CourierService;
import groupware.dispatcher.service.CourierServiceImpl;
import groupware.dispatcher.service.OrderService;
import groupware.dispatcher.service.mqtt.BrokerConnection;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class ApplicationUI extends BorderPane implements ViewMixin {

        public MainHeader header;
        private Footer footer;
        private StackPane couriersPane;
        private AllOrdersPM allOrdersPM;
        private AllCouriersPM allCouriersPM;
        private final RootPM rootPM;

        public ApplicationUI(RootPM rootPM, CourierService courierService, OrderService orderService) {
                this.rootPM = rootPM;
                allCouriersPM = new AllCouriersPM(courierService);
                allOrdersPM = new AllOrdersPM(orderService);
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
                footer = new Footer(rootPM);
        }

        @Override
        public void layoutParts() {

                this.setRight(new Text("Right "));
                this.setCenter(couriersPane);

                //to be added: the orders view and the detailed order view
                //to be added: the couriers view and the detailed courier view
                this.setBottom(footer);
                this.setLeft(new Text("Left"));
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
                footer.addExitBtn(exitBtn);
        }
}