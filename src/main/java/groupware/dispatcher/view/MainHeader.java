package groupware.dispatcher.view;

import groupware.dispatcher.view.util.ViewMixin;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;


public class MainHeader extends HBox implements ViewMixin {

    HBox topBanner;
    TilePane tilePane;
    HBox timerBox;

    public MainHeader(){

        init();
    }

    @Override
    public void initializeParts() {
        topBanner = new HBox();
        topBanner.setSpacing(4);
        topBanner.setPadding(new Insets(2,5,5,5));
        tilePane = new TilePane();
        tilePane.setPrefColumns(2);
        tilePane.setPrefRows(1);

        Text title= new Text("City Courier Services");
        // gradient fill, see details few sections below
        Stop[] stops = new Stop[]{new Stop(0, Color.DARKSLATEGRAY), new Stop(1, Color.DARKGRAY), new Stop(0.5, Color.ALICEBLUE)};
        LinearGradient gradient = new LinearGradient(50, 50, 250, 50, false, CycleMethod.NO_CYCLE, stops);
        title.setFill(gradient);

        timerBox = new HBox();
        timerBox.setSpacing(8);

        tilePane.getChildren().add(timerBox);
        topBanner.getChildren().addAll(title, tilePane);

    }

    @Override
    public void initializeSelf() {
        getStyleClass().add("main-header");
    }

    @Override
    public void layoutParts(){
        getChildren().addAll(topBanner);
    }

    public void addClockDemoToHeader(Text txtTimer){
        txtTimer.setFill(Color.MIDNIGHTBLUE);
        timerBox.getChildren().add(txtTimer);
    }


    public void addExitBtn(Button exitBtn) {
        topBanner.getChildren().add(exitBtn);
    }
}
