package groupware.dispatcher.service.mqtt;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.Mqtt3Subscribe;

import java.util.concurrent.CompletableFuture;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class BrokerClient {
    private static final java.util.UUID UUID = java.util.UUID.randomUUID();
    private static final Logger logger = LogManager.getLogManager().getLogger(String.valueOf(BrokerClient.class));

    void connectClient(Mqtt3AsyncClient client, int keepAlive, boolean cleanSession){

        client.connectWith()
                .simpleAuth()
                .username("mqtt-dispatcher")
                .password("groupwaredispatcher".getBytes())
                .applySimpleAuth()
                .keepAlive(keepAlive)
                .cleanSession(cleanSession)
                .send()
                .thenAcceptAsync(connAck -> System.out.println(client.getState()+"connected " + connAck));
    }

    CompletableFuture<Mqtt3Publish> publishToTopic(Mqtt3AsyncClient client,String myTopic, String  myPayload, boolean retained){
       return client.publishWith()
                .topic(myTopic)
                .retain(retained)
                .payload(myPayload == null? null: myPayload.getBytes())
                .qos(MqttQos.EXACTLY_ONCE)
                .send()
                .whenCompleteAsync((mqtt3Publish, throwable) -> {
                    if (throwable != null) {
                        // Handle failure to publish
                        logger.info(" - failed to publish to the topic " + myTopic);
                    } else {
                        // Handle successful publish, e.g. logging or incrementing a metric
                        logger.info(" - published to the topic " + myTopic);
                    }
                });

    }


    Mqtt3Publish publishMessage(String topic, String myPayload, boolean retained){
        return Mqtt3Publish.builder()
                .topic(topic)
                .retain(retained)
                .qos(MqttQos.EXACTLY_ONCE)
                .payload(myPayload == null? null: myPayload.getBytes())
                .build();
    }

    Mqtt3Subscribe subscribeMessage(String myTopic){
        return Mqtt3Subscribe.builder()
                .topicFilter(myTopic)
                .qos(MqttQos.EXACTLY_ONCE)
                .build();
    }








}
