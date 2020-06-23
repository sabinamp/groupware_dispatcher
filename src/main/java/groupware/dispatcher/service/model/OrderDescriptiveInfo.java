package groupware.dispatcher.service.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class OrderDescriptiveInfo {

    @JsonProperty("orderId")
    private String orderId = null;

    @JsonProperty("customerName")
    private String customerName = null;

    @JsonProperty("weightUnitOfMeasureCode")
    private WeightUnitOfMeasureCode weightUnitOfMeasureCode = null;

    @JsonProperty("timeZone")
    private String timeZone = null;

    @JsonProperty("currencyCode")
    private String currencyCode = null;

    @JsonProperty("orderInfo")
    private OrderInfo orderInfo = null;

    @JsonProperty("contactInfos")
    private List<ContactInfo> contactInfos = null;

    @JsonProperty("finalDestinationContactInfos")
    private List<ContactInfo> finalDestinationContactInfos = null;


    @JsonProperty("deliveryInfos")
    private LinkedList<DeliveryStep> deliveryInfos = null;

    public OrderDescriptiveInfo orderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    /**
     * The order ID.
     * @return orderId
     **/
    public String getOrderId() {
        return orderId;
    }

    public OrderDescriptiveInfo customerName(String customerName) {
        this.customerName = customerName;
        return this;
    }



    public OrderDescriptiveInfo weightUnitOfMeasureCode(WeightUnitOfMeasureCode weightUnitOfMeasureCode) {
        this.weightUnitOfMeasureCode = weightUnitOfMeasureCode;
        return this;
    }


    public WeightUnitOfMeasureCode getWeightUnitOfMeasureCode() {
        return weightUnitOfMeasureCode;
    }

    public void setWeightUnitOfMeasureCode(WeightUnitOfMeasureCode weightUnitOfMeasureCode) {
        this.weightUnitOfMeasureCode = weightUnitOfMeasureCode;
    }

    public OrderDescriptiveInfo timeZone(String timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public OrderDescriptiveInfo currencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }
    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    public OrderDescriptiveInfo orderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
        return this;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public OrderDescriptiveInfo contactInfos(List<ContactInfo> contactInfos) {
        this.contactInfos = contactInfos;
        return this;
    }

    public OrderDescriptiveInfo addContactInfosItem(ContactInfo contactInfosItem) {
        if (this.contactInfos == null) {
            this.contactInfos = new ArrayList<>();
        }
        this.contactInfos.add(contactInfosItem);
        return this;
    }

    public List<ContactInfo> getContactInfos() {
        return contactInfos;
    }

    public void setContactInfos(List<ContactInfo> contactInfos) {
        this.contactInfos = contactInfos;
    }

    public OrderDescriptiveInfo finalDestinationContactInfos(List<ContactInfo> contactInfos) {
        this.finalDestinationContactInfos = contactInfos;
        return this;
    }

    public OrderDescriptiveInfo addFinalDestinationContactInfosItem(ContactInfo contactInfosItem) {
        if (this.finalDestinationContactInfos == null) {
            this.finalDestinationContactInfos = new ArrayList<>();
        }
        this.contactInfos.add(contactInfosItem);
        return this;
    }

    public List<ContactInfo> getFinalDestinationContactInfos() {
        return this.finalDestinationContactInfos;
    }

    public void setFinalDestinationContactInfos(List<ContactInfo> finalDestinationContactInfos) {
        this.finalDestinationContactInfos = contactInfos;
    }
    public OrderDescriptiveInfo deliveryInfos(LinkedList<DeliveryStep> deliveryInfos) {
        this.deliveryInfos = deliveryInfos;
        return this;
    }

    public OrderDescriptiveInfo addDeliveryInfosItem(DeliveryStep deliveryStep) {
        if (this.deliveryInfos == null) {
            this.deliveryInfos = new LinkedList<>();
        }
        this.deliveryInfos.add(deliveryStep);
        return this;
    }

    public LinkedList<DeliveryStep> getDeliveryInfos() {
        return deliveryInfos;
    }

    public void setDeliveryInfos(LinkedList<DeliveryStep> deliveryInfos) {
        this.deliveryInfos = deliveryInfos;
    }
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderDescriptiveInfo orderDescriptiveInfo = (OrderDescriptiveInfo) o;
        return Objects.equals(this.orderId, orderDescriptiveInfo.orderId) &&
                Objects.equals(this.customerName, orderDescriptiveInfo.customerName) &&
                Objects.equals(this.timeZone, orderDescriptiveInfo.timeZone) &&
                Objects.equals(this.weightUnitOfMeasureCode, orderDescriptiveInfo.weightUnitOfMeasureCode) &&
                Objects.equals(this.currencyCode, orderDescriptiveInfo.currencyCode) &&
                Objects.equals(this.orderInfo, orderDescriptiveInfo.orderInfo) &&
                Objects.equals(this.contactInfos, orderDescriptiveInfo.contactInfos) &&
                Objects.equals(this.finalDestinationContactInfos, orderDescriptiveInfo.finalDestinationContactInfos) &&
                Objects.equals(this.deliveryInfos, orderDescriptiveInfo.deliveryInfos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, customerName, timeZone, weightUnitOfMeasureCode, currencyCode, orderInfo,
                contactInfos, finalDestinationContactInfos, deliveryInfos);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OrderDescriptiveInfo {\n");

        sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
        sb.append("    customerName: ").append(toIndentedString(customerName)).append("\n");
        sb.append("    timeZone: ").append(toIndentedString(timeZone)).append("\n");
        sb.append("    weightUnitOfMeasureCode: ").append(toIndentedString(weightUnitOfMeasureCode)).append("\n");
        sb.append("    currencyCode: ").append(toIndentedString(currencyCode)).append("\n");
        sb.append("    orderInfo: ").append(toIndentedString(orderInfo)).append("\n");
        sb.append("    contactInfos: ").append(toIndentedString(contactInfos)).append("\n");
        sb.append("    finalDestinationContactInfos: ").append(toIndentedString(finalDestinationContactInfos)).append("\n");
        sb.append("    deliveryInfos: ").append(toIndentedString(deliveryInfos)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
