package groupware.dispatcher.service;

import groupware.dispatcher.presentationmodel.AllCouriersPM;
import groupware.dispatcher.presentationmodel.CourierPM;
import groupware.dispatcher.service.model.*;
import groupware.dispatcher.service.mqtt.BrokerConnection;
import groupware.dispatcher.service.util.ModelObjManager;


import java.util.*;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class CourierServiceImpl implements CourierService {
    private static Map<String, CourierInfo> couriers;
    private final static Logger logger;
    private AllCouriersPM allCouriersPM;

    static{
        logger = LogManager.getLogManager().getLogger(String.valueOf(OrderServiceImpl.class));
        couriers = new HashMap<>();

    }
    public CourierServiceImpl(){
        this.allCouriersPM = new AllCouriersPM();
    }

    public static Map<String, CourierInfo> getCouriers() {
        return couriers;
    }


    @Override
    public boolean setStatus(String courierId, CourierStatus status) {
        boolean successful = false;
        CourierInfo courierInfo = getCourierInfo(courierId);
        if(status != null && courierInfo != null){
            courierInfo.setStatus(status);
            successful = saveCourier(courierId, courierInfo);
            if(successful){
                System.out.println("setStatus()- Successfully updated the courier's status "+status.toString());
            }
        }
        return successful;
    }

    @Override
    public boolean saveCourier(String courierId, CourierInfo courier) {
        if(courier == null ){
            return false;
        }else{
            couriers.put(courierId, courier);
            CourierPM currentCourierPM = CourierPM.of(courierId, courier);
            allCouriersPM.updateAllCouriersPM(currentCourierPM);
            return true;
        }
    }

    @Override
    public boolean setConn(String courierId, Conn conn) {
        boolean successful = false;
        CourierInfo courierInfo = getCourierInfo(courierId);
        if(courierInfo != null && conn != null){
            courierInfo.setConn(conn);
            successful = saveCourier(courierId, courierInfo);
            if(successful){
                    System.out.println("Successfully updated the courier's conn : " + conn.toString());
            }

        }

        return successful;
    }
    @Override
    public boolean updateAssignedOrders(String courierId, String addedOrderId) {
        CourierInfo courierInfo = getCourierInfo(courierId);
        courierInfo.addAssignedOrder(addedOrderId);
        boolean successful = saveCourier(courierId, courierInfo);
        System.out.println("Successfully updated the courier's assigned orders : " + successful);
        return successful;
    }
    @Override
    public boolean updateCourierInfo(String courierId, CourierInfo courierInfo) {
        boolean successful = saveCourier(courierId, courierInfo);
        System.out.println("Successfully updated the courier's info: " + successful);
        return successful;
    }

    @Override
    public Conn getConn(String courierId) {
        CourierInfo courier= getCourierInfo(courierId);
        return courier.getConn();
    }

    @Override
    public CourierStatus getStatus(String courierId) {
        CourierInfo courier = getCourierInfo(courierId);
        return courier.getStatus();
    }
    @Override
    public CourierInfo getCourierInfo(String courierId) {
        return couriers.get(courierId);
    }

    @Override
    public ContactInfo getContactInfo(String courierId) {
        CourierInfo courier= getCourierInfo(courierId);
        return courier.getContactInfo();
    }

    @Override
    public Set<String> getAssignedOrders(String courierId) {
        CourierInfo courier= getCourierInfo(courierId);
        return courier.getAssignedOrders();
    }

    public AllCouriersPM getAllCouriersPM() {
        return this.allCouriersPM;
    }
}
