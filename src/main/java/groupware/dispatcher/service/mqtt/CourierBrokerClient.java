package groupware.dispatcher.service.mqtt;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.Mqtt3Subscribe;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.suback.Mqtt3SubAck;
import groupware.dispatcher.service.CourierService;
import groupware.dispatcher.service.OrderService;
import groupware.dispatcher.service.model.Courier;
import groupware.dispatcher.service.model.OrderDescriptiveInfo;
import groupware.dispatcher.service.util.ModelObjManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class CourierBrokerClient extends BrokerClient{
    private static final java.util.UUID UUID = java.util.UUID.randomUUID();
    private Mqtt3AsyncClient clientA;
    private CourierService courierService;
    private final Logger logger = LogManager.getLogManager().getLogger(String.valueOf(this.getClass()));


    public CourierBrokerClient(){
        courierService= new CourierService();
        clientA = MqttClient.builder()
                .useMqttVersion3()
                .identifier(UUID.toString())
                .serverHost("127.0.0.1")
                .serverPort(1883)
                .automaticReconnectWithDefaultConfig()
                .buildAsync();

    }

    public void subscribeToCouriers(){
        List<String> courierIds= new ArrayList<>();
        courierIds.add("C100");
        courierIds.add("C101");
        courierIds.add("C102");
        courierIds.add("C103");
        courierIds.add("C104");
        courierIds.add("C105");
        courierIds.add("C106");
        courierIds.add("C107");
        for (String id: courierIds){
            connectAndRequestCourier(id);
        }

    }
    void stopClientBrokerConnection(){
        clientA.disconnect();
    }

   public void connectAndRequestCourier(String courierId){
        System.out.println("connecting to Broker and subscribing for courier "+courierId);
        this.clientA.connectWith()
                .keepAlive(30)
                .cleanSession(true)
                .willPublish()
                .topic("couriers/info/get/"+ courierId)
                .qos(MqttQos.EXACTLY_ONCE)
                .applyWillPublish()
                .send()
                .thenAcceptAsync(connAck -> System.out.println("connected " + connAck))
                .thenComposeAsync(v -> subscribeToGetCourierByIdResponse(courierId))
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

    private CompletableFuture<Mqtt3SubAck> subscribeToGetCourierByIdResponse(String courierId){
        String topicName = "couriers/info/get/"+ courierId +"/response";
        System.out.println("entering subscribeToGetCourierByIdResponse for the topic "+topicName);

        CompletableFuture<Mqtt3SubAck> subscribesToCourierById = this.clientA.subscribeWith()
                .topicFilter(topicName)
                .callback(mqtt3Publish -> {
                    if(mqtt3Publish.getPayload().isPresent()){
                        Courier courier = ModelObjManager.convertJsonToCourier(mqtt3Publish.getPayload().toString());
                        courierService.saveCourierInMemory(courierId, courier);
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


    public CourierService getOrderService() {
        return courierService;
    }

}
