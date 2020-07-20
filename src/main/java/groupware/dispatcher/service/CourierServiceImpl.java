package groupware.dispatcher.service;

import groupware.dispatcher.service.model.*;
import groupware.dispatcher.service.util.ModelObjManager;


import java.util.*;

public class CourierServiceImpl implements CourierService {
    private Map<String, Courier> couriers;

    public CourierServiceImpl(){
        couriers = new HashMap<>();
    }

    public Courier getCourier(String id){
        return couriers.get(id);
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

    @Override
    public Map<String, Courier> getCouriers() {
        return couriers;
    }

    @Override
    public boolean setStatus(String courierId, CourierStatus status) {
        boolean successful = false;
        Courier courier = getCourier(courierId);
        if(status != null && courier != null){
            CourierInfo courierInfo= courier.getCourierInfo();
            courierInfo.setStatus(status);
            successful = updateCourierInfo(courierId, courierInfo);
            if(successful){
                System.out.println("setStatus()- Successfully updated the courier's status "+status.toString());
            }
        }
        return successful;
    }

    @Override
    public boolean updateCourier(String courierId, Courier courier) {
        if(courier == null ){
            return false;
        }else{
            couriers.put(courierId, courier);
            return true;
        }
    }
    @Override
    public boolean setConn(String courierId, Conn conn) {
        boolean successful = false;
        Courier courier = getCourier(courierId);
        if(courier != null && conn != null){
            CourierInfo courierInfo= courier.getCourierInfo();
            courierInfo.setConn(conn);
            successful = updateCourierInfo(courierId, courierInfo);
            if(successful){
                    System.out.println("Successfully updated the courier's conn : " + conn.toString());
            }

        }

        return successful;
    }
    @Override
    public boolean updateAssignedOrders(String courierId, String addedOrderId) {
        Courier courier = getCourier(courierId);
        CourierInfo courierInfo = courier.getCourierInfo();
        courierInfo.addAssignedOrder(addedOrderId);

        boolean successful = updateCourierInfo(courierId, courierInfo);
        System.out.println("Successfully updated the courier's assigned orders : " + successful);
        return successful;
    }
    @Override
    public boolean updateCourierInfo(String courierId, CourierInfo courierInfo) {
        Courier courier = getCourier(courierId);
        courier.setCourierInfo(courierInfo);
        boolean successful = updateCourier(courierId, courier);
        System.out.println("Successfully updated the courier's info: " + successful);
        return successful;
    }
    @Override
    public Conn getConn(String courierId) {
        Courier courier= getCourier(courierId);
        return courier.getCourierInfo().getConn();
    }

    @Override
    public CourierStatus getStatus(String courierId) {
        Courier courier = getCourier(courierId);
        return courier.getCourierInfo().getStatus();
    }
    @Override
    public CourierInfo getCourierInfo(String courierId) {
        Courier courier= getCourier(courierId);
        return courier.getCourierInfo();
    }

    @Override
    public List<ContactInfo> getContactInfos(String courierId) {
        Courier courier= getCourier(courierId);
        return courier.getCourierInfo().getContactInfos();
    }

    @Override
    public Set<String> getAssignedOrders(String courierId) {
        Courier courier= getCourier(courierId);
        return courier.getCourierInfo().getAssignedOrders();
    }

}
