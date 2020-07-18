package groupware.dispatcher;

import groupware.dispatcher.service.mqtt.BrokerConnection;
import groupware.dispatcher.view.ApplicationUI;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppStarter extends Application {

    @Override
    public void start(Stage primaryStage){
        Parent   rootPanel = new ApplicationUI();

        Scene scene = new Scene(rootPanel);

        primaryStage.setTitle("Dispatcher GUI");
        BrokerConnection connection= new BrokerConnection();
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
