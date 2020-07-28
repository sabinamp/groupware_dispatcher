package groupware.dispatcher.view;

import groupware.dispatcher.presentationmodel.AllCouriersPM;
import groupware.dispatcher.presentationmodel.AllOrdersPM;
import groupware.dispatcher.presentationmodel.AllTaskRequestsPM;
import groupware.dispatcher.presentationmodel.RootPM;
import groupware.dispatcher.view.couriers.CouriersPane;
import groupware.dispatcher.view.orders.OrdersPane;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class ApplicationUI extends BorderPane implements ViewMixin {

        public MainHeader header;
        private Footer footer;
        private CouriersPane couriersPane;
        private OrdersPane ordersPane;
        private TaskRequestListView tasksPane;
        private AllOrdersPM allOrdersPM;
        private AllCouriersPM allCouriersPM;
        private AllTaskRequestsPM allTaskRequestsPM;

        private final RootPM rootPM;

        public ApplicationUI(RootPM rootPM,AllOrdersPM allOrdersPM,AllCouriersPM allCouriersPM, AllTaskRequestsPM allTaskRequestsPM ) {
                this.rootPM = rootPM;
                this.allOrdersPM = allOrdersPM;
                this.allCouriersPM = allCouriersPM;
                this.allTaskRequestsPM = allTaskRequestsPM;
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
                header.ordersBtn.setOnAction(event -> setMainContent(ordersPane));
                header.couriersBtn.setOnAction(event ->
                        setMainContent(couriersPane));
                footer = new Footer(rootPM);
                couriersPane = new CouriersPane(allCouriersPM);
                ordersPane = new OrdersPane(allOrdersPM);

                allTaskRequestsPM.setUpFirstTask();
                tasksPane = new TaskRequestListView(allTaskRequestsPM);

        }

        @Override
        public void layoutParts() {

                this.setTop(header);
                BorderPane.setAlignment(header, Pos.CENTER);
                setMainContent(couriersPane);

                this.setBottom(footer);
                this.setLeft(new Text("Left"));
                this.setRight(tasksPane);

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


        public void setMainContent(Node param) {
                this.setCenter(param);
                if(param instanceof CouriersPane){
                        header.couriersBtn.setDisable(true);
                        header.ordersBtn.setDisable(false);
                }else if (param instanceof OrdersPane){
                        header.ordersBtn.setDisable(true);
                        header.couriersBtn.setDisable(false);
                }
        }


}