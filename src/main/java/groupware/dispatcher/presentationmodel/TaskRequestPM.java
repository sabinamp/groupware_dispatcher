package groupware.dispatcher.presentationmodel;

import groupware.dispatcher.service.model.*;
import javafx.beans.property.*;
import javafx.beans.value.WritableObjectValue;

import java.time.LocalDateTime;

public class TaskRequestPM {

    private static final String ELLIPSIS = "...";
    private StringProperty taskId = new SimpleStringProperty(ELLIPSIS);
    private final StringProperty orderId = new SimpleStringProperty();
    private final StringProperty assigneeId = new SimpleStringProperty();
    private final ObjectProperty<DeliveryType> deliveryType = new SimpleObjectProperty<>();
    private final ObjectProperty<TaskType> taskType = new SimpleObjectProperty<>();

    private final ObjectProperty<LocalDateTime> dueOn = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> sentWhen = new SimpleObjectProperty<>();
    private final ObjectProperty<RequestReply> requestReply = new SimpleObjectProperty<>();
    private final BooleanProperty done= new SimpleBooleanProperty();



    public TaskRequestPM(){

    }

    public void reset() {
        this.setDone(false);
        this.setDueOn(null);
        this.setRequestReply(null);
        this.setAssigneeId("");
        this.setOrderId("");
        this.setDeliveryType(null);
        this.setTaskType(null);
        this.setTaskId("");
    }

    public static TaskRequestPM of(TaskRequest taskRequest){
        TaskRequestPM taskPM= new TaskRequestPM();
        taskPM.setTaskId(taskRequest.getTaskId());
        taskPM.setOrderId(taskRequest.getOrderId());
        taskPM.setAssigneeId(taskRequest.getAssigneeId());
        taskPM.setTaskType(taskRequest.getTaskType());
        taskPM.setDeliveryType(taskRequest.getDeliveryType());
        taskPM.setRequestReply(taskRequest.getConfirmed());
        taskPM.setDone(taskRequest.isDone());
        taskPM.setDueOn(taskRequest.getDueOn());
        return taskPM;
    }

    public static TaskRequest toTaskRequest(TaskRequestPM taskPM){
        TaskRequest taskRequest =  new TaskRequest();
        taskRequest.setTaskId(taskPM.getTaskId());
        taskRequest.setDone(taskPM.isDone());
        taskRequest.setOrderId(taskPM.getOrderId());
        taskRequest.setDueOn(taskPM.getDueOn());
        taskRequest.setAssigneeId(taskPM.getAssigneeId());
        taskRequest.setConfirmed(taskPM.getRequestReply());
        taskRequest.setSentWhen(taskPM.getSentWhen());
        taskRequest.setDeliveryType(taskPM.getDeliveryType());
        taskRequest.setTaskType(taskPM.getTaskType());
        return taskRequest;

    }


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



    public DeliveryType getDeliveryType() {
        return deliveryType.get();
    }

    public ObjectProperty<DeliveryType> deliveryTypeProperty() {
        return deliveryType;
    }

    public void setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType.set(deliveryType);
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


    public TaskType getTaskType() {
        return taskType.get();
    }

    public ObjectProperty<TaskType> taskTypeProperty() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType.set(taskType);
    }

    public boolean isDone() {
        return done.get();
    }

    public BooleanProperty doneProperty() {
        return done;
    }

    public void setDone(boolean done) {
        this.done.set(done);
    }

    public RequestReply getRequestReply() {
        return requestReply.get();
    }

    public ObjectProperty<RequestReply> requestReplyProperty() {
        return requestReply;
    }

    public void setRequestReply(RequestReply requestReply){
     this.requestReply.set(requestReply);
    }

    @Override
    public String toString() {
        return "TaskRequestPM{" +
                "taskId=" + taskId +
                ", orderId=" + orderId +
                ", assigneeId=" + assigneeId +
                ", deliveryType=" + deliveryType +
                ", taskType=" + taskType +
                ", dueOn=" + dueOn +
                ", sentWhen=" + sentWhen +
                ", accepted=" + requestReply +
                ", done=" + done +
                '}';
    }
}
