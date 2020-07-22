package groupware.dispatcher.view;

import groupware.dispatcher.presentationmodel.RootPM;
import groupware.dispatcher.view.util.ViewMixin;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class Footer extends BorderPane implements ViewMixin {

    private final RootPM rootPM;
    private Label title;

    public Footer(RootPM rootPM) {
        this.rootPM = rootPM;
        init();
    }


    @Override
    public void initializeParts() {
        title = new Label();
        title.getStyleClass().add("label");
        getStyleClass().add("footer");
    }

    @Override
    public void layoutParts() {


       this.setLeft(title);
    }

    @Override
    public void setupBindings() {
        title.textProperty().bind(rootPM.applicationTitleProperty());

    }

    public void addExitBtn(Button exitBtn) {
        this.setRight(exitBtn);
    }
}