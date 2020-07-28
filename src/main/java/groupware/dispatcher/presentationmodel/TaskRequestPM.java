package groupware.dispatcher.presentationmodel;

import groupware.dispatcher.service.model.DeliveryType;
import groupware.dispatcher.service.model.ShiftType;
import groupware.dispatcher.service.model.TaskType;
import javafx.beans.property.*;

import java.time.LocalDateTime;

public class TaskRequestPM {

    private static final String ELLIPSIS = "...";
    private StringProperty taskId = new SimpleStringProperty(ELLIPSIS);
    private final StringProperty orderId = new SimpleStringProperty();
    private final StringProperty assigneeId = new SimpleStringProperty();
    private final StringProperty addressLine = new SimpleStringProperty();
    private final ObjectProperty<DeliveryType> deliveryType = new SimpleObjectProperty<>();


    private final ObjectProperty<TaskType> taskType = new SimpleObjectProperty<>();
    private final ObjectProperty<ShiftType> shiftType = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> dueOn = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> sentWhen = new SimpleObjectProperty<>();
    private final BooleanProperty accepted= new SimpleBooleanProperty();

    private final LongProperty timeoutMinutes = new SimpleLongProperty(5);

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    private final StringProperty name = new SimpleStringProperty(ELLIPSIS);

    public String getTaskId() {
        return taskId.get();
    }

    public StringProperty taskIdProperty() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId.set(taskId);
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

    public String getAssigneeId() {
        return assigneeId.get();
    }

    public StringProperty assigneeIdProperty() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId.set(assigneeId);
    }

    public String getAddressLine() {
        return addressLine.get();
    }

    public StringProperty addressLineProperty() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine.set(addressLine);
    }

    public DeliveryType getDeliveryType() {
        return deliveryType.get();
    }

    public ObjectProperty<DeliveryType> deliveryTypeProperty() {
        return deliveryType;
    }

    public void setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType.set(deliveryType);
    }



    public ShiftType getShiftType() {
        return shiftType.get();
    }

    public ObjectProperty<ShiftType> shiftTypeProperty() {
        return shiftType;
    }

    public void setShiftType(ShiftType shiftType) {
        this.shiftType.set(shiftType);
    }

    public LocalDateTime getDueOn() {
        return dueOn.get();
    }

    public ObjectProperty<LocalDateTime> dueOnProperty() {
        return dueOn;
    }

    public void setDueOn(LocalDateTime dueOn) {
        this.dueOn.set(dueOn);
    }

    public LocalDateTime getSentWhen() {
        return sentWhen.get();
    }

    public ObjectProperty<LocalDateTime> sentWhenProperty() {
        return sentWhen;
    }

    public void setSentWhen(LocalDateTime sentWhen) {
        this.sentWhen.set(sentWhen);
    }

    public boolean isAccepted() {
        return accepted.get();
    }

    public BooleanProperty acceptedProperty() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted.set(accepted);
    }

    public long getTimeoutMinutes() {
        return timeoutMinutes.get();
    }

    public LongProperty timeoutMinutesProperty() {
        return timeoutMinutes;
    }

    public void setTimeoutMinutes(long timeoutMinutes) {
        this.timeoutMinutes.set(timeoutMinutes);
    }

    public TaskType getTaskType() {
        return taskType.get();
    }

    public ObjectProperty<TaskType> taskTypeProperty() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType.set(taskType);
    }
    
}
