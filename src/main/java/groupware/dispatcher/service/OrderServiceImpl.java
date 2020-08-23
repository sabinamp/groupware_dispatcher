package groupware.dispatcher.service;

import groupware.dispatcher.presentationmodel.AllOrdersPM;
import groupware.dispatcher.presentationmodel.OrderPM;
import groupware.dispatcher.service.model.*;
import groupware.dispatcher.service.util.ModelObjManager;

import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class OrderServiceImpl implements OrderService{
    private final Logger logger;
    private static Map<String, OrderDescriptiveInfo> orders;


    private OrderEventListener orderEventListener;

    static{

        orders= new HashMap<>();
    }

    public OrderServiceImpl(){
        logger = LogManager.getLogManager().getLogger(String.valueOf(OrderServiceImpl.class));

    }

    @Override
    public OrderDescriptiveInfo getOrder(String orderId){
        return orders.get(orderId);
    }

    @Override
    public boolean updateOrder(String id, OrderDescriptiveInfo order) {
        if(order == null) {
            logger.info("updateOrder() received arg order- null");
            return false;
        }else {
            if(orders.get(id) != null){
                orders.put(id, order);
                orderEventListener.handleOrderUpdateEvent(OrderPM.ofOrder(order));
            }else{
                orders.put(id, order);
                orderEventListener.handleNewOrderEvent(OrderPM.ofOrder(order));
            }
            return true;
        }
    }

    @Override
    public boolean setParcelCollectionDate(String orderId, String scheduledParcelCollectionWhen) {
        OrderDescriptiveInfo orderDescrInfo= getOrder(orderId);
        OrderInfo oinfo = orderDescrInfo.getOrderInfo();
        oinfo.setScheduledParcelCollectionWhen(LocalDateTime.parse(scheduledParcelCollectionWhen));
        boolean successful = updateOrder(orderId, orderDescrInfo);
        System.out.println("Successfully updated the order : " + successful);
        return successful;
    }

    @Override
    public boolean setDeliveryDate(String orderId, String scheduledDeliveryWhen) {
        OrderDescriptiveInfo orderDescrInfo= getOrder(orderId);
        OrderInfo oinfo = orderDescrInfo.getOrderInfo();
        oinfo.setScheduledDeliveryWhen(LocalDateTime.parse(scheduledDeliveryWhen));
        boolean successful = updateOrder(orderId, orderDescrInfo);
        System.out.println("Successfully updated the order : " + successful);
        return successful;
    }

    @Override
    public boolean updateOrderAllInfo(String orderId, String allInfo) {
        boolean successful= false;
        OrderDescriptiveInfo currentOrder = ModelObjManager.convertJsonToOrderDescriptiveInfo(allInfo);
        OrderDescriptiveInfo previous = orders.get(orderId);
        if(previous != null){
            successful = updateOrder(orderId, currentOrder);
            System.out.println("Successfully updated the order : " + successful);
        }
        return successful;
    }

    @Override
    public boolean updateOrderInfo(String orderId, String info) {
        boolean successful = false;
        OrderInfo currentOrderInfo = ModelObjManager.convertJsonToOrderInfo(info);
        OrderDescriptiveInfo orderToUpdate = orders.get(orderId);
        if(orderToUpdate != null){
            orderToUpdate.setOrderInfo(currentOrderInfo);
            successful = updateOrder(orderId, orderToUpdate);
            System.out.println("Successfully updated the order : " + successful);
        }
        return successful;
    }

    @Override
    public boolean updateOrderAssignee(String orderId, String courierId) {
        OrderDescriptiveInfo orderDescrInfo= getOrder(orderId);
        DeliveryStep lastStep = orderDescrInfo.getDeliveryInfos().getLast();
        lastStep.setCurrentAssignee(courierId);
        boolean successful = updateOrder(orderId, orderDescrInfo);
        System.out.println("Successfully updated the order assignee : " + successful);
        return successful;
    }

    @Override
    public OrderInfo getOrderInfo(String orderId) {
        OrderDescriptiveInfo orderDescrInfo= getOrder(orderId);
        if( orderDescrInfo != null){
            return orderDescrInfo.getOrderInfo();
        }
        else return null;
    }

    @Override
    public List<ContactInfo> getOrderCustomerContactInfos(String orderId) {
        OrderDescriptiveInfo orderDescrInfo= getOrder(orderId);
        if( orderDescrInfo != null){
            return orderDescrInfo.getContactInfos();
        }
        else return null;
    }

    @Override
    public List<ContactInfo> getOrderDestinationContactInfos(String orderId) {
        OrderDescriptiveInfo orderDescrInfo= getOrder(orderId);
        return orderDescrInfo.getFinalDestinationContactInfos();
    }

    @Override
    public LinkedList<DeliveryStep> getOrderDeliveryInfos(String orderId) {
        OrderDescriptiveInfo orderDescrInfo= getOrder(orderId);
        if(orderDescrInfo != null){
            return orderDescrInfo.getDeliveryInfos();
        }else return null;
    }

    @Override
    public boolean updateOrderStatus(String orderId, DeliveryStep deliveryStep) {
        boolean successful= false;
        System.out.println("next step is : " + deliveryStep);
       // DeliveryStep next = ModelObjManager.convertDeliveryStepData(deliveryStep);
        System.out.println("the next step (converted from json) is : " + deliveryStep);

        OrderDescriptiveInfo order=orders.get(orderId);
        if(order != null){
            order.addDeliveryInfosItem(deliveryStep);
            successful = updateOrder(orderId, order);
            if(successful){
                System.out.println("updateOrderStatus() - Successfully updated the order.");
            }
        }
        return successful;
    }

    @Override
    public Set<String> getAllOrderIds() {
        logger.entering("OrderServiceImpl", "entering getAllOrderIds()");
        Set<String> orderIds = orders.keySet();
        logger.info("the existing orders are the following: ");
        for (String orderId : orderIds) {
            logger.info("orderId " + orderId);
        }
        logger.exiting("OrderServiceImpl", "exiting getAllOrderIds()");
        return orderIds;
    }



    @Override
    public Map<String, OrderDescriptiveInfo> getOrders() {
        return orders;
    }


    public OrderEventListener getOrderEventListener() {
        return orderEventListener;
    }

    public void setOrderEventListener(OrderEventListener orderEventListener) {
        this.orderEventListener = orderEventListener;
    }
}
