package groupware.dispatcher.service.mqtt;

import groupware.dispatcher.presentationmodel.AllOrdersPM;
import groupware.dispatcher.service.OrderServiceImpl;
import groupware.dispatcher.service.model.*;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.suback.Mqtt3SubAck;
import groupware.dispatcher.service.util.ByteBufferToStringConversion;
import groupware.dispatcher.service.util.ModelObjManager;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class OrdersBrokerClient extends BrokerClient {
    private static final java.util.UUID UUID = java.util.UUID.randomUUID();
    Mqtt3AsyncClient client1;
    Mqtt3AsyncClient client2;
    Mqtt3AsyncClient orderSubscriber;
    private OrderServiceImpl orderService;
    private final Logger logger = LogManager.getLogManager().getLogger(String.valueOf(this.getClass()));
    private AllOrdersPM allOrdersPM;

    public OrdersBrokerClient(){
        orderService= new OrderServiceImpl();
        allOrdersPM = new AllOrdersPM(orderService);
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
        orderSubscriber = MqttClient.builder()
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
                .keepAlive(180)
                .cleanSession(false)
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
                    String received= ByteBufferToStringConversion.byteBuffer2String(mqtt3Publish.getPayload().get(), StandardCharsets.UTF_8);
                    System.out.println("new order received " +received);
                   OrderDescriptiveInfo order= ModelObjManager.convertJsonToOrderDescriptiveInfo(received);
                   if(order != null) {
                       OrderServiceImpl.saveOrderInMemory(order.getOrderId(), order);
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
        System.out.println("connecting to Broker and publishing the request for the existing order "+orderId);
        this.client1.connectWith()
                .keepAlive(30)
                .cleanSession(false)
                .willPublish()
                .topic("orders/get/"+ orderId)
                .qos(MqttQos.EXACTLY_ONCE)
                .applyWillPublish()
                .send()
                .thenAcceptAsync(connAck -> System.out.println("connected " + connAck))
                //.thenComposeAsync(v -> subscribeToGetOrderByIdResponse(orderId))
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
    public void connectAndSubscribeForExistingOrder() {
        System.out.println("connecting to Broker and subscribing for existing order. ");
        this.orderSubscriber.connectWith()
                .keepAlive(120)
                .cleanSession(false)
                .send()
                .thenAcceptAsync(connAck -> System.out.println("connected " + connAck))
                .thenComposeAsync(v -> subscribeToGetOrderByIdResponse())
                .whenComplete((connAck, throwable) -> {
                    if (throwable != null) {
                        // Handle connection failure
                        logger.info("connectAndSubscribeForExistingOrder. The connection to the broker failed."
                                + throwable.getMessage());
                        System.out.println("connectAndSubscribeForExistingOrder. The connection to the broker failed."+ throwable.getMessage());
                    } else {
                        System.out.println("connectAndSubscribeForExistingOrder - successful connection to the broker. The client orderSubscriber is connected");
                        logger.info("connectAndSubscribeForExistingOrder - successful connection to the broker. The client orderSubscriber is connected");
                    }
                });
    }

    private CompletableFuture<Mqtt3SubAck> subscribeToGetOrderByIdResponse(){
        String topic= "orders/get/+/response";
        return this.client1.subscribeWith()
                .topicFilter(topic)
                .callback(mqtt3Publish -> {
                    if(mqtt3Publish.getPayload().isPresent()){
                        String orderId = mqtt3Publish.getTopic().getLevels().get(2);
                        String received= ByteBufferToStringConversion.byteBuffer2String(mqtt3Publish.getPayload().get(), StandardCharsets.UTF_8);

                        OrderDescriptiveInfo order= ModelObjManager.convertJsonToOrderDescriptiveInfo(received);
                        if (order != null) {
                            System.out.println("an order has been received: " +orderId);
                            OrderServiceImpl.saveOrderInMemory(orderId, order);
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

    }



    public OrderServiceImpl getOrderService() {
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
