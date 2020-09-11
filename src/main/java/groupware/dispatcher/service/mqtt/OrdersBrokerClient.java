package groupware.dispatcher.service.mqtt;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.Mqtt3Client;
import groupware.dispatcher.service.OrderService;
import groupware.dispatcher.service.model.*;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.suback.Mqtt3SubAck;
import groupware.dispatcher.service.util.ByteBufferToStringConversion;
import groupware.dispatcher.service.util.ModelObjManager;
import groupware.dispatcher.service.util.MqttUtils;
import groupware.dispatcher.service.util.TimerService;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class OrdersBrokerClient extends BrokerClient {
    private static final String IDENTIFIER_OrderRequestPublisher = "dispatcher_OrderRequestPublisher";
    private static final String IDENTIFIER_OrderSubscriber = "dispatcher_OrderSubscriber";
    Mqtt3AsyncClient orderSubscriber;
    Mqtt3AsyncClient orderRequestPublisher;
    private OrderService orderService;
    private TimerService timerService;
    private static final Logger LOGGER = LogManager.getLogManager().getLogger(String.valueOf(OrdersBrokerClient.class));

    public OrdersBrokerClient(OrderService orderServiceImpl){
        orderService= orderServiceImpl;
        timerService = new TimerService();
        timerService.init();

        orderRequestPublisher= Mqtt3Client.builder()
                .identifier(IDENTIFIER_OrderRequestPublisher)
                .serverHost(MqttUtils.BROKER_HIVEMQ_ADR)
                .serverPort(MqttUtils.BROKER_HIVEMQ_PORT)
                .automaticReconnectWithDefaultConfig()
                .addConnectedListener(context -> {
                   // TypeSwitch.when(context).is(Mqtt3ClientConnectedContext.class, context3 -> System.out.println(context3.getConnAck()));
                    requestOrders(orderRequestPublisher);
                })
                .build().toAsync();

        orderSubscriber = MqttClient.builder()
                .useMqttVersion3()
                .identifier(IDENTIFIER_OrderSubscriber)
                .serverHost(MqttUtils.BROKER_HIVEMQ_ADR)
                .serverPort(MqttUtils.BROKER_HIVEMQ_PORT)
                .addConnectedListener(context -> {
                   // TypeSwitch.when(context).is(Mqtt3ClientConnectedContext.class, context3 -> System.out.println(context3.getConnAck()));
                    requestOrders(orderSubscriber);
                    subscribeToGetOrderByIdResponse();
                    subscribeToConfirmedOrders(orderSubscriber);
                })
               /* .sslConfig()
                .keyManagerFactory(MqttUtils.myKeyManagerFactory)
                .trustManagerFactory(MqttUtils.myTrustManagerFactory)
                .applySslConfig()*/
                .automaticReconnectWithDefaultConfig()
                .buildAsync();

    }

    public void requestOrders(Mqtt3AsyncClient mqtt3AsyncClient){
        Set<String> orderIds= new HashSet<>();
        orderIds.add("OR1111");
        orderIds.add("OR1122");
        orderIds.add("OR1123");
        orderIds.add("OR1124");
        orderIds.forEach(orderId->{
            String topicName= "orders/all_info/get/"+orderId;
            System.out.println(" requesting order data, orderId: "+orderId);
            publishToTopic(mqtt3AsyncClient,topicName,null, true);
        });

    }

    private CompletableFuture<Mqtt3SubAck> subscribeToConfirmedOrders(Mqtt3AsyncClient client){
        String topicName = "orders/confirmed/+/#";
        System.out.println("entering subscribeToNewOrders for the topic "+topicName);
         return client.subscribeWith()
            .topicFilter(topicName)
            .callback(mqtt3Publish -> {
                if(mqtt3Publish.getPayload().isPresent()){
                    String received= ByteBufferToStringConversion.byteBuffer2String(mqtt3Publish.getPayload().get(), StandardCharsets.UTF_8);
                    System.out.println("new order received to topic: "+topicName +received);
                   OrderDescriptiveInfo order= ModelObjManager.convertJsonToOrderDescriptiveInfo(received);
                   if(order != null) {
                       orderService.updateOrder(order.getOrderId(), order);
                   }
                }
            } ).send()
            .whenComplete((mqtt3SubAck, throwable) -> {
                if (throwable != null) {
                    // Handle failure to subscribe
                    LOGGER.warning("Couldn't subscribe to topic  "+ topicName);
                    System.out.println(" Could not subscribe to topic "+ topicName);
                } else {
                    // Handle successful subscription, e.g. logging or incrementing a metric
                    OrdersBrokerClient.LOGGER.info( " - subscribed to topic "+topicName);
                }
            });
    }


/*
    public void connectAndRequestExistingOrders(){
        System.out.println(IDENTIFIER_OrderRequestPublisher+"connecting to Broker");
        connectClient(this.orderRequestPublisher, 240, false);

        System.out.println("connecting to Broker and publishing the request for existing orders. ");
        MqttUtils.addDisconnectOnRuntimeShutDownHock(this.orderRequestPublisher);
    }
*/

    public void connectOrderSubscriber() {
       // System.out.println(IDENTIFIER_OrderSubscriber+"connecting to Broker");
        //connectClient(this.orderSubscriber, 120, true);
        Runnable timeoutTrigger=
                ()-> {
                    System.out.println(IDENTIFIER_OrderSubscriber+"connecting to Broker");
                    connectClient(this.orderSubscriber, 1800, false);
                };

        timerService.schedule(timeoutTrigger, 3500, "ID_SubscribeToExistingOrders");
        System.out.println("connecting to Broker and subscribing for existing orders. ");
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
                .whenComplete((mqtt3SubAck, throwable) -> {
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


    //called by BrokerConnection
    void subscribeToOrders(){
        connectOrderSubscriber();
    }

}
