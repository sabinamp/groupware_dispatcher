package groupware.dispatcher.service.mqtt;

import groupware.dispatcher.service.CourierServiceImpl;
import groupware.dispatcher.service.OrderServiceImpl;
import groupware.dispatcher.service.TaskRequestServiceImpl;


public class BrokerConnection {
    private CourierBrokerClient courierBrokerClient;
    private OrdersBrokerClient ordersBrokerClient;
    private TaskRequestServiceImpl taskRequestService;


    public BrokerConnection(CourierServiceImpl courierService, OrderServiceImpl orderService, TaskRequestServiceImpl taskService) {
        courierBrokerClient = new CourierBrokerClient(courierService, taskService);
        ordersBrokerClient = new OrdersBrokerClient(orderService);

        connectCourierServiceToBroker();
        connectOrderServiceToBroker();
    }


    private void connectCourierServiceToBroker(){
        subscribeToCouriers();
        courierBrokerClient.connectToBrokerAndSubscribeToCourierUpdates();
    }

    private void connectOrderServiceToBroker(){

        subscribeToOrders();
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

    private void subscribeToOrders(){
        ordersBrokerClient.connectAndRequestExistingOrder("OR1111");
        ordersBrokerClient.connectAndRequestExistingOrder("OR1122");
        ordersBrokerClient.connectAndRequestExistingOrder("OR1123");
        ordersBrokerClient.connectAndRequestExistingOrder("OR1124");
        ordersBrokerClient.connectAndSubscribeForExistingOrders();
    }
}
