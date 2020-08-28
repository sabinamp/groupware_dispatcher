package groupware.dispatcher.service.util;

import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MqttUtils {
    public static final String BROKER_HIVEMQ_ADR = "ssl://localhost";
    public static final int BROKER_HIVEMQ_PORT = 8883;
    public static final int KEEP_ALIVE = 120;
    public static KeyManagerFactory myKeyManagerFactory;
    private static KeyStore trustStore;
    public static TrustManagerFactory myTrustManagerFactory;

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

  
    static void disconnectOnExit(Mqtt3AsyncClient client) {
        if (client != null) {
          //  logger.info("Disconnect Client " + client.getConfig().getClientIdentifier().get());
            client.disconnect();
        }
    }

    public static void addDisconnectOnRuntimeShutDownHock(Mqtt3AsyncClient client) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> disconnectOnExit(client)));
    }

    public static void setUPSSLConfig() {
            try {
                trustStore = KeyStore.getInstance("JKS");
                KeyStore keyStoreK= KeyStore.getInstance("JKS");

                InputStream in = getInputStream("mqtt-client-trust-store.jks");
                trustStore.load(in, "groupwarestore".toCharArray());

                InputStream inKeyStore = getInputStream("hivemq.jks");
                keyStoreK.load(inKeyStore, "sep232020".toCharArray());

                myTrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                myTrustManagerFactory.init(trustStore);

                SSLContext sslCtx = SSLContext.getInstance("TLSv1.2");
                sslCtx.init(null, myTrustManagerFactory.getTrustManagers(), null);

                myKeyManagerFactory= KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                myKeyManagerFactory.init(keyStoreK, "sep232020".toCharArray());
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    private static InputStream getInputStream(String fileName) {
        return MqttUtils.class.getResourceAsStream(fileName);

    }
}
