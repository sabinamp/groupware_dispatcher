package groupware.dispatcher.service;


import groupware.dispatcher.presentationmodel.AllTaskRequestsPM;
import groupware.dispatcher.presentationmodel.TaskRequestPM;
import groupware.dispatcher.service.model.DeliveryStep;
import groupware.dispatcher.service.model.RequestReply;
import groupware.dispatcher.service.model.TaskRequest;
import groupware.dispatcher.service.util.ModelObjManager;
import groupware.dispatcher.view.util.TaskEvent;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class TaskRequestServiceImpl{
    private static final Logger logger = LogManager.getLogManager().getLogger(String.valueOf(TaskRequestServiceImpl.class));
    private static Map<String, TaskRequest> tasks;
    private TaskRequest currentTaskRequest;
    private OrderServiceImpl orderService;
    private CourierServiceImpl courierService;

    private AllTaskRequestsPM allTaskRequestsPM;
    private TaskRequestEventListener taskRequestEventListener;
    private TaskRequestPMEventListener taskRequestPMEventListener;
    static{
        tasks= new HashMap<>();
    }

    public TaskRequestServiceImpl(OrderServiceImpl orderService, CourierServiceImpl courierService){
        this.orderService = orderService;
        this.courierService = courierService;
    }

    public TaskRequest getTaskRequestById(String taskId){
            return tasks.get(taskId);
      }

    public boolean updateTaskRequest(String id, TaskRequest taskRequest, String update) {
        if(taskRequest == null) {
            logger.info("updateTask() received arg taskRequest - null");
            return false;
        } else {
            boolean updating = tasks.get(id) != null;
            tasks.put(id, taskRequest);
            setCurrentTaskRequest(taskRequest);
            if(updating){
                System.out.println("TaskRequestServiceImplementation updateTaskRequest() called. " +
                        "The task with id : "+ id+" updated.");
                if( taskRequestEventListener != null){
                    TaskEvent updateEvent = new TaskEvent(TaskEvent.UPDATE);
                    taskRequestEventListener.handleTaskUpdateEvent(updateEvent, TaskRequestPM.of(taskRequest), update);
                }
            }else{
                if(taskRequestPMEventListener != null){
                    if(update!= null && update.equals("Task_Timeout")){
                        taskRequestPMEventListener.handleTimeoutTaskEvent(new TaskEvent(TaskEvent.TASK_TIMEOUT), taskRequest);
                    } else{
                        taskRequestPMEventListener.handleNewTaskEvent(new TaskEvent(TaskEvent.NEW_TASK), taskRequest);
                        System.out.println("TaskRequestServiceImplementation updateTaskRequest() called. " +
                                "The task with id : "+ id+" added.");
                    }

                }
            }
            return true;
        }
    }

    public boolean updateTaskRequestReply(String taskId, RequestReply reply, String update, String assigneeID) {
        TaskRequest task = getTaskRequestById(taskId);
        task.setConfirmed(reply);
        if(reply.equals(RequestReply.ACCEPTED)){
            courierService.updateAssignedOrders(assigneeID, task.getOrderId());
            orderService.updateOrderAssignee(task.getOrderId(),assigneeID);
            //to update order status from confirmed to assigned in the case of a new order
        }
        if(reply.equals(RequestReply.TIMEOUT)){
            System.out.println("Task timed out!");
        }
        boolean successful = this.updateTaskRequest(taskId, task, update);
        System.out.println("Successfully updated the task request due date : " + successful);
        return successful;
    }

    public boolean updateTaskRequestStatusCompleted(String taskId, boolean done, String update, String assigneeID, TaskRequest updatedTask) {
        //TaskRequest task = getTaskRequestById(taskId);
        boolean successful = false;
        if(updatedTask !=null && updatedTask.getTaskId().equals(taskId)){
            updatedTask.setDone(true);
            DeliveryStep step= new DeliveryStep();
            step.setCurrentAssignee(assigneeID);
            step.setCurrentStatus(updatedTask.getOutcome());
            step.setUpdatedWhen(updatedTask.getCompletedWhen());
            orderService.updateOrderStatus(updatedTask.getOrderId(),step);

            successful = updateTaskRequest(taskId, updatedTask, update);
            System.out.println("Successfully updated the task request : "+taskId + successful);
        }
       return successful;
    }

   public boolean updateTaskRequestDueOn(String taskId, LocalDateTime dueOn, String update) {
        TaskRequest task = getTaskRequestById(taskId);
        task.setDueOn(dueOn);
        boolean successful = updateTaskRequest(taskId, task, update);
        System.out.println("Successfully updated the task request due date : " + successful);
        return successful;
    }


    public AllTaskRequestsPM getAllTaskRequestsPM() {
        return allTaskRequestsPM;
    }


    public TaskRequest getCurrentTaskRequest() {
        return currentTaskRequest;
    }

    public void setCurrentTaskRequest(TaskRequest currentTaskRequest) {
        this.currentTaskRequest = currentTaskRequest;
    }

    public String convertToJson(TaskRequest taskRequest) {
       return ModelObjManager.convertToJSON(taskRequest);
    }

    public TaskRequestEventListener getTaskRequestEventListener() {
        return taskRequestEventListener;
    }

    public void setTaskRequestEventListener(TaskRequestEventListener taskRequestEventListener) {
        this.taskRequestEventListener = taskRequestEventListener;
    }

    public TaskRequestPMEventListener getTaskRequestPMEventListener() {
        return taskRequestPMEventListener;
    }

    public void setTaskRequestPMEventListener(TaskRequestPMEventListener taskRequestPMEventListener) {
        this.taskRequestPMEventListener = taskRequestPMEventListener;
    }
}
