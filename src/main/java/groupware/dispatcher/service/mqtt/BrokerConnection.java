package groupware.dispatcher.service.mqtt;

import groupware.dispatcher.service.mqtt.CourierBrokerClient;
import groupware.dispatcher.service.mqtt.OrdersBrokerClient;
import javafx.application.Platform;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BrokerConnection {
    private static CourierBrokerClient courierBrokerClient;
    private static OrdersBrokerClient ordersBrokerClient;

   static Thread brokerConnect = new Thread(() -> {
        try {
                // running "long" operation not on UI thread
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

        connectCourierServiceToBroker();
        connectOrderServiceToBroker();

    });
    static {
        courierBrokerClient = new CourierBrokerClient();
        ordersBrokerClient = new OrdersBrokerClient();

    }
    public static void startBrokerConnection(){
        brokerConnect.start();
    }
  /*  public BrokerConnection(){
        System.out.println("BrokerConnection constructor called");

    }*/

    private static void connectCourierServiceToBroker(){
        courierBrokerClient.subscribeToCouriers();
        courierBrokerClient.connectToBrokerAndSubscribeToCourierUpdates();
    }

    private static void connectOrderServiceToBroker(){
        ordersBrokerClient.connectAndRequestExistingOrder("OR1111");
        ordersBrokerClient.connectAndRequestExistingOrder("OR1122");
        ordersBrokerClient.connectAndRequestExistingOrder("OR1123");
        ordersBrokerClient.connectAndRequestExistingOrder("OR1124");
        ordersBrokerClient.connectToBrokerAndSubscribeToNewOrders();
    }
}
