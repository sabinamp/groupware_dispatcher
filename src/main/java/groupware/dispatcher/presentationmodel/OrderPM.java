package groupware.dispatcher.presentationmodel;

import groupware.dispatcher.service.model.*;
import javafx.beans.property.*;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class OrderPM {
    private static final String ELLIPSIS = "...";

    private final StringProperty orderId = new SimpleStringProperty();
    private final StringProperty customerName =  new SimpleStringProperty(ELLIPSIS);


    //final destination address
    private final ObjectProperty<ContactInfo> destinationAddress =  new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> orderPlacedWhen = new SimpleObjectProperty<>();

    private final ObjectProperty<DeliveryType> orderType = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> scheduledParcelCollectionWhen = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> orderUpdatedWhen = new SimpleObjectProperty<>();


    private ObjectProperty<List<ContactInfo>> contactInfos = new SimpleObjectProperty<>();
    private final ObjectProperty<OrderStatus> orderStatus = new SimpleObjectProperty<>();
    private final StringProperty currentAssignee =  new SimpleStringProperty(ELLIPSIS);
    private final ObjectProperty<DeliveryStep> orderLastUpdate = new SimpleObjectProperty<>();
    //private final DoubleProperty price = new SimpleDoubleProperty(0.0);

    public OrderPM(){

    }

    public static OrderPM ofOrder(OrderDescriptiveInfo orderDescriptiveInfo){
        OrderPM orderPM = new OrderPM();
        orderPM.setOrderId(orderDescriptiveInfo.getOrderId());
        orderPM.setCustomerName(orderDescriptiveInfo.getCustomerName());
        orderPM.setContactInfos(orderDescriptiveInfo.getContactInfos());
       List<ContactInfo> destinationList = orderDescriptiveInfo.getFinalDestinationContactInfos();
       if(destinationList != null && !destinationList.isEmpty()){
           orderPM.setDestinationAddress(destinationList.get(0));
       }

        OrderInfo orderInfo = orderDescriptiveInfo.getOrderInfo();
        orderPM.setOrderType(orderInfo.getDeliveryType());
        orderPM.setOrderPlacedWhen(orderInfo.getPlacedWhen());
        LinkedList<DeliveryStep> deliveryInfos = orderDescriptiveInfo.getDeliveryInfos();
        DeliveryStep lastStep = null;
       if(deliveryInfos != null){
           lastStep = deliveryInfos.getLast();
       }
        if(lastStep != null){
            orderPM.setOrderLastUpdate(lastStep);
            orderPM.setOrderUpdatedWhen(lastStep.getUpdatedWhen());
            orderPM.setOrderStatus(lastStep.getCurrentStatus());
            orderPM.setCurrentAssignee(lastStep.getCurrentAssignee());
        }

        orderPM.setScheduledParcelCollectionWhen(orderInfo.getScheduledParcelCollectionWhen());
//        orderPM.setPrice(orderInfo.getPrice());
        return orderPM;
    }

    public String getOrderId() {
        return orderId.get();
    }

    public StringProperty orderIdProperty() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId.set(orderId);
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public StringProperty customerNameProperty() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public LocalDateTime getOrderPlacedWhen() {
        return orderPlacedWhen.get();
    }

    public ObjectProperty<LocalDateTime> orderPlacedWhenProperty() {
        return orderPlacedWhen;
    }

    public void setOrderPlacedWhen(LocalDateTime orderPlacedWhen) {
        this.orderPlacedWhen.set(orderPlacedWhen);
    }

    public LocalDateTime getScheduledParcelCollectionWhen() {
        return scheduledParcelCollectionWhen.get();
    }

    public ObjectProperty<LocalDateTime> scheduledParcelCollectionWhenProperty() {
        return scheduledParcelCollectionWhen;
    }

    public void setScheduledParcelCollectionWhen(LocalDateTime scheduledParcelCollectionWhen) {
        this.scheduledParcelCollectionWhen.set(scheduledParcelCollectionWhen);
    }

    public LocalDateTime getOrderUpdatedWhen() {
        return orderUpdatedWhen.get();
    }

    public ObjectProperty<LocalDateTime> orderUpdatedWhenProperty() {
        return orderUpdatedWhen;
    }

    public void setOrderUpdatedWhen(LocalDateTime orderUpdatedWhen) {
        this.orderUpdatedWhen.set(orderUpdatedWhen);
    }

    public OrderStatus getOrderStatus() {
        return orderStatus.get();
    }

    public ObjectProperty<OrderStatus> orderStatusProperty() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus.set(orderStatus);
    }

    public String getCurrentAssignee() {
        return currentAssignee.get();
    }

    public StringProperty currentAssigneeProperty() {
        return currentAssignee;
    }

    public void setCurrentAssignee(String currentAssignee) {
        this.currentAssignee.set(currentAssignee);
    }



    /*   public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }*/

    public DeliveryStep getOrderLastUpdate() {
        return orderLastUpdate.get();
    }

    public ObjectProperty<DeliveryStep> orderLastUpdateProperty() {
        return orderLastUpdate;
    }

    public void setOrderLastUpdate(DeliveryStep orderLastUpdate) {
        this.orderLastUpdate.set(orderLastUpdate);
    }

    public List<ContactInfo> getContactInfos() {
        return contactInfos.get();
    }

    public ObjectProperty<List<ContactInfo>> contactInfosProperty() {
        return contactInfos;
    }

    public void setContactInfos(List<ContactInfo> contactInfos) {
        this.contactInfos.set(contactInfos);
    }


    public ContactInfo getDestinationAddress() {
        return destinationAddress.get();
    }

    public ObjectProperty<ContactInfo> destinationAddressProperty() {
        return destinationAddress;
    }

    public void setDestinationAddress(ContactInfo destinationAddress) {
        this.destinationAddress.set(destinationAddress);
    }

    public DeliveryType getOrderType() {
        return orderType.get();
    }

    public ObjectProperty<DeliveryType> orderTypeProperty() {
        return orderType;
    }

    public void setOrderType(DeliveryType orderType) {
        this.orderType.set(orderType);
    }

    @Override
    public String toString() {
        return "OrderPM{" +
                "orderId=" + orderId +
                ", customerName=" + customerName +
                ", orderPlacedWhen=" + orderPlacedWhen +
                ", scheduledParcelCollectionWhen=" + scheduledParcelCollectionWhen +
                ", orderUpdatedWhen=" + orderUpdatedWhen +
                ", contactInfos=" + contactInfos +
                ", orderStatus=" + orderStatus +
                ", currentAssignee=" + currentAssignee +
                '}';
    }


}
