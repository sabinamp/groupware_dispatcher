package groupware.dispatcher.service.mqtt;

import groupware.dispatcher.service.CourierServiceImpl;
import groupware.dispatcher.service.OrderServiceImpl;
import groupware.dispatcher.service.TaskRequestServiceImpl;
import groupware.dispatcher.service.model.TaskRequest;


public class BrokerConnection {
    public static CourierBrokerClient courierBrokerClient;
    public static OrdersBrokerClient ordersBrokerClient;
    public static TaskBrokerClient taskBrokerClient;


    public BrokerConnection(CourierServiceImpl courierService, OrderServiceImpl orderService, TaskRequestServiceImpl taskService) {
        courierBrokerClient = new CourierBrokerClient(courierService);
        ordersBrokerClient = new OrdersBrokerClient(orderService);
        taskBrokerClient = new TaskBrokerClient(courierService, taskService);
        connectCourierServiceToBroker.run();
        connectOrderServiceToBroker.run();
    }


    Runnable connectCourierServiceToBroker = new Runnable() {
        @Override
        public void run() {
            courierBrokerClient.subscribeToCouriers();
        }
    };


    Runnable connectOrderServiceToBroker =new Runnable() {
        @Override
        public void run() {
           ordersBrokerClient.subscribeToOrders();
        }
    };








}
