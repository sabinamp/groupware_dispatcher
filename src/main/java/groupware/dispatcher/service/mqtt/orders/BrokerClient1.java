package groupware.dispatcher.service.mqtt.orders;

import com.fasterxml.jackson.databind.deser.std.ObjectArrayDeserializer;
import groupware.dispatcher.service.OrderService;
import groupware.dispatcher.service.model.*;
import com.hivemq.client.internal.mqtt.message.MqttMessage;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.Mqtt3Subscribe;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.suback.Mqtt3SubAck;
import groupware.dispatcher.service.util.ModelObjManager;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class BrokerClient1 {
    private static final java.util.UUID UUID = java.util.UUID.randomUUID();
    Mqtt3AsyncClient client;
    private OrderService orderService;
    private final Logger logger = LogManager.getLogManager().getLogger(String.valueOf(this.getClass()));


    public BrokerClient1(){
        orderService= new OrderService();
         client = MqttClient.builder()
                .useMqttVersion3()
                .identifier(UUID.toString())
                .serverHost("127.0.0.1")
                .serverPort(1883)
                .automaticReconnectWithDefaultConfig()
                .buildAsync();

    }


    public void connectToBroker(){
        System.out.println("connecting to Broker");
        this.client.connectWith()
                .keepAlive(60)
                .cleanSession(false)
                .willPublish()
                .topic("dispatcher/hello")
                .qos(MqttQos.EXACTLY_ONCE)
                .payload("City Courier".getBytes())
                .applyWillPublish()
                .send()
                .thenAcceptAsync(connAck -> System.out.println("connected " + connAck))

                .thenComposeAsync(v -> subscribeToNewOrders())

                .whenComplete((connAck, throwable) -> {
                    if (throwable != null) {
                        // Handle connection failure
                        logger.info("The connection to the broker failed."+ throwable.getMessage());
                        System.out.println("The connection to the broker failed."+ throwable.getMessage());
                    } else {
                        System.out.println("successful connection to the broker. The client "+ UUID + "is connected");
                        logger.info("successful connection to the broker. The client "+ UUID + " is connected");
//                       client.subscribe(subscribeMessage("orders/new"));

                    }
                });


        }


    private CompletableFuture<Mqtt3SubAck> subscribeToNewOrders(){
        System.out.println("entering subscribeToNewOrders "+"for the topic orders/new");
         CompletableFuture<Mqtt3SubAck> subscribesToNewOrders= this.client.subscribeWith()
            .topicFilter("orders/new")
            .callback(mqtt3Publish -> {
                if(mqtt3Publish.getPayload().isPresent()){
                   OrderDescriptiveInfo order= ModelObjManager.convertJsonToOrderDescriptiveInfo(mqtt3Publish.getPayload().toString());
                    orderService.saveOrderInMemory(order);
                }
            } ).send()
            .whenComplete((mqtt3SubAck, throwable) -> {
                if (throwable != null) {
                    // Handle failure to subscribe
                    logger.warning("Couldn't subscribe to topic orders/new ");
                    System.out.println(" - could not subscribe to topic orders/new ");
                } else {
                    // Handle successful subscription, e.g. logging or incrementing a metric
                    logger.info(" - subscribed to topic  orders/new ");
                    System.out.println(" - subscribed to topic orders/new ");
                }
            });
        return subscribesToNewOrders;
    }



    private  CompletableFuture<Mqtt3Publish> publishToTopic(String myTopic, String  myPayload){
        CompletableFuture<Mqtt3Publish> mqtt3PublishCompletableFuture = client.publishWith()
                .topic(myTopic)
                .retain(true)
                .payload(myPayload.getBytes())
                .qos(MqttQos.EXACTLY_ONCE)
                .send()
                .whenComplete((mqtt3Publish, throwable) -> {
                    if (throwable != null) {
                        // Handle failure to publish
                        logger.info(" - failed to publish to the topic " + myTopic);
                    } else {
                        // Handle successful publish, e.g. logging or incrementing a metric
                        logger.info(" - published to the topic " + myTopic);
                    }
                });
        return  mqtt3PublishCompletableFuture;

    }


    private Mqtt3Publish publishMessage(String topic, String payload){
        Mqtt3Publish publishMessage = Mqtt3Publish.builder()
                .topic(topic)
                .qos(MqttQos.AT_LEAST_ONCE)
                .payload(payload.getBytes())
                .build();
        return publishMessage;
    }

    private Mqtt3Subscribe subscribeMessage(String myTopic){
        return Mqtt3Subscribe.builder()
                .topicFilter(myTopic)
                .qos(MqttQos.EXACTLY_ONCE)
                .build();
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void stopBrokerConnection(){
        client.disconnect();

    }

}
