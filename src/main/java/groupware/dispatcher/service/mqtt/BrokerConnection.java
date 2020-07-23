package groupware.dispatcher.service.mqtt;

import groupware.dispatcher.service.CourierService;
import groupware.dispatcher.service.CourierServiceImpl;
import groupware.dispatcher.service.OrderServiceImpl;
import groupware.dispatcher.service.mqtt.CourierBrokerClient;
import groupware.dispatcher.service.mqtt.OrdersBrokerClient;
import javafx.application.Platform;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BrokerConnection {
    private CourierBrokerClient courierBrokerClient;
    private OrdersBrokerClient ordersBrokerClient;




    public BrokerConnection(CourierServiceImpl courierService, OrderServiceImpl orderService) {
        courierBrokerClient = new CourierBrokerClient(courierService);
        ordersBrokerClient = new OrdersBrokerClient(orderService);
        connectCourierServiceToBroker();
        connectOrderServiceToBroker();
    }

/*    public void startBrokerConnection(){
        connectToBroker.run();
    }*/


    private void connectCourierServiceToBroker(){
        subscribeToCouriers();
        courierBrokerClient.connectToBrokerAndSubscribeToCourierUpdates();
    }

    private void connectOrderServiceToBroker(){
        ordersBrokerClient.connectAndRequestExistingOrder("OR1111");
        ordersBrokerClient.connectAndRequestExistingOrder("OR1122");
        ordersBrokerClient.connectAndRequestExistingOrder("OR1123");
        ordersBrokerClient.connectAndRequestExistingOrder("OR1124");

        ordersBrokerClient.connectAndSubscribeForExistingOrder();
        ordersBrokerClient.connectToBrokerAndSubscribeToNewOrders();
    }

    private void subscribeToCouriers(){

        courierBrokerClient.connectAndRequestCourier("C100");
        courierBrokerClient.connectAndRequestCourier("C101");
        courierBrokerClient.connectAndRequestCourier("C102");
        courierBrokerClient.connectAndRequestCourier("C103");
        courierBrokerClient.connectAndRequestCourier("C104");
        courierBrokerClient.connectAndRequestCourier("C105");
        courierBrokerClient.connectAndRequestCourier("C106");
        courierBrokerClient.connectAndRequestCourier("C107");
        courierBrokerClient.connectAndSubscribeForCourierInfoResponse();

    }
}
