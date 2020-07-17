package groupware.dispatcher.presentationmodel;

import groupware.dispatcher.service.model.ContactInfo;
import groupware.dispatcher.service.model.DeliveryStep;
import groupware.dispatcher.service.model.OrderDescriptiveInfo;
import groupware.dispatcher.service.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public class OrderPM {
    private String orderId;
    private String customerName;
    private LocalDateTime orderPlacedWhen;
    private LocalDateTime orderUpdatedWhen;
    private List<ContactInfo> contactInfos = null;
    private String orderStatus;
    private String currentAssignee;

    public OrderPM(OrderDescriptiveInfo orderDescriptiveInfo){
        orderId= orderDescriptiveInfo.getOrderId();
        customerName = orderDescriptiveInfo.getCustomerName();
        orderPlacedWhen = orderDescriptiveInfo.getOrderInfo().getPlacedWhen();
        DeliveryStep lastStep= orderDescriptiveInfo.getDeliveryInfos().getLast();
        orderUpdatedWhen = lastStep.getUpdatedWhen();
        orderStatus = lastStep.getCurrentStatus();
        currentAssignee = lastStep.getCurrentAssignee();
    }




}
