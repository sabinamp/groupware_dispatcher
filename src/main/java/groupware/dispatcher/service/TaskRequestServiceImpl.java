package groupware.dispatcher.service;


import groupware.dispatcher.presentationmodel.AllTaskRequestsPM;
import groupware.dispatcher.presentationmodel.TaskRequestPM;
import groupware.dispatcher.service.model.TaskRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class TaskRequestServiceImpl {
    private final Logger logger;
    private static Map<String, TaskRequest> tasks;

    private OrderServiceImpl orderService;
    private CourierServiceImpl courierService;

    private AllTaskRequestsPM allTaskRequestsPM;
    static{
        tasks= new HashMap<>();
    }

    public TaskRequestServiceImpl(OrderServiceImpl orderService, CourierServiceImpl courierService){
        logger = LogManager.getLogManager().getLogger(String.valueOf(TaskRequestServiceImpl.class));
        this.orderService = orderService;
        this.courierService = courierService;
        this.allTaskRequestsPM = new AllTaskRequestsPM(orderService.getAllOrdersPM(), courierService.getAllCouriersPM());
    }

      public TaskRequest getTaskRequestById(String taskId){
            return tasks.get(taskId);
      }

    public boolean updateTaskRequest(String id, TaskRequest taskRequest) {
        if(taskRequest == null) {
            logger.info("updateTask() received arg taskRequest - null");
            return false;
        }else {
            tasks.put(id, taskRequest);
            allTaskRequestsPM.updateAllTaskRequestsPM(TaskRequestPM.of(taskRequest));
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


    public AllTaskRequestsPM getAllTaskRequestsPM() {
        return allTaskRequestsPM;
    }

}
