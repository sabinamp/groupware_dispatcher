package groupware.dispatcher.service.mqtt;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.suback.Mqtt3SubAck;
import groupware.dispatcher.presentationmodel.TaskRequestPM;
import groupware.dispatcher.service.CourierServiceImpl;
import groupware.dispatcher.service.TaskRequestEventListener;
import groupware.dispatcher.service.TaskRequestPMEventListener;
import groupware.dispatcher.service.TaskRequestServiceImpl;
import groupware.dispatcher.service.model.TaskRequest;
import groupware.dispatcher.service.util.ByteBufferToStringConversion;
import groupware.dispatcher.service.util.ModelObjManager;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class TaskBrokerClient extends BrokerClient implements TaskRequestEventListener {
    private static final java.util.UUID IDENTIFIER = UUID.randomUUID();

    private Mqtt3AsyncClient clientTaskRequestsPublisher;
    private Mqtt3AsyncClient clientTaskSubscriber;
    private CourierServiceImpl courierService;

    private TaskRequestServiceImpl taskRequestService;
    private static final Logger logger = LogManager.getLogManager().getLogger(String.valueOf(TaskBrokerClient.class));


    public TaskBrokerClient(CourierServiceImpl courierService, TaskRequestServiceImpl taskRequestService)
    {
        this.courierService= courierService;
        this.taskRequestService = taskRequestService;

        clientTaskRequestsPublisher = MqttClient.builder()
                .useMqttVersion3()
                .identifier(UUID.randomUUID().toString())
                .serverHost("127.0.0.1")
                .serverPort(1883)
                .automaticReconnectWithDefaultConfig()
                .buildAsync();
        clientTaskSubscriber = MqttClient.builder().useMqttVersion3()
                .identifier(UUID.randomUUID().toString())
                .serverHost("127.0.0.1")
                .serverPort(1883)
                .automaticReconnectWithDefaultConfig()
                .buildAsync();
    }

    void stopClientBrokerConnection(){
        clientTaskSubscriber.disconnect();
        clientTaskRequestsPublisher.disconnect();
    }

    private void connectClientTaskRequestsPublisher() {
        this.clientTaskRequestsPublisher.connectWith()
                .keepAlive(180)
                .cleanSession(true)
                .send()
                .thenAcceptAsync(connAck -> System.out.println("connected " + connAck));
    }

    private void connectPublishTaskRequest(TaskRequest taskRequest) {
        String courierId= taskRequest.getAssigneeId();
        String taskId= taskRequest.getTaskId();
        String topicNewTaskRequestFilter="orders/"+courierId+"/"+taskId+"/request";

        connectClientTaskRequestsPublisher();
        publishToTopic(clientTaskRequestsPublisher, topicNewTaskRequestFilter,
                        taskRequestService.convertToJson(taskRequest));
        System.out.println("connectPublishTaskRequest() called");

    }




    private void connectToBrokerAndSubscribeToTaskUpdates(String taskId, TaskRequest taskRequest){
        System.out.println("connecting to Broker connectToBrokerAndSubscribeToTaskUpdates");
        this.clientTaskSubscriber.connectWith()
                .keepAlive(120)
                .cleanSession(false)
                .send()
                .thenAcceptAsync(connAck -> System.out.println("connected " + connAck))
                .thenComposeAsync(v -> subscribeToTaskRequestUpdates(taskId, taskRequest))
                .whenCompleteAsync((connAck, throwable) -> {
                    if (throwable != null) {
                        // Handle connection failure
                        logger.info("The connection to the broker failed."+ throwable.getMessage());
                        System.out.println("The connection to the broker failed."+ throwable.getMessage());
                    } else {
                        System.out.println("successful connection to the broker. The client clientTaskSubscriber is connected");
                        logger.info("successful connection to the broker. The client clientTaskSubscriber is connected");

                    }
                });
    }


    private CompletableFuture<Mqtt3SubAck> subscribeToTaskRequestUpdates(String taskId, TaskRequest task) {
        String courierId=task.getAssigneeId();
        String topic="orders/"+courierId+"/"+taskId+"/#";
        return clientTaskSubscriber.subscribeWith()
                .topicFilter(topic)
                .qos(MqttQos.EXACTLY_ONCE)
                .callback(publish -> {
                    // Process the received message
                    String topicEnd= publish.getTopic().getLevels().get(3);
                    switch (topicEnd) {
                        case "accept":
                            taskRequestService.updateTaskRequestAccepted(taskId, true);
                            System.out.println("task accepted - update received for the task request " + taskId);
                            break;
                        case "deny":
                            taskRequestService.updateTaskRequestAccepted(taskId, false);
                            System.out.println("update received for the task request " + taskId);
                            break;
                        case "timeout":
                            taskRequestService.updateTaskRequestAccepted(taskId, false);
                            System.out.println("task timed out - update received for the task request " + taskId);
                            break;
                        case "completed":
                            taskRequestService.updateTaskRequestDone(taskId, true);
                            System.out.println("task completed- update received for the task request " + taskId);
                            break;
                    }
                   if( publish.getPayload().isPresent()){
                        System.out.println("update received for the task request sent to "+ taskId);
                        String receivedString= ByteBufferToStringConversion.byteBuffer2String(publish.getPayload().get(), StandardCharsets.UTF_8);
                        taskRequestService.updateAllTaskRequestPM(taskId, ModelObjManager.convertJsonToTaskRequest(receivedString));
                    }
                })
                .send()
                .whenCompleteAsync((mqtt3SubAck, throwable) -> {
                    if (throwable != null) {
                        // Handle failure to subscribe
                        logger.warning("Couldn't subscribe to topic " + topic);
                        System.out.println(" - could not subscribe to topic " + topic);
                    } else {
                        // Handle successful subscription, e.g. logging or incrementing a metric
                        logger.info(" - subscribed to topic " + topic);
                        System.out.println(" - subscribed to topic " + topic);
                    }
                });
    }

    public CourierServiceImpl getOrderService() {
        return courierService;
    }

    public TaskRequestServiceImpl getTaskRequestService() {
        return taskRequestService;
    }

    @Override
    public void handleNewTaskEvent(TaskRequest taskRequest) {
        connectPublishTaskRequest(taskRequest);
        connectToBrokerAndSubscribeToTaskUpdates(taskRequest.getTaskId(), taskRequest);
    }

    @Override
    public void handleTaskUpdateEvent(TaskRequest taskRequest) {

    }
}
