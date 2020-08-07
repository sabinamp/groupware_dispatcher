package groupware.dispatcher.service;


import groupware.dispatcher.presentationmodel.AllCouriersPM;
import groupware.dispatcher.presentationmodel.AllOrdersPM;
import groupware.dispatcher.presentationmodel.AllTaskRequestsPM;
import groupware.dispatcher.presentationmodel.TaskRequestPM;
import groupware.dispatcher.service.model.CourierStatus;
import groupware.dispatcher.service.model.TaskRequest;
import groupware.dispatcher.service.util.ModelObjManager;

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
    private TaskEventListener taskEventListener;
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
                        "The task with id : "+ id+" added or updated.");
                if(taskEventListener != null){
                    taskEventListener.handleTaskUpdateEvent(TaskRequestPM.of(taskRequest));
                }

            }else{
                tasks.put(id, taskRequest);
                setCurrentTaskRequest(taskRequest);
                System.out.println("TaskRequestServiceImplementation updateTaskRequest() called. " +
                        "The task with id : "+ id+" added or updated.");
                if(taskEventListener != null){
                    taskEventListener.handleNewTaskEvent(TaskRequestPM.of(taskRequest));
                }
            }

            return true;
        }
    }


    public boolean updateTaskRequestAssignee(String taskId, String courierId) {
        TaskRequest task= getTaskRequestById(taskId);
        task.setAssigneeId(courierId);
        boolean successful = updateTaskRequest(taskId, task);
        System.out.println("Successfully updated the assignee : " + successful);
        return successful;
    }


    public boolean updateTaskRequestDueOn(String taskId, LocalDateTime dueOn) {
        TaskRequest task = getTaskRequestById(taskId);
        task.setDueOn(dueOn);
        boolean successful = updateTaskRequest(taskId, task);
        System.out.println("Successfully updated the task request due date : " + successful);
        return successful;
    }


    public boolean updateTaskRequestDone(String taskId, boolean done) {
        TaskRequest task = getTaskRequestById(taskId);
        task.setDone(done);
        boolean successful = updateTaskRequest(taskId, task);
        System.out.println("Successfully updated the task request : " + successful);
        return successful;
    }

    public boolean updateTaskRequestAccepted(String taskId, boolean confirmed) {
        TaskRequest task = getTaskRequestById(taskId);
        task.setConfirmed(confirmed);
        boolean successful = updateTaskRequest(taskId, task);
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

    public void confirmTask(String taskId, boolean accepted) {
        updateTaskRequestAccepted(taskId, accepted);
    }



   /* @Override
    public void handleNewTaskEvent(TaskRequest taskRequest){
        //event sent always by the GUI
        updateTaskRequest(taskRequest.getTaskId(), taskRequest);
        System.out.println("handleNewTaskEvent"+taskRequest.getTaskId());
    }

    //TaskUpdate event usually sent by the courier
    @Override
    public void handleTaskUpdateEvent(TaskRequest taskRequest) {
        updateTaskRequest(taskRequest.getTaskId(), taskRequest);
        allTaskRequestsPM.updateAllTaskRequestsPM(TaskRequestPM.of(taskRequest));
    }*/

    public TaskEventListener getTaskEventListener() {
        return taskEventListener;
    }

    public void setTaskEventListener(TaskEventListener taskEventListener) {
        this.taskEventListener = taskEventListener;
    }
}
