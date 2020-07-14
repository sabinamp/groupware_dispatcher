package groupware.dispatcher.view;

import groupware.dispatcher.view.util.ViewMixin;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
public class ApplicationUI extends VBox implements ViewMixin {

        private Node header;

        public ApplicationUI() {
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
        }

        @Override
        public void layoutParts() {

        HBox hb1= new HBox();
        hb1.setSpacing(10);

        VBox vbLeft= new VBox();
        vbLeft.setSpacing(10);
        vbLeft.getChildren().add(new OrderListView());

        vbLeft.setPrefSize(550,700);
        VBox vboxOnTheRight= new VBox();
        vboxOnTheRight.setPrefSize(550,700);

        hb1.getChildren().addAll(vbLeft,vboxOnTheRight);
        getChildren().addAll(header, hb1);
        setVgrow(hb1, Priority.ALWAYS);
        }

        @Override
        public void setupBindings() {

        }
}