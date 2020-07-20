package groupware.dispatcher.service.mqtt;

import groupware.dispatcher.service.mqtt.CourierBrokerClient;
import groupware.dispatcher.service.mqtt.OrdersBrokerClient;
import javafx.application.Platform;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BrokerConnection {
    private CourierBrokerClient courierBrokerClient;
    private OrdersBrokerClient ordersBrokerClient;

    Thread brokerConnect = new Thread(() -> {
        try {
                // running "long" operation not on UI thread
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

        connectCourierServiceToBroker();
        connectOrderServiceToBroker();

    });

    public BrokerConnection(){
        courierBrokerClient = new CourierBrokerClient();
        ordersBrokerClient = new OrdersBrokerClient();
        connectCourierServiceToBroker();
        connectOrderServiceToBroker();
        System.out.println("BrokerConnection - connected to broker");
    }

    private void connectCourierServiceToBroker(){
        courierBrokerClient.subscribeToCouriers();
        courierBrokerClient.connectToBrokerAndSubscribeToCourierUpdates();
    }

    private void connectOrderServiceToBroker(){
        ordersBrokerClient.connectAndRequestExistingOrder("OR1111");
        ordersBrokerClient.connectAndRequestExistingOrder("OR1122");
        ordersBrokerClient.connectAndRequestExistingOrder("OR1123");
        ordersBrokerClient.connectAndRequestExistingOrder("OR1124");
        ordersBrokerClient.connectToBrokerAndSubscribeToNewOrders();
    }
}
