package groupware.dispatcher.service.mqtt;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.suback.Mqtt3SubAck;
import groupware.dispatcher.presentationmodel.AllCouriersPM;
import groupware.dispatcher.presentationmodel.CourierPM;
import groupware.dispatcher.service.CourierService;
import groupware.dispatcher.service.CourierServiceImpl;
import groupware.dispatcher.service.TaskRequestServiceImpl;
import groupware.dispatcher.service.model.*;
import groupware.dispatcher.service.util.ByteBufferToStringConversion;
import groupware.dispatcher.service.util.ModelObjManager;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class CourierBrokerClient extends BrokerClient{
    private static final java.util.UUID IDENTIFIER = UUID.randomUUID();
    private Mqtt3AsyncClient clientCourierInfo;
    private Mqtt3AsyncClient clientCourierUpdates;
    private Mqtt3AsyncClient clientCourierInfoSubscriber;
    private Mqtt3AsyncClient clientTaskRequestsPublisher;
    private Mqtt3AsyncClient clientTaskSubscriber;
    private CourierServiceImpl courierService;

    private static final Logger logger = LogManager.getLogManager().getLogger(String.valueOf(CourierBrokerClient.class));


    public CourierBrokerClient(CourierServiceImpl courierService){
        this.courierService= courierService;

        clientCourierInfo = MqttClient.builder()
                .useMqttVersion3()
                .identifier(IDENTIFIER.toString())
                .serverHost("127.0.0.1")
                .serverPort(1883)
                .automaticReconnectWithDefaultConfig()
                .buildAsync();
        clientCourierInfoSubscriber = MqttClient.builder()
                .useMqttVersion3()
                .identifier(UUID.randomUUID().toString())
                .serverHost("127.0.0.1")
                .serverPort(1883)
                .automaticReconnectWithDefaultConfig()
                .buildAsync();
        clientCourierUpdates = MqttClient.builder()
                .useMqttVersion3()
                .identifier(UUID.randomUUID().toString())
                .serverHost("127.0.0.1")
                .serverPort(1883)
                .automaticReconnectWithDefaultConfig()
                .buildAsync();

    }

    void stopClientBrokerConnection(){
        clientCourierInfo.disconnect();
        clientCourierUpdates.disconnect();
        clientCourierInfoSubscriber.disconnect();
    }


   public void connectAndRequestCourier(String courierId){
        String topicName= "couriers/info/get/" + courierId;
        System.out.println("connecting to Broker and subscribing for courier "+courierId);
        this.clientCourierInfo.connectWith()
                .keepAlive(80)
                .cleanSession(false)
               /* .willPublish()
                .topic("couriers/info/get/" + courierId)
                .qos(MqttQos.EXACTLY_ONCE)
                .applyWillPublish()*/
                .send()
                .thenAcceptAsync(connAck -> System.out.println("connected " + connAck))
                .thenComposeAsync(v -> publishToTopic(clientCourierInfo,topicName,null))
                .whenComplete((connAck, throwable) -> {
                    if (throwable != null) {
                        // Handle connection failure
                        logger.info("connectAndRequestCourier " + courierId + " The connection to the broker failed."
                                        + throwable.getMessage());
                        System.out.println("connectAndRequestCourier " + courierId + " The connection to the broker failed."+ throwable.getMessage());
                    } else {
                        System.out.println(" - successful connection to the broker. The client clientCourierInfo is connected");
                        logger.info(" - successful connection to the broker. The client clientCourierInfo is connected");

                    }
                });
    }


    public void connectAndSubscribeForCourierInfoResponse(){
        System.out.println("connecting to Broker and subscribing for courier info. ");
        this.clientCourierInfoSubscriber.connectWith()
                .keepAlive(80)
                .cleanSession(false)
                .send()
                .thenAcceptAsync(connAck -> System.out.println("connected " + connAck))
                .thenComposeAsync(v -> subscribeToGetCourierById())
                .whenComplete((connAck, throwable) -> {
                    if (throwable != null) {
                        // Handle connection failure
                        logger.info("connectAndSubscribeForCourierInfo. The connection to the broker failed."
                                + throwable.getMessage());
                        System.out.println("connectAndSubscribeForCourierInfo. The connection to the broker failed."+ throwable.getMessage());
                    } else {
                        System.out.println("connectAndSubscribeForCourierInfo - successful connection to the broker. The client clientCourierInfo is connected");
                        logger.info("connectAndSubscribeForCourierInfo - successful connection to the broker. The client clientCourierInfo is connected");
                        //clientCourierInfoSubscriber.unsubscribeWith().topicFilter( "couriers/info/get/#").send();
                    }

                });
    }

     CompletableFuture<Mqtt3SubAck> subscribeToGetCourierById(){
        String topicName = "couriers/info/get/+/response";
        System.out.println("entering subscribeToGetCourierById for the topic "+topicName);

        return this.clientCourierInfoSubscriber.subscribeWith()
                .topicFilter(topicName)
                .qos(MqttQos.EXACTLY_ONCE)
                .callback(mqtt3Publish -> {
                    if(mqtt3Publish.getPayload().isPresent()){
                        String courierId= mqtt3Publish.getTopic().getLevels().get(3);
                        System.out.println("response related to the courier "+ courierId);
                        String received= ByteBufferToStringConversion.byteBuffer2String(mqtt3Publish.getPayload().get(), StandardCharsets.UTF_8);

                        if( !received.isEmpty()){
                            System.out.println("the courier info received as json "+received);
                            CourierInfo courier = ModelObjManager.convertJsonToCourierInfo(received);
                            if(courier != null){
                                courierService.saveCourier(courierId, courier);
                            }else{
                                System.out.println("the converted courier object is null");
                            }
                        }
                        else{
                            System.out.println("the courier info received as json is null");
                        }
                    }
                } ).send()
                .whenComplete((mqtt3SubAck, throwable) -> {
                    if (throwable != null) {
                        // Handle failure to subscribe
                        logger.warning("Couldn't subscribe to topic " + topicName);
                        System.out.println(" Could not subscribe to topic "+ topicName);
                    } else {
                        // Handle successful subscription, e.g. logging or incrementing a metric
                        logger.info(" - subscribed to topic "+ topicName);
                    }
                });

    }
    public void connectToBrokerAndSubscribeToCourierUpdates(){
        System.out.println("connecting to Broker connectToBrokerAndSubscribeToCourierUpdates");
        this.clientCourierUpdates.connectWith()
                .keepAlive(160)
                .cleanSession(false)
                .send()
                .thenAcceptAsync(connAck -> System.out.println("connected " + connAck))
                .thenComposeAsync(v -> subscribeToCourierUpdates())
                .whenComplete((connAck, throwable) -> {
                    if (throwable != null) {
                        // Handle connection failure
                        logger.info("The connection to the broker failed."+ throwable.getMessage());
                        System.out.println("The connection to the broker failed."+ throwable.getMessage());
                    } else {
                        System.out.println("successful connection to the broker. The client clientCourierUpdates is connected");
                        logger.info("successful connection to the broker. The client clientCourierUpdates is connected");

                    }
                });
    }


    private CompletableFuture<Mqtt3SubAck> subscribeToCourierUpdates(){
        String topicUpdateInfo="couriers/info/update/#";
        String topicUpdateStatus="couriers/status/update/#";
        String topicUpdateConnected="couriers/conn/update/#";
        String topicUpdateAssignedOrders="couriers/assigned_orders/update/#";
        String topic = "couriers/+/update/#";
        System.out.println("entering subscribeToCourierUpdates - subscribe topic : "+topic);

       return clientCourierUpdates.subscribeWith()
                .topicFilter(topic)
                .qos(MqttQos.EXACTLY_ONCE)
                .callback(publish -> {
                    // Process the received message
                    if( publish.getPayload().isPresent()){
                        String courierId= publish.getTopic().getLevels().get(3);
                        System.out.println("update received for the courier "+ courierId);

                        String tobeUpdated= publish.getTopic().getLevels().get(1);
                        String receivedString= ByteBufferToStringConversion.byteBuffer2String(publish.getPayload().get(), StandardCharsets.UTF_8);
                        switch (tobeUpdated) {
                            case "status":
                                courierService.setStatus(courierId, CourierStatus.fromValue(receivedString));
                                System.out.println("update received for the courier " + receivedString);
                                break;
                            case "conn":
                                courierService.setConn(courierId, Conn.fromValue(receivedString));
                                System.out.println("update received for the courier " + receivedString);
                                break;
                            case "assigned_orders":
                                courierService.updateAssignedOrders(courierId, receivedString);
                                System.out.println("update received for the courier " + receivedString);
                                break;
                            case "info":
                                courierService.updateCourierInfo(courierId, ModelObjManager.convertJsonToCourierInfo(receivedString));
                                System.out.println("update received for the courier " + receivedString);
                                break;
                        }
                    }
                })
                .send()
                .whenComplete((mqtt3SubAck, throwable) -> {
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

}
