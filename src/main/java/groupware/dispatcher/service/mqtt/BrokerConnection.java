package groupware.dispatcher.service.mqtt;

import groupware.dispatcher.service.mqtt.CourierBrokerClient;
import groupware.dispatcher.service.mqtt.OrdersBrokerClient;

public class BrokerConnection {
    private CourierBrokerClient courierBrokerClient;
    private OrdersBrokerClient ordersBrokerClient;

    public BrokerConnection(){
        courierBrokerClient = new CourierBrokerClient();
        courierBrokerClient.subscribeToCouriers();
        courierBrokerClient.connectToBrokerAndSubscribeToCourierUpdates();
        ordersBrokerClient = new OrdersBrokerClient();
        connectOrderServiceToBroker();
        System.out.println("BrokerConnection - connected to broker");
    }

    private void connectOrderServiceToBroker(){
        ordersBrokerClient.connectAndRequestExistingOrder("OR1111");
        ordersBrokerClient.connectAndRequestExistingOrder("OR1122");
        ordersBrokerClient.connectAndRequestExistingOrder("OR1123");
        ordersBrokerClient.connectAndRequestExistingOrder("OR1124");
        ordersBrokerClient.connectToBrokerAndSubscribeToNewOrders();
    }
}
