package groupware.dispatcher.view;

import groupware.dispatcher.view.util.ViewMixin;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class MainHeader extends VBox implements ViewMixin {

    private HBox topBanner;

    public MainHeader(){

        init();
    }

    @Override
    public void initializeParts() {
        topBanner = new HBox();
        topBanner.setSpacing(4);
        topBanner.setPadding(new Insets(5,5,5,5));

        Text title= new Text("City Courier Services");
        topBanner.getChildren().addAll(title);


    }
    @Override
    public void initializeSelf() {
        getStyleClass().add("main-header");
    }

    @Override
    public void layoutParts() {
        getChildren().addAll(topBanner);
    }
}
