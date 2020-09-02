package groupware.dispatcher.service.mqtt;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.lifecycle.Mqtt3ClientConnectedContext;
import com.hivemq.client.util.TypeSwitch;
import groupware.dispatcher.service.OrderService;
import groupware.dispatcher.service.model.*;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.suback.Mqtt3SubAck;
import groupware.dispatcher.service.util.ByteBufferToStringConversion;
import groupware.dispatcher.service.util.ModelObjManager;
import groupware.dispatcher.service.util.MqttUtils;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class OrdersBrokerClient extends BrokerClient {
    private static final String IDENTIFIER_OrderRequestPublisher = "dispatcher_OrderRequestPublisher";
    private static final String IDENTIFIER_OrderSubscriber = "dispatcher_OrderSubscriber";
    private static final String IDENTIFIER_SubscribeToNewOrders = "dispatcher_SubscribeToNewOrders";
    private static final String IDENTIFIER_orderStatusPublisher = "dispatcher_OrderConfirmationPublisher";
    Mqtt3AsyncClient subscribeToNewOrders;
    Mqtt3AsyncClient orderSubscriber;
    Mqtt3AsyncClient orderStatusPublisher;
    Mqtt3AsyncClient orderRequestPublisher;
    private OrderService orderService;
    private static final Logger LOGGER = LogManager.getLogManager().getLogger(String.valueOf(OrdersBrokerClient.class));

    public OrdersBrokerClient(OrderService orderServiceImpl){
        orderService= orderServiceImpl;
        orderRequestPublisher= MqttClient.builder()
                .useMqttVersion3()
                .identifier(IDENTIFIER_SubscribeToNewOrders)
                .serverHost(MqttUtils.BROKER_HIVEMQ_ADR)
                .serverPort(MqttUtils.BROKER_HIVEMQ_PORT)
                .automaticReconnectWithDefaultConfig()
                .addConnectedListener(context -> {
                    TypeSwitch.when(context).is(Mqtt3ClientConnectedContext.class, context3 -> System.out.println(context3.getConnAck()));
                    publishToTopic(this.orderRequestPublisher,"orders/all_info/get/OR1111",null, true);
                    publishToTopic(this.orderRequestPublisher,"orders/all_info/get/OR1122",null, true);
                    publishToTopic(this.orderRequestPublisher,"orders/all_info/get/OR1123",null, true);
                    publishToTopic(this.orderRequestPublisher,"orders/all_info/get/OR1124",null, true);
                })
                .buildAsync();
        subscribeToNewOrders = MqttClient.builder()
                .useMqttVersion3()
                .identifier(IDENTIFIER_SubscribeToNewOrders)
                .serverHost(MqttUtils.BROKER_HIVEMQ_ADR)
                .serverPort(MqttUtils.BROKER_HIVEMQ_PORT)
              /* .sslConfig()
                .keyManagerFactory(MqttUtils.myKeyManagerFactory)
                .trustManagerFactory(MqttUtils.myTrustManagerFactory)
                .applySslConfig()*/
                .automaticReconnectWithDefaultConfig()
                .buildAsync();
        orderSubscriber = MqttClient.builder()
                .useMqttVersion3()
                .identifier(IDENTIFIER_OrderSubscriber)
                .serverHost(MqttUtils.BROKER_HIVEMQ_ADR)
                .serverPort(MqttUtils.BROKER_HIVEMQ_PORT)
               /* .sslConfig()
                .keyManagerFactory(MqttUtils.myKeyManagerFactory)
                .trustManagerFactory(MqttUtils.myTrustManagerFactory)
                .applySslConfig()*/
                .automaticReconnectWithDefaultConfig()
                .buildAsync();
        orderStatusPublisher = MqttClient.builder()
                .useMqttVersion3()
                .identifier(IDENTIFIER_orderStatusPublisher)
                .serverHost(MqttUtils.BROKER_HIVEMQ_ADR)
                .serverPort(MqttUtils.BROKER_HIVEMQ_PORT)
              /*  .sslConfig()
                .keyManagerFactory(MqttUtils.myKeyManagerFactory)
                .trustManagerFactory(MqttUtils.myTrustManagerFactory)
                .applySslConfig()*/
                .automaticReconnectWithDefaultConfig()
                .buildAsync();
    }


    public void connectToBrokerAndSubscribeToNewOrders(){
        System.out.println("connecting to Broker");
        connectClient(this.subscribeToNewOrders, 120, true);
        subscribeToNewOrders();
        MqttUtils.addDisconnectOnRuntimeShutDownHock(this.subscribeToNewOrders);
    }


    private CompletableFuture<Mqtt3SubAck> subscribeToNewOrders(){
        String topicName = "orders/confirmed/#";
        System.out.println("entering subscribeToNewOrders for the topic "+topicName);
         return this.subscribeToNewOrders.subscribeWith()
            .topicFilter(topicName).qos(MqttQos.EXACTLY_ONCE)
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
            .whenCompleteAsync((mqtt3SubAck, throwable) -> {
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


    public void connectAndRequestExistingOrders(){
       /* if(!this.orderRequestPublisher.getState().isConnected()){
            connectClient(this.orderRequestPublisher, 60, false);
            publishToTopic(this.orderRequestPublisher,"orders/all_info/get/"+orderId,null, true);
        }*/
        connectClient(this.orderRequestPublisher, 120, false);
        System.out.println("connecting to Broker and publishing the request for existing orders. ");
        MqttUtils.addDisconnectOnRuntimeShutDownHock(orderSubscriber);
    }

    public void connectAndSubscribeForExistingOrders() {
        connectClient(this.orderSubscriber, 80, false);
        System.out.println("connecting to Broker and subscribing for existing orders. ");
        subscribeToGetOrderByIdResponse();
        MqttUtils.addDisconnectOnRuntimeShutDownHock(this.orderSubscriber);

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
        //orderGetPublisher.disconnect();
        subscribeToNewOrders.disconnect();
        orderSubscriber.disconnect();
    }


    public void stopClient2BrokerConnection(){
        subscribeToNewOrders.disconnect();
    }
    public void stopClientOrderSubscriber(){
        orderSubscriber.disconnect();
    }

    //called by BrokerConnection
    void subscribeToOrders(){
        connectAndSubscribeForExistingOrders();
        connectAndRequestExistingOrders();

        connectToBrokerAndSubscribeToNewOrders();
    }

}
