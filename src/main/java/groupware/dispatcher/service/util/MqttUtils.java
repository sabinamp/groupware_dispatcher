package groupware.dispatcher.service.util;

import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;

import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MqttUtils {
    public static final String BROKER_HIVEMQ_ADR = "127.0.0.1";
    public static final int BROKER_HIVEMQ_PORT = 1883;
    public static final int KEEP_ALIVE = 120;

    private static final Logger logger;
    static {
        logger = LogManager.getLogManager().getLogger(String.valueOf(MqttUtils.class));
    }
    public static void sleep(int seconds) {
        try {
            Thread.sleep(1000 * seconds);
        } catch (InterruptedException e) {
            logger.warning(e.getMessage());
        }
    }

    public static void idle(int sleepInterval) throws Exception {
        while (true) {
            sleep(sleepInterval);
            System.out.print(".");
        }
    }

    static void disconnectOnExit(Mqtt3AsyncClient client) {
        if (client != null) {
          //  logger.info("Disconnect Client " + client.getConfig().getClientIdentifier().get());
            client.disconnect();
        }
    }

    public static void addDisconnectOnRuntimeShutDownHock(Mqtt3AsyncClient client) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> disconnectOnExit(client)));
    }
}
