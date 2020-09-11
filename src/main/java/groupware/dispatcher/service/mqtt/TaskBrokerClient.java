package groupware.dispatcher.service.mqtt;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.suback.Mqtt3SubAck;
import groupware.dispatcher.service.*;

import groupware.dispatcher.service.model.RequestReply;
import groupware.dispatcher.service.model.TaskRequest;
import groupware.dispatcher.service.util.ByteBufferToStringConversion;
import groupware.dispatcher.service.util.ModelObjManager;
import groupware.dispatcher.service.util.MqttUtils;
import groupware.dispatcher.view.util.TaskEvent;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class TaskBrokerClient extends BrokerClient implements BrokerTaskRequestEventListener {
    private static final String ID_ClientTaskRequestsPublisher = "dispatcher_ClientTaskRequestsPublisher";
    private Mqtt3AsyncClient clientTaskRequestsPublisher;

    private TaskRequestServiceImpl taskRequestService;
    private static final Logger logger = LogManager.getLogManager().getLogger(String.valueOf(TaskBrokerClient.class));


    public TaskBrokerClient(TaskRequestServiceImpl taskRequestService)
    {
        this.taskRequestService = taskRequestService;
        clientTaskRequestsPublisher = MqttClient.builder()
                .useMqttVersion3()
                .identifier(ID_ClientTaskRequestsPublisher)
                .serverHost(MqttUtils.BROKER_HIVEMQ_ADR)
                .serverPort(MqttUtils.BROKER_HIVEMQ_PORT)
                /*.sslConfig()
                .keyManagerFactory(MqttUtils.myKeyManagerFactory)
                .trustManagerFactory(MqttUtils.myTrustManagerFactory)
                .applySslConfig()*/
                .automaticReconnectWithDefaultConfig()
                .buildAsync();

    }


    public void connectClientTaskRequestPublisher(){
        connectClient( this.clientTaskRequestsPublisher, 1800, false);
        MqttUtils.addDisconnectOnRuntimeShutDownHock(this.clientTaskRequestsPublisher);
    }

    private void publishTaskRequest(TaskRequest taskRequest) {
        String courierId= taskRequest.getAssigneeId();
        String taskId= taskRequest.getTaskId();
        String topicNewTaskRequestFilter="orders/"+courierId+"/"+taskId+"/request";
        publishToTopic(this.clientTaskRequestsPublisher, topicNewTaskRequestFilter,
                taskRequestService.convertToJson(taskRequest), false);
        subscribeToTaskRequestUpdates(this.clientTaskRequestsPublisher,taskRequest.getTaskId(), taskRequest);
        System.out.println("publishTaskRequest() called with the MQTT3AsyncClient"+ID_ClientTaskRequestsPublisher);
    }


    private void publishTaskRequestTimeout(TaskRequest taskRequest) {
        String courierId= taskRequest.getAssigneeId();
        String taskId= taskRequest.getTaskId();
        String timeoutTaskRequestTopicFilter="orders/"+courierId+"/"+taskId+"/timeout";
        publishToTopic(clientTaskRequestsPublisher, timeoutTaskRequestTopicFilter, null, true);
        System.out.println("publishTaskRequestTimeout() called");
    }



    private CompletableFuture<Mqtt3SubAck> subscribeToTaskRequestUpdates(Mqtt3AsyncClient client,String taskId, TaskRequest task) {
        String courierId=task.getAssigneeId();
        String topic="orders/"+courierId+"/"+taskId+"/#";
        return client.subscribeWith()
                .topicFilter(topic).qos(MqttQos.EXACTLY_ONCE)
                .callback(publish -> handleTaskUpdateEvent(new TaskEvent(TaskEvent.UPDATE), publish, taskId) )
                .send()
                .whenComplete((mqtt3SubAck, throwable) -> {
                    if (throwable != null) {
                        // Handle failure to subscribe
                        logger.warning("Couldn't subscribe to topic " + topic);
                    } else {
                        // Handle successful subscription, e.g. logging or incrementing a metric
                        logger.info(" - subscribed to topic " + topic);
                    }
                });
    }


    public TaskRequestServiceImpl getTaskRequestService() {
        return taskRequestService;
    }

    @Override
    public void handleNewTaskEvent(TaskEvent event, TaskRequest taskRequest) {
        publishTaskRequest(taskRequest);
    }

    @Override
    public void handleTimeoutTaskEvent(TaskEvent event, TaskRequest task) {
        publishTaskRequestTimeout(task);
    }

    @Override
    public void handleTaskUpdateEvent(TaskEvent event,Mqtt3Publish publish, String taskId) {
        System.out.println("handleTaskUpdateEvent() called");
        // Process the received message
        String topicEnd= publish.getTopic().getLevels().get(3);
        String assigneeID = publish.getTopic().getLevels().get(1);

        switch (topicEnd) {
            case "accept": {
                taskRequestService.updateTaskRequestReply(taskId, RequestReply.ACCEPTED,
                        "Task "+taskId +" Accepted.Topic End: "+topicEnd , assigneeID);
                System.out.println("task accepted - update received for the task request " + taskId);
                break;
            }
            case "deny": {
                taskRequestService.updateTaskRequestReply(taskId, RequestReply.DENIED,
                        "Task "+taskId +" Denied. Topic End: "+topicEnd, assigneeID);
                System.out.println(topicEnd + " update received for the task request " + taskId);
                break;
            }
            case "timeout":  break;
            case "completed": {
                System.out.println("task completed- update received for the task request " + taskId);
                if( publish.getPayload().isPresent()){
                    String receivedString= ByteBufferToStringConversion.byteBuffer2String(publish.getPayload().get(), StandardCharsets.UTF_8);
                    TaskRequest updatedTask = ModelObjManager.convertJsonToTaskRequest(receivedString);

                    taskRequestService.updateTaskRequestStatusCompleted(taskId, true,"Task "+taskId +" "+topicEnd,
                            assigneeID, updatedTask );
                }else{
                    System.out.println("task completed- but message payload missing " + taskId);
                }
                break;
            }
            case "request":
                break;
        }
    }

}
