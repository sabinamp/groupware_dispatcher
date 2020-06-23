package groupware.dispatcher.service;

import groupware.dispatcher.service.model.OrderDescriptiveInfo;
import groupware.dispatcher.service.util.ModelObjManager;

import java.util.HashMap;
import java.util.Map;

public class OrderService {
    private Map<String, OrderDescriptiveInfo> orders;

    public OrderService(){
        orders= new HashMap<>();
    }

    public void saveOrderInMemory(OrderDescriptiveInfo order){
        orders.put(order.getOrderId(), order);
    }
    public OrderDescriptiveInfo convertJsonToOrderDescriptiveInfo(String json) {
        return ModelObjManager.convertJsonToOrderDescriptiveInfo(json);
    }
}
