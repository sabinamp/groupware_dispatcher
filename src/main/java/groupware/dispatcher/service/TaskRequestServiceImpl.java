package groupware.dispatcher.service;


import groupware.dispatcher.presentationmodel.AllTaskRequestsPM;
import groupware.dispatcher.presentationmodel.TaskRequestPM;
import groupware.dispatcher.service.model.TaskRequest;
import groupware.dispatcher.service.util.ModelObjManager;
import groupware.dispatcher.view.util.TaskEvent;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class TaskRequestServiceImpl{
    private final Logger logger;
    private static Map<String, TaskRequest> tasks;
    private TaskRequest currentTaskRequest;
    private OrderServiceImpl orderService;
    private CourierServiceImpl courierService;

    private AllTaskRequestsPM allTaskRequestsPM;
    private TaskRequestPMEventListener taskRequestPMEventListener;
    private TaskRequestEventListener taskRequestEventListener;
    static{
        tasks= new HashMap<>();
    }

    public TaskRequestServiceImpl(OrderServiceImpl orderService, CourierServiceImpl courierService){
        logger = LogManager.getLogManager().getLogger(String.valueOf(TaskRequestServiceImpl.class));
        this.orderService = orderService;
        this.courierService = courierService;
    }

    public TaskRequest getTaskRequestById(String taskId){
            return tasks.get(taskId);
      }

    public boolean updateTaskRequest(String id, TaskRequest taskRequest) {
        if(taskRequest == null) {
            logger.info("updateTask() received arg taskRequest - null");
            return false;
        }else {
            if(tasks.get(id) != null){
                tasks.put(id, taskRequest);
                setCurrentTaskRequest(taskRequest);
                System.out.println("TaskRequestServiceImplementation updateTaskRequest() called. " +
                        "The task with id : "+ id+" updated.");
                if(taskRequestEventListener != null){
                    taskRequestEventListener.handleTaskUpdateEvent(new TaskEvent(TaskEvent.UPDATE),taskRequest);
                }
            }else{
                tasks.put(id, taskRequest);
                setCurrentTaskRequest(taskRequest);
                System.out.println("TaskRequestServiceImplementation updateTaskRequest() called. " +
                        "The task with id : "+ id+" added.");
                if(taskRequestEventListener != null){
                    taskRequestEventListener.handleNewTaskEvent(new TaskEvent(TaskEvent.NEW_TASK), taskRequest);
                }
            }
            return true;
        }
    }

    public boolean updateAllTaskRequestPM(String id, TaskRequest taskRequest, String update) {
        if(taskRequest == null) {
            logger.info("updateAllTaskRequestPM() received arg taskRequest - null");
            return false;
        }else {
            if(tasks.get(id) != null){
                tasks.put(id, taskRequest);
                setCurrentTaskRequest(taskRequest);
                System.out.println("TaskRequestServiceImplementation updateAllTaskRequestPM() called. " +
                        "The task with id : "+ id+" updated.");
                if(taskRequestPMEventListener != null){
                    taskRequestPMEventListener.handleTaskUpdateEvent(new TaskEvent(TaskEvent.UPDATE), TaskRequestPM.of(taskRequest), update);
                }
            }else{
                System.out.println("TaskRequestServiceImplementation updateAllTaskRequestPM() called. " +
                        "The task with id : "+ id+" should exist but it doesn't exist in the tasks list.");

            }
            return true;
        }
    }

  /*  public boolean updateTaskRequestAssignee(String taskId, String courierId) {
        TaskRequest task= getTaskRequestById(taskId);
        task.setAssigneeId(courierId);
        boolean successful = updateTaskRequest(taskId, task);
        System.out.println("Successfully updated the assignee : " + successful);
        return successful;
    }*/


    public boolean updateTaskRequestDueOn(String taskId, LocalDateTime dueOn) {
        TaskRequest task = getTaskRequestById(taskId);
        task.setDueOn(dueOn);
        boolean successful = updateTaskRequest(taskId, task);
        System.out.println("Successfully updated the task request due date : " + successful);
        return successful;
    }

    public boolean updateTaskRequestDone(String taskId, boolean done, String topicEnd) {
        TaskRequest task = getTaskRequestById(taskId);
        task.setDone(done);
        boolean successful = updateAllTaskRequestPM(taskId, task, "Task "+topicEnd +"Completed: "+done);
        System.out.println("Successfully updated the task request : " + successful);
        return successful;
    }

    public boolean updateTaskRequestAccepted(String taskId, boolean confirmed, String topicEnd) {
        TaskRequest task = getTaskRequestById(taskId);
        task.setConfirmed(confirmed);
        boolean successful = updateAllTaskRequestPM(taskId, task, "Task "+topicEnd +" Accepted: "+confirmed);
        System.out.println("Successfully updated the task request : " + successful);
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



    public TaskRequestPMEventListener getTaskRequestPMEventListener() {
        return taskRequestPMEventListener;
    }

    public void setTaskRequestPMEventListener(TaskRequestPMEventListener taskRequestPMEventListener) {
        this.taskRequestPMEventListener = taskRequestPMEventListener;
    }

    public TaskRequestEventListener getTaskRequestEventListener() {
        return taskRequestEventListener;
    }

    public void setTaskRequestEventListener(TaskRequestEventListener taskRequestEventListener) {
        this.taskRequestEventListener = taskRequestEventListener;
    }

}
