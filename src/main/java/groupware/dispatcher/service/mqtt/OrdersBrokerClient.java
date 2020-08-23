package groupware.dispatcher.service.mqtt;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import groupware.dispatcher.service.OrderService;
import groupware.dispatcher.service.model.*;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.suback.Mqtt3SubAck;
import groupware.dispatcher.service.util.ByteBufferToStringConversion;
import groupware.dispatcher.service.util.ModelObjManager;
import groupware.dispatcher.service.util.MqttUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class OrdersBrokerClient extends BrokerClient {
    private static final String IDENTIFIER_OrderGetPublisher = "orderGetPublisher";
    private static final String IDENTIFIER_OrderSubscriber = "orderSubscriber";
    private static final String IDENTIFIER_SubscribeToNewOrders = "subscribeToNewOrders";
    private static final String IDENTIFIER_orderConfirmationPublisher = "orderConfirmationPublisher";
    Mqtt3AsyncClient orderGetPublisher;
    Mqtt3AsyncClient subscribeToNewOrders;
    Mqtt3AsyncClient orderSubscriber;
    Mqtt3AsyncClient orderConfirmationPublisher;
    private OrderService orderService;
    private static final Logger LOGGER = LogManager.getLogManager().getLogger(String.valueOf(OrdersBrokerClient.class));

    public OrdersBrokerClient(OrderService orderServiceImpl){
        orderService= orderServiceImpl;
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
        orderConfirmationPublisher = MqttClient.builder()
                .useMqttVersion3()
                .identifier(IDENTIFIER_OrderSubscriber)
                .serverHost("127.0.0.1")
                .serverPort(1883)
                .automaticReconnectWithDefaultConfig()
                .buildAsync();
    }


    public void connectToBrokerAndSubscribeToNewOrders(){
        System.out.println("connecting to Broker");
        connectClient(this.subscribeToNewOrders, 120);
        connectClient(orderConfirmationPublisher,120);
        subscribeToNewOrders();
        MqttUtils.addDisconnectOnRuntimeShutDownHock(this.subscribeToNewOrders);
        MqttUtils.addDisconnectOnRuntimeShutDownHock(this.orderConfirmationPublisher);
    }


    private CompletableFuture<Mqtt3SubAck> subscribeToNewOrders(){
        String topicName = "orders/new";
        System.out.println("entering subscribeToNewOrders for the topic "+topicName);
         return this.subscribeToNewOrders.subscribeWith()
            .topicFilter("orders/new").qos(MqttQos.AT_LEAST_ONCE)
            .callback(mqtt3Publish -> {
                if(mqtt3Publish.getPayload().isPresent()){
                    String received= ByteBufferToStringConversion.byteBuffer2String(mqtt3Publish.getPayload().get(), StandardCharsets.UTF_8);
                    System.out.println("new order received " +received);
                   OrderDescriptiveInfo order= ModelObjManager.convertJsonToOrderDescriptiveInfo(received);
                   if(order != null) {
                       orderService.updateOrder(order.getOrderId(), order);
                       DeliveryStep startedStep = new DeliveryStep();
                       startedStep.setUpdatedWhen(LocalDateTime.now());
                       startedStep.setCurrentStatus(OrderStatus.CONFIRMED);
                       startedStep.setCurrentAssignee("C000");
                       orderService.updateOrderStatus(order.getOrderId(), startedStep);
                       //publish order confirmation
                       String topicToPublishConfirmation= "orders/status/update/"+order.getOrderId();
                       publishToTopic(subscribeToNewOrders,topicToPublishConfirmation, ModelObjManager.convertToJSON(order));
                   }
                }
            } ).send()
            .whenComplete((mqtt3SubAck, throwable) -> {
                if (throwable != null) {
                    // Handle failure to subscribe
                    LOGGER.warning(IDENTIFIER_SubscribeToNewOrders+ "Couldn't subscribe to topic  "+ topicName);
                    System.out.println(IDENTIFIER_SubscribeToNewOrders+ " Could not subscribe to topic "+ topicName);
                } else {
                    // Handle successful subscription, e.g. logging or incrementing a metric
                    OrdersBrokerClient.LOGGER.info(IDENTIFIER_SubscribeToNewOrders+ " - subscribed to topic "+topicName);
                }
            });
    }



    public void connectAndRequestExistingOrder(String orderId){
        connectClient(this.orderGetPublisher, 60);
        publishToTopic(orderGetPublisher,"orders/all_info/get/"+ orderId,null);
        System.out.println("connecting to Broker and publishing the request for the existing order "+ orderId);
        MqttUtils.addDisconnectOnRuntimeShutDownHock(orderGetPublisher);
    }

    public void connectAndSubscribeForExistingOrders() {
        connectClient(this.orderSubscriber, 60);
        System.out.println("connecting to Broker and subscribing for existing orders. ");
        subscribeToGetOrderByIdResponse();
        MqttUtils.addDisconnectOnRuntimeShutDownHock(this.orderSubscriber);

    }

    private void connectClient(Mqtt3AsyncClient client, int keepAlive) {
        client.connectWith()
                .keepAlive(keepAlive)
                .cleanSession(false)
                .send()
                .thenAcceptAsync(connAck -> System.out.println("connected " + connAck));
    }

    private CompletableFuture<Mqtt3SubAck> subscribeToGetOrderByIdResponse(){
        String topic= "orders/all_info/get/+/response";
        return this.orderSubscriber.subscribeWith()
                .topicFilter(topic)
                .qos(MqttQos.EXACTLY_ONCE)
                .callback(mqtt3Publish -> {
                    if(mqtt3Publish.getPayload().isPresent()){
                        String orderId = mqtt3Publish.getTopic().getLevels().get(3);
                        String received = ByteBufferToStringConversion.byteBuffer2String(mqtt3Publish.getPayload().get(), StandardCharsets.UTF_8);
                        OrderDescriptiveInfo order = ModelObjManager.convertJsonToOrderDescriptiveInfo(received);
                        if (order != null) {
                            System.out.println("the order "+orderId+" has been received from the broker.");
                            orderService.updateOrder(orderId, order);
                        } else {
                            System.out.println("OrderId "+orderId + " order is null");
                        }
                    }
                } ).send()
                .whenCompleteAsync((mqtt3SubAck, throwable) -> {
                    if (throwable != null) {
                        // Handle failure to subscribe
                        LOGGER.warning("Couldn't subscribe to topic "+ topic);
                        System.out.println(" Could not subscribe to topic "+ topic);
                    } else {
                        // Handle successful subscription, e.g. logging or incrementing a metric
                        LOGGER.info(" - subscribed to topic " + topic);
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

    void subscribeToOrders(){

        connectAndRequestExistingOrder("OR1111");
        connectAndRequestExistingOrder("OR1122");
        connectAndRequestExistingOrder("OR1123");
        connectAndRequestExistingOrder("OR1124");

        connectAndSubscribeForExistingOrders();
        connectToBrokerAndSubscribeToNewOrders();
    }

}
