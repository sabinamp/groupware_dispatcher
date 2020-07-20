package groupware.dispatcher.service.mqtt;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.suback.Mqtt3SubAck;
import groupware.dispatcher.service.CourierServiceImpl;
import groupware.dispatcher.service.model.Conn;
import groupware.dispatcher.service.model.Courier;
import groupware.dispatcher.service.model.CourierStatus;
import groupware.dispatcher.service.util.ByteBufferToStringConversion;
import groupware.dispatcher.service.util.ModelObjManager;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class CourierBrokerClient extends BrokerClient{
    private static final java.util.UUID UUID = java.util.UUID.randomUUID();
    private Mqtt3AsyncClient clientA;
    private Mqtt3AsyncClient clientCourierUpdates;
    private CourierServiceImpl courierService;
    private static final Logger logger = LogManager.getLogManager().getLogger(String.valueOf(CourierBrokerClient.class));



    private List<String> courierIds;

    public CourierBrokerClient(){

        courierService= new CourierServiceImpl();
        clientA = MqttClient.builder()
                .useMqttVersion3()
                .identifier(UUID.toString())
                .serverHost("127.0.0.1")
                .serverPort(1883)
                .automaticReconnectWithDefaultConfig()
                .buildAsync();
        clientCourierUpdates = MqttClient.builder()
                .useMqttVersion3()
                .identifier(UUID.toString())
                .serverHost("127.0.0.1")
                .serverPort(1883)
                .automaticReconnectWithDefaultConfig()
                .buildAsync();
        courierIds= new ArrayList<>();
        courierIds.add("C100");
        courierIds.add("C101");
        courierIds.add("C102");
        courierIds.add("C103");
        courierIds.add("C104");
        courierIds.add("C105");
        courierIds.add("C106");
        courierIds.add("C107");
    }

    public void subscribeToCouriers(){

        for (String id: this.getCourierIds()){
            connectAndRequestCourier(id);
        }

    }
    public List<String> getCourierIds() {
        return courierIds;
    }
    void stopClientBrokerConnection(){
        clientA.disconnect();
        clientCourierUpdates.disconnect();
    }

   public void connectAndRequestCourier(String courierId){
        System.out.println("connecting to Broker and subscribing for courier "+courierId);
        this.clientA.connectWith()
                .keepAlive(120)
                .cleanSession(false)
                .willPublish()
                .topic("couriers/info/get/"+ courierId)
                .qos(MqttQos.EXACTLY_ONCE)
                .applyWillPublish()
                .send()
                .thenAcceptAsync(connAck -> System.out.println("connected " + connAck))
                .thenComposeAsync(v -> subscribeToGetCourierById(courierId))
                .whenComplete((connAck, throwable) -> {
                    if (throwable != null) {
                        // Handle connection failure
                        logger.info("connectAndRequestCourier " + courierId + " The connection to the broker failed."
                                        + throwable.getMessage());
                        System.out.println("connectAndRequestCourier " + courierId + " The connection to the broker failed."+ throwable.getMessage());
                    } else {
                        System.out.println(" - successful connection to the broker. The client "+ UUID + "is connected");
                        logger.info(" - successful connection to the broker. The client "+ UUID + " is connected");
                    }
                });
    }

    private CompletableFuture<Mqtt3SubAck> subscribeToGetCourierById(String courierId){
        String topicName = "couriers/info/get/"+ courierId +"/response";
        System.out.println("entering subscribeToGetCourierByIdResponse for the topic "+topicName);

        CompletableFuture<Mqtt3SubAck> subscribesToCourierById = this.clientA.subscribeWith()
                .topicFilter(topicName)
                .callback(mqtt3Publish -> {
                    if(mqtt3Publish.getPayload().isPresent()){
                        String received= ByteBufferToStringConversion.byteBuffer2String(mqtt3Publish.getPayload().get(), StandardCharsets.UTF_8);
                        Courier courier = ModelObjManager.convertJsonToCourier(received);
                        System.out.print(received);
                        courierService.updateCourier(courierId, courier);
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
        return subscribesToCourierById;
    }
    public void connectToBrokerAndSubscribeToCourierUpdates(){
        System.out.println("connecting to Broker connectToBrokerAndSubscribeToCourierUpdates");
        this.clientCourierUpdates.connectWith()
                .keepAlive(180)
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
                        logger.info("successful connection to the broker. The client "+ clientCourierUpdates + " is connected");

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

        CompletableFuture<Mqtt3SubAck> mqtt3SubAckCompletableFuture= clientCourierUpdates.subscribeWith()
                .topicFilter(topic)
                .callback(publish -> {
                    // Process the received message
                    if( publish.getPayload().isPresent()){
                        String courierId= publish.getTopic().getLevels().get(3);
                        System.out.println("update received for the courier "+ courierId);

                        String tobeUpdated= publish.getTopic().getLevels().get(1);
                        String receivedString= ByteBufferToStringConversion.byteBuffer2String(publish.getPayload().get(), StandardCharsets.UTF_8);
                        if (tobeUpdated.equals("status")){
                            courierService.setStatus(courierId, CourierStatus.fromValue(receivedString));
                            System.out.println("update received for the courier "+ receivedString);

                        }else if(tobeUpdated.equals("conn")){
                            courierService.setConn(courierId, Conn.fromValue(receivedString));
                            System.out.println("update received for the courier "+ receivedString);
                        }else if(tobeUpdated.equals("assigned_orders")){
                            courierService.updateAssignedOrders(courierId, receivedString);
                            System.out.println("update received for the courier "+ receivedString);
                        }else if(tobeUpdated.equals("info")){
                            courierService.updateCourierInfo(courierId, ModelObjManager.convertJsonToCourierInfo(receivedString));
                            System.out.println("update received for the courier "+ receivedString);
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

        return mqtt3SubAckCompletableFuture;
    }
    public CourierServiceImpl getOrderService() {
        return courierService;
    }

}
