package groupware.dispatcher;

import groupware.dispatcher.presentationmodel.AllCouriersPM;
import groupware.dispatcher.presentationmodel.AllOrdersPM;
import groupware.dispatcher.presentationmodel.AllTaskRequestsPM;
import groupware.dispatcher.presentationmodel.RootPM;
import groupware.dispatcher.service.CourierServiceImpl;
import groupware.dispatcher.service.OrderServiceImpl;
import groupware.dispatcher.service.TaskRequestServiceImpl;
import groupware.dispatcher.service.mqtt.BrokerConnection;
import groupware.dispatcher.view.ApplicationUI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppStarter extends Application {
    private ApplicationUI rootPanel;
    private RootPM rootPM;

    private AllOrdersPM allOrdersPM;
    private AllCouriersPM allCouriersPM;
    private volatile boolean enough = false;
    private final Text txtTime = new Text();
    private final CourierServiceImpl courierService =  new CourierServiceImpl();
    private final  OrderServiceImpl orderService = new OrderServiceImpl();
    private final TaskRequestServiceImpl taskRequestService = new TaskRequestServiceImpl(orderService,courierService);
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
    Runnable connectToBroker = new Runnable() {
        @Override
        public void run() {
            BrokerConnection brokerConnection = new BrokerConnection(courierService, orderService, taskRequestService);

            Platform.runLater(()-> {
                // updating live UI object requires JavaFX App Thread

            });
        }
    };

    @Override
    public void start(Stage primaryStage){
        rootPM = new RootPM();

        connectToBroker.run();
        allCouriersPM = courierService.getAllCouriersPM();
        allOrdersPM = orderService.getAllOrdersPM();
        Button exitBtn = new Button("Exit");
        exitBtn.setTextFill(Color.rgb(50,50,100));
        exitBtn.setOnAction(e -> {
            stop();
            System.exit(0);

        });
        AllTaskRequestsPM allTaskRequestsPM = new AllTaskRequestsPM(allOrdersPM, allCouriersPM);
        allTaskRequestsPM.setListener(taskRequestService.taskEventListener);
        rootPanel = new ApplicationUI(rootPM, allOrdersPM, allCouriersPM);

        rootPanel.addClockToHeader(txtTime);

        rootPanel.addExitButton(exitBtn);

        Scene scene = new Scene(rootPanel, 1000,800);
        primaryStage.setTitle("Dispatcher GUI");
        primaryStage.setScene(scene);
        timer.start();
        scene.getWindow().setOnCloseRequest(new EventHandler<>() {
            @Override
            public void handle(WindowEvent event) {
                stop();
                System.exit(0);
            }
        });
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
