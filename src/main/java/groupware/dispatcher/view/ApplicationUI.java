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




        }

        @Override
        public void layoutParts() {
        header = new MainHeader();
        HBox hb1= new HBox();
        hb1.setSpacing(10);

        hb1.setPrefSize(900,700);
        VBox vboxOnTheRight= new VBox();
        vboxOnTheRight.getChildren().addAll(new HBox(), new VBox());
        hb1.getChildren().addAll(new VBox(),vboxOnTheRight);
        getChildren().addAll(header, hb1);
        setVgrow(hb1, Priority.ALWAYS);
        }

        @Override
        public void setupBindings() {

        }
}