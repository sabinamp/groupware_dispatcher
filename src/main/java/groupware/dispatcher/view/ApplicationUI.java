package groupware.dispatcher.view;

import groupware.dispatcher.presentationmodel.AllCouriersPM;
import groupware.dispatcher.presentationmodel.RootPM;
import groupware.dispatcher.view.couriers.CouriersPane;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class ApplicationUI extends BorderPane implements ViewMixin {

        public MainHeader header;
        private Footer footer;
        private CouriersPane couriersPane;


        private final RootPM rootPM;

        public ApplicationUI(RootPM rootPM) {
                this.rootPM = rootPM;

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
                couriersPane = new CouriersPane(rootPM);

        }

        @Override
        public void layoutParts() {

                this.setRight(new Text("Right "));
                this.setCenter(couriersPane);

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