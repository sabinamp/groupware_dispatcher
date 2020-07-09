package groupware.dispatcher.service;

import groupware.dispatcher.service.model.Courier;
import groupware.dispatcher.service.model.DeliveryStep;
import groupware.dispatcher.service.model.OrderDescriptiveInfo;
import groupware.dispatcher.service.model.TaskRequest;
import groupware.dispatcher.service.mqtt.CourierBrokerClient;
import groupware.dispatcher.service.util.ModelObjManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourierService {

    private Map<String, Courier> couriers;


    public CourierService(){
        couriers = new HashMap<>();
    }




    public Courier getCourier(String id){
        return couriers.get(id);
    }

    public void saveCourierInMemory(String id, Courier courier){
        couriers.put(courier.getCourierId(), courier);
    }

    public Courier convertJsonToCourier(String json) {
        return ModelObjManager.convertJsonToCourier(json);
    }

    TaskRequest convertJsonToTaskRequest(String taskJson){
        return ModelObjManager.convertJsonToTaskRequest(taskJson);
    }

    public DeliveryStep convertJsonToDeliveryStep(String jsonString){
        return ModelObjManager.convertDeliveryStepData(jsonString);
    }

    public Map<String, Courier> getCouriers() {
        return couriers;
    }

}
