package groupware.dispatcher.service;

import groupware.dispatcher.service.model.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface OrderService {
    OrderDescriptiveInfo getOrder(String orderId);
    boolean updateOrder(String id, OrderDescriptiveInfo order);
    boolean setParcelCollectionDate(String orderId, String scheduledParcelCollectionWhen);
    boolean setDeliveryDate(String orderId, String scheduledDeliveryWhen);
    boolean updateOrderAllInfo(String orderId, String allinfo);
    boolean updateOrderInfo(String orderId, String info);
    boolean updateOrderAssignee(String orderId, String courierId);

    OrderInfo getOrderInfo(String orderId);
    List<ContactInfo> getOrderCustomerContactInfos(String orderId);
    List<ContactInfo> getOrderDestinationContactInfos(String orderId);
    LinkedList<DeliveryStep> getOrderDeliveryInfos(String orderId);
    boolean updateOrderStatus(String orderId, DeliveryStep deliveryStep);
    Set<String> getAllOrderIds();
    Map<String, OrderDescriptiveInfo> getOrders();

}
