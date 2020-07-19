package groupware.dispatcher.service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import groupware.dispatcher.service.OrderService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;


public class DeliveryStep {

    @JsonProperty("currentStatus")
    private OrderStatus currentStatus = null;

    @JsonProperty("currentAssignee")
    private String currentAssignee = null;

    @JsonProperty("updatedWhen")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm", locale = "de")
    private LocalDateTime updatedWhen = null;


    public OrderStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(OrderStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getCurrentAssignee() {
        return currentAssignee;
    }

    public void setCurrentAssignee(String currentAssignee) {
        this.currentAssignee = currentAssignee;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm", locale = "de")
    public LocalDateTime getUpdatedWhen() {
        return updatedWhen;
    }

    public void setUpdatedWhen(LocalDateTime updatedWhen) {
        this.updatedWhen = updatedWhen;
    }



    public DeliveryStep currentStatus(OrderStatus currentStatus) {
        this.currentStatus = currentStatus;
        return this;
    }

    public DeliveryStep currentAssignee(String currentAssignee) {
        this.currentAssignee = currentAssignee;
        return this;
    }

    public DeliveryStep updatedWhen(LocalDateTime updatedWhen) {
        this.updatedWhen = updatedWhen;
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
        DeliveryStep step = (DeliveryStep) o;
        return  Objects.equals(this.currentStatus, step.currentStatus) &&
                Objects.equals(this.currentAssignee, step.currentAssignee) &&
                Objects.equals(this.updatedWhen, step.updatedWhen);
    }

    @Override
    public int hashCode() {
        return Objects.hash( currentStatus, currentAssignee, updatedWhen);
    }

    @Override
    public String toString() {
        return "DeliveryStep{" +
                ", currentStatus='" + currentStatus + '\'' +
                ", currentAssignee='" + currentAssignee + '\'' +
                ", updatedWhen=" + updatedWhen +
                '}';
    }
}
