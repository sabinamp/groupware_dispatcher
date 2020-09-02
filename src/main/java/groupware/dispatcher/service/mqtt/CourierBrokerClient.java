package groupware.dispatcher.service.mqtt;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.lifecycle.Mqtt3ClientConnectedContext;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.suback.Mqtt3SubAck;
import com.hivemq.client.util.TypeSwitch;
import groupware.dispatcher.service.CourierServiceImpl;
import groupware.dispatcher.service.model.*;
import groupware.dispatcher.service.util.ByteBufferToStringConversion;
import groupware.dispatcher.service.util.ModelObjManager;
import groupware.dispatcher.service.util.MqttUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class CourierBrokerClient extends BrokerClient{
    private static final String IDENTIFIER_ClientCourierInfoRequestPublisher = "dispatcher_ClientCourierInfoRequestPublisher";
    private static final String IDENTIFIER_ClientCourierUpdates = "dispatcher_ClientCourierUpdates";
    private static final String IDENTIFIER_ClientCourierInfoSubscriber = "dispatcher_ClientCourierInfoSubscriber";

    private Mqtt3AsyncClient clientCourierInfoRequestPublisher;
    private Mqtt3AsyncClient clientCourierUpdates;
    private Mqtt3AsyncClient clientCourierInfoSubscriber;

    private CourierServiceImpl courierService;

    private static final Logger logger = LogManager.getLogManager().getLogger(String.valueOf(CourierBrokerClient.class));


    public CourierBrokerClient(CourierServiceImpl courierService){
        this.courierService= courierService;

        clientCourierInfoRequestPublisher = MqttClient.builder()
                .useMqttVersion3()
                .identifier(IDENTIFIER_ClientCourierInfoRequestPublisher)
                .serverHost(MqttUtils.BROKER_HIVEMQ_ADR)
                .serverPort(MqttUtils.BROKER_HIVEMQ_PORT)
               /* .sslConfig()
                .keyManagerFactory(MqttUtils.myKeyManagerFactory)
                .trustManagerFactory(MqttUtils.myTrustManagerFactory)
                .applySslConfig()*/
                .automaticReconnectWithDefaultConfig()
                .addConnectedListener(context -> {
                    TypeSwitch.when(context)
                            .is(Mqtt3ClientConnectedContext.class, context3 -> System.out.println(context3.getConnAck()));
                    //publish to couriers/info/get/courierId -for each courier
                    connectAndRequestCouriers();
                })
                .buildAsync();
        clientCourierInfoSubscriber = MqttClient.builder()
                .useMqttVersion3()
                .identifier(IDENTIFIER_ClientCourierInfoSubscriber)
                .serverHost(MqttUtils.BROKER_HIVEMQ_ADR)
                .serverPort(MqttUtils.BROKER_HIVEMQ_PORT)
              /*  .sslConfig()
                .keyManagerFactory(MqttUtils.myKeyManagerFactory)
                .trustManagerFactory(MqttUtils.myTrustManagerFactory)
                .applySslConfig()*/
                .automaticReconnectWithDefaultConfig()
                .buildAsync();
        clientCourierUpdates = MqttClient.builder()
                .useMqttVersion3()
                .identifier(IDENTIFIER_ClientCourierUpdates)
                .serverHost(MqttUtils.BROKER_HIVEMQ_ADR)
                .serverPort(MqttUtils.BROKER_HIVEMQ_PORT)
             /*   .sslConfig()
                .keyManagerFactory(MqttUtils.myKeyManagerFactory)
                .trustManagerFactory(MqttUtils.myTrustManagerFactory)
                .applySslConfig()*/
                .automaticReconnectWithDefaultConfig()
                .buildAsync();

    }

    void stopClientBrokerConnection(){
        clientCourierInfoRequestPublisher.disconnect();
        clientCourierUpdates.disconnect();
        clientCourierInfoSubscriber.disconnect();
    }



   public void connectAndRequestCourier(String courierId){
        String topicName= "couriers/info/get/" + courierId;
        System.out.println("connecting to Broker and requesting courier data, courierId: "+courierId);
        if(!this.clientCourierInfoSubscriber.getState().isConnected()){
            connectClient( this.clientCourierInfoSubscriber, 60, false);
            publishToTopic(this.clientCourierInfoSubscriber,topicName,null, true);
        }

   }

    public void connectAndRequestCouriers(){
        Set<String> courierIds= new HashSet<>();
        courierIds.add("C100");
        courierIds.add("C101");
        courierIds.add("C102");
        courierIds.add("C103");
        courierIds.add("C104");
        courierIds.add("C105");
        courierIds.add("C106");
        courierIds.add("C107");
        courierIds.forEach(courierId->{
            String topicName= "couriers/info/get/" + courierId;
            System.out.println(" requesting courier data, courierId: "+courierId);
            publishToTopic(this.clientCourierInfoSubscriber,topicName,null, true);

        });

    }

    public void connectAndSubscribeForCourierInfoResponse(){
        System.out.println("connecting to Broker and subscribing for courier info. ");
        connectClient(this.clientCourierInfoSubscriber, 80, true);
        subscribeToGetCourierById(this.clientCourierInfoSubscriber);

    }

     CompletableFuture<Mqtt3SubAck> subscribeToGetCourierById(Mqtt3AsyncClient client){
        String topicName = "couriers/info/get/+/response";
        System.out.println("entering subscribeToGetCourierById for the topic "+topicName);

        return client.subscribeWith()
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
                .whenCompleteAsync((mqtt3SubAck, throwable) -> {
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
        connectClient(this.clientCourierUpdates, 160, false);
        subscribeToCourierUpdates(this.clientCourierUpdates);
        MqttUtils.addDisconnectOnRuntimeShutDownHock(this.clientCourierUpdates);

    }


    private CompletableFuture<Mqtt3SubAck> subscribeToCourierUpdates(Mqtt3AsyncClient client){
        String topicUpdateInfo="couriers/info/update/#";
        String topicUpdateStatus="couriers/status/update/#";
        String topicUpdateConnected="couriers/conn/update/#";
        String topicUpdateAssignedOrders="couriers/assigned_orders/update/#";
        String topic = "couriers/+/update/#";
        System.out.println("entering subscribeToCourierUpdates - subscribe topic : "+topic);

       return client.subscribeWith()
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

    void subscribeToCouriers(){


        connectClient(clientCourierInfoRequestPublisher, 120, false);
        connectAndSubscribeForCourierInfoResponse();
        connectToBrokerAndSubscribeToCourierUpdates();


    }
}
