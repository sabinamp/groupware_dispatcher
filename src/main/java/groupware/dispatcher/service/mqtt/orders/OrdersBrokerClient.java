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
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class OrdersBrokerClient {
    private static final java.util.UUID UUID = java.util.UUID.randomUUID();
    Mqtt3AsyncClient client1;
    Mqtt3AsyncClient client2;
    private OrderService orderService;
    private final Logger logger = LogManager.getLogManager().getLogger(String.valueOf(this.getClass()));


    public OrdersBrokerClient(){
        orderService= new OrderService();
         client1 = MqttClient.builder()
                .useMqttVersion3()
                .identifier(UUID.toString())
                .serverHost("127.0.0.1")
                .serverPort(1883)
                .automaticReconnectWithDefaultConfig()
                .buildAsync();
        client2 = MqttClient.builder()
                .useMqttVersion3()
                .identifier(UUID.toString())
                .serverHost("127.0.0.1")
                .serverPort(1883)
                .automaticReconnectWithDefaultConfig()
                .buildAsync();
    }


    public void connectToBrokerAndSubscribeToNewOrders(){
        System.out.println("connecting to Broker");
        this.client2.connectWith()
                .keepAlive(60)
                .cleanSession(false)
                .willPublish()
                .topic("orders/hello")
                .qos(MqttQos.EXACTLY_ONCE)
                .payload("Hello".getBytes())
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
                    }
                });


        }


    private CompletableFuture<Mqtt3SubAck> subscribeToNewOrders(){
        System.out.println("entering subscribeToNewOrders "+"for the topic orders/new");
         CompletableFuture<Mqtt3SubAck> subscribesToNewOrders= this.client2.subscribeWith()
            .topicFilter("orders/new")
            .callback(mqtt3Publish -> {
                if(mqtt3Publish.getPayload().isPresent()){
                   OrderDescriptiveInfo order= ModelObjManager.convertJsonToOrderDescriptiveInfo(mqtt3Publish.getPayload().toString());
                    orderService.saveOrderInMemory(order.getOrderId(), order);
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


    public void connectAndRequestExistingOrders(){
        System.out.println("connecting to Broker and subscribing for existing orders");
        this.client1.connectWith()
                .keepAlive(60)
                .cleanSession(false)
                .willPublish()
                .topic("orders/get/#")
                .qos(MqttQos.EXACTLY_ONCE)
                .applyWillPublish()
                .send()
                .thenAcceptAsync(connAck -> System.out.println("connected " + connAck))

                .thenComposeAsync(v -> subscribeToGetOrderByIdResponse())

                .whenComplete((connAck, throwable) -> {
                    if (throwable != null) {
                        // Handle connection failure
                        logger.info("connectAndRequestExistingOrders - The connection to the broker failed."+ throwable.getMessage());
                        System.out.println("connectAndRequestExistingOrders - The connection to the broker failed."+ throwable.getMessage());
                    } else {
                        System.out.println("connectAndRequestExistingOrders - successful connection to the broker. The client "+ UUID + "is connected");
                        logger.info("connectAndRequestExistingOrders - successful connection to the broker. The client "+ UUID + " is connected");
                    }
                });
    }

    private CompletableFuture<Mqtt3SubAck> subscribeToGetOrderByIdResponse(){
        System.out.println("entering subscribeToNewOrders "+"for the topic orders/new");
        String topic= "orders/get/+/response";
        CompletableFuture<Mqtt3SubAck> subscribesToGetOrderByIdResponse= this.client1.subscribeWith()
                .topicFilter("orders/get/+/response")
                .callback(mqtt3Publish -> {
                    if(mqtt3Publish.getPayload().isPresent()){
                        OrderDescriptiveInfo order= ModelObjManager.convertJsonToOrderDescriptiveInfo(mqtt3Publish.getPayload().toString());
                        orderService.saveOrderInMemory(order.getOrderId(), order);
                    }
                } ).send()
                .whenComplete((mqtt3SubAck, throwable) -> {
                    if (throwable != null) {
                        // Handle failure to subscribe
                        logger.warning("Couldn't subscribe to topic "+topic);
                        System.out.println(" - could not subscribe to topic "+topic);
                    } else {
                        // Handle successful subscription, e.g. logging or incrementing a metric
                        logger.info(" - subscribed to topic " +topic);
                        System.out.println(" - subscribed to topic " +topic);
                    }
                });
        return subscribesToGetOrderByIdResponse;
    }

    private  CompletableFuture<Mqtt3Publish> publishToTopic(Mqtt3AsyncClient client,String myTopic, String  myPayload){
        CompletableFuture<Mqtt3Publish> mqtt3PublishCompletableFuture = client.publishWith()
                .topic(myTopic)
                .retain(true)
                .payload(myPayload == null? null: myPayload.getBytes())
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


    private Mqtt3Publish publishMessage(String topic, String myPayload){
        Mqtt3Publish publishMessage = Mqtt3Publish.builder()
                .topic(topic)
                .qos(MqttQos.EXACTLY_ONCE)
                .payload(myPayload == null? null: myPayload.getBytes())
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

    public void stopClient1BrokerConnection(){
        client1.disconnect();

    }
    public void stopClient2BrokerConnection(){
        client2.disconnect();
    }

}
