package groupware.dispatcher.service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Objects;


public class OrderInfo {
    @JsonProperty("placedWhen")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm", locale = "de")
    private LocalDateTime  placedWhen = null;

    @JsonProperty("deliveryType")
    private DeliveryType deliveryType = null;

    @JsonProperty("scheduledParcelCollectionWhen")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm", locale = "de")
    private LocalDateTime  scheduledParcelCollectionWhen = null;

    @JsonProperty("scheduledDeliveryWhen")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm", locale = "de")
    private LocalDateTime  scheduledDeliveryWhen = null;

    @JsonProperty("parcelWeight")
    private Double parcelWeight = null;

    @JsonProperty("price")
    private Double price = null;

    @JsonProperty("assignedTo")
    private String assignedTo = null;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm", locale = "de")
    public LocalDateTime getPlacedWhen() {
        return placedWhen;
    }

    public void setPlacedWhen(LocalDateTime  placedWhen) {
        this.placedWhen = placedWhen;
    }

    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm", locale = "de")
    public LocalDateTime  getScheduledParcelCollectionWhen() {
        return scheduledParcelCollectionWhen;
    }

    public void setScheduledParcelCollectionWhen(LocalDateTime  scheduledParcelCollectionWhen) {
        this.scheduledParcelCollectionWhen = scheduledParcelCollectionWhen;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm", locale = "de")
    public LocalDateTime  getScheduledDeliveryWhen() {
        return scheduledDeliveryWhen;
    }

    public void setScheduledDeliveryWhen(LocalDateTime  scheduledDeliveryWhen) {
        this.scheduledDeliveryWhen = scheduledDeliveryWhen;
    }

    public Double getParcelWeight() {
        return parcelWeight;
    }

    public void setParcelWeight(Double parcelWeight) {
        this.parcelWeight = parcelWeight;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public OrderInfo placedWhen(LocalDateTime  placedWhen) {
        this.placedWhen = placedWhen;
        return this;
    }

    public OrderInfo scheduledDeliveryWhen(LocalDateTime  scheduledDeliveryWhen) {
        this.scheduledDeliveryWhen = scheduledDeliveryWhen;
        return this;
    }

    public OrderInfo scheduledParcelCollectionWhen(LocalDateTime  scheduledParcelCollectionWhen) {
        this.scheduledParcelCollectionWhen = scheduledParcelCollectionWhen;
        return this;
    }

    public OrderInfo assignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
        return this;
    }

    public OrderInfo deliveryType(DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
        return this;
    }

    public OrderInfo parcelWeight(Double parcelWeight) {
        this.parcelWeight= parcelWeight;
        return this;
    }

    public OrderInfo price(Double price) {
        this.price= price;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderInfo orderInfo = (OrderInfo) o;
        return Objects.equals(this.placedWhen, orderInfo.placedWhen) &&
                Objects.equals(this.deliveryType, orderInfo.deliveryType) &&
                Objects.equals(this.parcelWeight, orderInfo.parcelWeight) &&
                Objects.equals(this.price, orderInfo.price) &&
                Objects.equals(this.scheduledParcelCollectionWhen, orderInfo.scheduledParcelCollectionWhen) &&
                Objects.equals(this.scheduledDeliveryWhen, orderInfo.scheduledDeliveryWhen) &&
                Objects.equals(this.assignedTo, orderInfo.assignedTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placedWhen, deliveryType, scheduledParcelCollectionWhen, scheduledDeliveryWhen,
                parcelWeight, price, assignedTo);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OrderInfo {\n");

        sb.append("    placedWhen: ").append(toIndentedString(placedWhen)).append("\n");
        sb.append("    deliveryType: ").append(toIndentedString(deliveryType)).append("\n");
        sb.append("    parcelWeight: ").append(toIndentedString(parcelWeight)).append("\n");
        sb.append("    price: ").append(toIndentedString(price)).append("\n");
        sb.append("    assignedTo: ").append(toIndentedString(assignedTo)).append("\n");
        sb.append("    scheduledParcelCollectionWhen: ").append(toIndentedString(scheduledParcelCollectionWhen)).append("\n");
        sb.append("    scheduledDeliveryWhen: ").append(toIndentedString(scheduledDeliveryWhen)).append("\n");
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
