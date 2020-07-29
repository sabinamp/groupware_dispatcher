package groupware.dispatcher.view;

import groupware.dispatcher.presentationmodel.AllCouriersPM;
import groupware.dispatcher.presentationmodel.AllOrdersPM;
import groupware.dispatcher.presentationmodel.AllTaskRequestsPM;
import groupware.dispatcher.presentationmodel.RootPM;
import groupware.dispatcher.view.couriers.CouriersPane;
import groupware.dispatcher.view.orders.OrdersPane;
import groupware.dispatcher.view.tasks.TaskRequestsTable;
import groupware.dispatcher.view.tasks.TasksPane;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class ApplicationUI extends BorderPane implements ViewMixin {

        public MainHeader header;
        private Footer footer;
        private CouriersPane couriersPane;
        private OrdersPane ordersPane;
        private TasksPane tasksPane;
        private AllOrdersPM allOrdersPM;
        private AllCouriersPM allCouriersPM;


        private final RootPM rootPM;

        public ApplicationUI(RootPM rootPM,AllOrdersPM allOrdersPM,AllCouriersPM allCouriersPM) {
                this.rootPM = rootPM;
                this.allOrdersPM = allOrdersPM;
                this.allCouriersPM = allCouriersPM;
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
                footer = new Footer(rootPM);
                couriersPane = new CouriersPane(allCouriersPM);
                ordersPane = new OrdersPane(allOrdersPM);
                tasksPane = new TasksPane(allCouriersPM, allOrdersPM);

                header.ordersBtn.setOnAction(event -> setMainContent(ordersPane));
                header.couriersBtn.setOnAction(event ->
                        setMainContent(couriersPane));
                header.tasksBtn.setOnAction(e-> setMainContent(tasksPane));
        }

        @Override
        public void layoutParts() {

                this.setTop(header);
                BorderPane.setAlignment(header, Pos.CENTER);
                setMainContent(couriersPane);
                //todo the chat pane
                this.setRight(new TextArea("Chat"));
                this.setBottom(footer);
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
                        header.tasksBtn.setDisable(false);
                }else if (param instanceof OrdersPane){
                        header.ordersBtn.setDisable(true);
                        header.couriersBtn.setDisable(false);
                        header.tasksBtn.setDisable(false);
                }else if (param instanceof TasksPane){
                        header.tasksBtn.setDisable(true);
                        header.ordersBtn.setDisable(false);
                        header.couriersBtn.setDisable(false);
                }
        }


}