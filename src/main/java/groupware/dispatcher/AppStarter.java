package groupware.dispatcher;

import groupware.dispatcher.service.mqtt.BrokerConnection;
import groupware.dispatcher.view.ApplicationUI;
import groupware.dispatcher.view.MainHeader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class AppStarter extends Application {

    private volatile boolean enough = false;
    private final Text txtTime = new Text();
    // this is timer thread which will update out time view every second
    Thread timer = new Thread(() -> {
        SimpleDateFormat dt = new SimpleDateFormat("hh:mm:ss");
        while(!enough) {
            try {
                // running "long" operation not on UI thread
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            final String time = dt.format(new Date());
            Platform.runLater(()-> {
                // updating live UI object requires JavaFX App Thread
                txtTime.setText(time);
            });
        }
    });
    @Override
    public void start(Stage primaryStage){
        ApplicationUI rootPanel = new ApplicationUI();
        rootPanel.addClockToHeader(txtTime);
        Scene scene = new Scene(rootPanel, 800,500);

        primaryStage.setTitle("Dispatcher GUI");
        BrokerConnection connection= new BrokerConnection();
        primaryStage.setScene(scene);
        timer.start();
        primaryStage.show();

    }
    @Override
    public void stop() {
        // we need to stop our working thread after closing a window
        // or our program will not exit
        enough = true;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
