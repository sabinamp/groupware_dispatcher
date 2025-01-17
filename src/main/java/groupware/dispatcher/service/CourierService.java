package groupware.dispatcher.service;

import groupware.dispatcher.service.model.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CourierService {
    boolean saveCourier(String courierId, CourierInfo courier);
    boolean setConn(String courierId, Conn conn);
    boolean setStatus(String courierId, CourierStatus status);
    Conn getConn(String courierId);
    CourierStatus getStatus(String courierId);

    CourierInfo getCourierInfo(String courierId);
    ContactInfo getContactInfo(String courierId);

    Set<String> getAssignedOrders(String courierId);
    boolean updateAssignedOrders(String courierId, String addedOrderId);

    boolean updateCourierInfo(String courierId, CourierInfo receivedString);
}
