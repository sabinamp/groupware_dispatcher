package groupware.dispatcher.service;

import groupware.dispatcher.service.model.DeliveryStep;
import groupware.dispatcher.service.model.OrderDescriptiveInfo;
import groupware.dispatcher.service.model.TaskRequest;
import groupware.dispatcher.service.mqtt.orders.OrdersBrokerClient;
import groupware.dispatcher.service.util.ModelObjManager;

import java.util.HashMap;
import java.util.Map;

public class OrderService {
    private Map<String, OrderDescriptiveInfo> orders;
    private OrdersBrokerClient client;

    public OrderService(){
        orders= new HashMap<>();
       /* client= new OrdersBrokerClient();
        connectToBroker();*/
    }

    private void connectToBroker(){
        client.connectAndRequestExistingOrders();
        client.connectToBrokerAndSubscribeToNewOrders();
    }
    public OrderDescriptiveInfo getOrder(String orderId){
        return orders.get(orderId);
    }

    public void saveOrderInMemory(String id, OrderDescriptiveInfo order){
        orders.put(order.getOrderId(), order);
    }

    public OrderDescriptiveInfo convertJsonToOrderDescriptiveInfo(String json) {
        return ModelObjManager.convertJsonToOrderDescriptiveInfo(json);
    }

    public TaskRequest convertJsonToTaskRequest(String taskJson){
        return ModelObjManager.convertJsonToTaskRequest(taskJson);
    }

    public DeliveryStep convertJsonToDeliveryStep(String jsonString){
        return ModelObjManager.convertDeliveryStepData(jsonString);
    }
}
