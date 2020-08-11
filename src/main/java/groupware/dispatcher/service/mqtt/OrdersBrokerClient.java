package groupware.dispatcher.service.mqtt;

import groupware.dispatcher.service.OrderService;
import groupware.dispatcher.service.model.*;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.suback.Mqtt3SubAck;
import groupware.dispatcher.service.util.ByteBufferToStringConversion;
import groupware.dispatcher.service.util.ModelObjManager;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class OrdersBrokerClient extends BrokerClient {
    private static final String IDENTIFIER_OrderGetPublisher = "orderGetPublisher";
    private static final String IDENTIFIER_OrderSubscriber = "orderSubscriber";
    private static final String IDENTIFIER_SubscribeToNewOrders = "subscribeToNewOrders";
    Mqtt3AsyncClient orderGetPublisher;
    Mqtt3AsyncClient subscribeToNewOrders;
    Mqtt3AsyncClient orderSubscriber;
    private OrderService orderService;
    private final Logger logger;


    public OrdersBrokerClient(OrderService orderServiceImpl){
        orderService= orderServiceImpl;
        logger = LogManager.getLogManager().getLogger(String.valueOf(this.getClass()));

        orderGetPublisher = MqttClient.builder()
                .useMqttVersion3()
                .identifier(IDENTIFIER_OrderGetPublisher)
                .serverHost("127.0.0.1")
                .serverPort(1883)
                .automaticReconnectWithDefaultConfig()
                .buildAsync();
        subscribeToNewOrders = MqttClient.builder()
                .useMqttVersion3()
                .identifier(IDENTIFIER_SubscribeToNewOrders)
                .serverHost("127.0.0.1")
                .serverPort(1883)
                .automaticReconnectWithDefaultConfig()
                .buildAsync();
        orderSubscriber = MqttClient.builder()
                .useMqttVersion3()
                .identifier(IDENTIFIER_OrderSubscriber)
                .serverHost("127.0.0.1")
                .serverPort(1883)
                .automaticReconnectWithDefaultConfig()
                .buildAsync();
    }


    public void connectToBrokerAndSubscribeToNewOrders(){
        System.out.println("connecting to Broker");
        this.subscribeToNewOrders.connectWith()
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
                        System.out.println("successful connection to the broker. The client "+ IDENTIFIER_SubscribeToNewOrders + "is connected");
                        logger.info("successful connection to the broker. The client "+ IDENTIFIER_SubscribeToNewOrders + " is connected");
                    }
                });
        }


    private CompletableFuture<Mqtt3SubAck> subscribeToNewOrders(){
        String topicName = "orders/new";
        System.out.println("entering subscribeToNewOrders for the topic "+topicName);
         return this.subscribeToNewOrders.subscribeWith()
            .topicFilter("orders/new")
            .callback(mqtt3Publish -> {
                if(mqtt3Publish.getPayload().isPresent()){
                    String received= ByteBufferToStringConversion.byteBuffer2String(mqtt3Publish.getPayload().get(), StandardCharsets.UTF_8);
                    System.out.println("new order received " +received);
                   OrderDescriptiveInfo order= ModelObjManager.convertJsonToOrderDescriptiveInfo(received);
                   if(order != null) {
                       orderService.updateOrder(order.getOrderId(), order);
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
    }

    public void connectClientOrderGetPublisher() {
        this.orderGetPublisher.connectWith()
                .keepAlive(100)
                .cleanSession(true)
                .send()
                .thenAcceptAsync(connAck -> System.out.println("connected " + connAck));
    }
    public void connectAndRequestExistingOrder(String orderId){
        connectClientOrderGetPublisher();
        publishToTopic(orderGetPublisher,"orders/all_info/get/"+ orderId,null);
        System.out.println("connecting to Broker and publishing the request for the existing order "+orderId);

    }

    public void connectAndSubscribeForExistingOrder(String orderId) {
        System.out.println("connecting to Broker and subscribing for existing orders. ");
        this.orderSubscriber.connectWith()
                .keepAlive(120)
                .cleanSession(false)
                .send()
                .thenAcceptAsync(connAck -> System.out.println("connected " + connAck))
                .thenComposeAsync(v -> subscribeToGetOrderByIdResponse(orderId))
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

    private CompletableFuture<Mqtt3SubAck> subscribeToGetOrderByIdResponse(String orderId){
        String topic= "orders/all_info/get/"+ orderId + "/response";
        return this.orderSubscriber.subscribeWith()
                .topicFilter(topic)
                .callback(mqtt3Publish -> {
                    if(mqtt3Publish.getPayload().isPresent()){
                       // String orderId = mqtt3Publish.getTopic().getLevels().get(3);
                        String received = ByteBufferToStringConversion.byteBuffer2String(mqtt3Publish.getPayload().get(), StandardCharsets.UTF_8);
                        OrderDescriptiveInfo order = ModelObjManager.convertJsonToOrderDescriptiveInfo(received);
                        if (order != null) {
                            System.out.println("the order "+orderId+" has been received from the broker.");
                            orderService.updateOrder(orderId, order);
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



    public OrderService getOrderService() {
        return orderService;
    }

    public void stopClientBrokerConnection(){
        orderGetPublisher.disconnect();
        subscribeToNewOrders.disconnect();
        orderSubscriber.disconnect();
    }

    public void stopClient1BrokerConnection(){
        orderGetPublisher.disconnect();
    }
    public void stopClient2BrokerConnection(){
        subscribeToNewOrders.disconnect();
    }
    public void stopClientOrderSubscriber(){
        orderSubscriber.disconnect();
    }


}
