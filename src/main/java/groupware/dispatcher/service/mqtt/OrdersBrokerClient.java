package groupware.dispatcher.service.mqtt;

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

public class OrdersBrokerClient extends BrokerClient {
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
                .payload("hello".getBytes())
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
        String topicName = "orders/new";
        System.out.println("entering subscribeToNewOrders for the topic "+topicName);
         CompletableFuture<Mqtt3SubAck> subscribesToNewOrders= this.client2.subscribeWith()
            .topicFilter("orders/new")
            .callback(mqtt3Publish -> {
                if(mqtt3Publish.getPayload().isPresent()){
                   OrderDescriptiveInfo order= ModelObjManager.convertJsonToOrderDescriptiveInfo(mqtt3Publish.getPayload().toString());
                   if(order != null) {
                       orderService.saveOrderInMemory(order.getOrderId(), order);
                   }
                }
            } ).send()
            .whenComplete((mqtt3SubAck, throwable) -> {
                if (throwable != null) {
                    // Handle failure to subscribe
                    logger.warning("Couldn't subscribe to topic  "+ topicName);
                    System.out.println(" Could not subscribe to topic "+ topicName);
                } else {
                    // Handle successful subscription, e.g. logging or incrementing a metric
                    logger.info(" - subscribed to topic "+topicName);
                }
            });
        return subscribesToNewOrders;
    }


    public void connectAndRequestExistingOrder(String orderId){
        System.out.println("connecting to Broker and subscribing for existing order "+orderId);
        this.client1.connectWith()
                .keepAlive(60)
                .cleanSession(false)
                .willPublish()
                .topic("orders/get/"+ orderId)
                .qos(MqttQos.EXACTLY_ONCE)
                .applyWillPublish()
                .send()
                .thenAcceptAsync(connAck -> System.out.println("connected " + connAck))
                .thenComposeAsync(v -> subscribeToGetOrderByIdResponse(orderId))
                .whenComplete((connAck, throwable) -> {
                    if (throwable != null) {
                        // Handle connection failure
                        logger.info("connectAndRequestExistingOrder " + orderId + " The connection to the broker failed."
                                        + throwable.getMessage());
                        System.out.println("connectAndRequestExistingOrder " + orderId + " The connection to the broker failed."+ throwable.getMessage());
                    } else {
                        System.out.println("connectAndRequestExistingOrder - successful connection to the broker. The client "+ UUID + "is connected");
                        logger.info("connectAndRequestExistingOrder - successful connection to the broker. The client "+ UUID + " is connected");
                    }
                });
    }

    private CompletableFuture<Mqtt3SubAck> subscribeToGetOrderByIdResponse(String orderId){
        String topic= "orders/get/" +orderId +"/response";
        CompletableFuture<Mqtt3SubAck> subscribesToGetOrderByIdResponse= this.client1.subscribeWith()
                .topicFilter(topic)
                .callback(mqtt3Publish -> {
                    if(mqtt3Publish.getPayload().isPresent()){
                        OrderDescriptiveInfo order= ModelObjManager.convertJsonToOrderDescriptiveInfo(mqtt3Publish.getPayload().toString());
                        if (order != null) {
                            orderService.saveOrderInMemory(order.getOrderId(), order);
                        } else {
                            logger.warning("OrderId "+orderId + " order is null");
                        }
                    }
                } ).send()
                .whenComplete((mqtt3SubAck, throwable) -> {
                    if (throwable != null) {
                        // Handle failure to subscribe
                        logger.warning("Couldn't subscribe to topic "+ topic);
                        System.out.println(" Could not subscribe to topic "+ topic);
                    } else {
                        // Handle successful subscription, e.g. logging or incrementing a metric
                        logger.info(" - subscribed to topic " + topic);
                        System.out.println(" - subscribed to topic " + topic);
                    }
                });
        return subscribesToGetOrderByIdResponse;
    }



    public OrderService getOrderService() {
        return orderService;
    }

    public void stopClientBrokerConnection(){
        client1.disconnect();
        client2.disconnect();
    }

    public void stopClient1BrokerConnection(){
        client1.disconnect();
    }
    public void stopClient2BrokerConnection(){
        client2.disconnect();
    }


}
