package com.objecteffects.temperature.mqtt.paho;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttSubscription;

import com.objecteffects.temperature.gui.ISensors;

public class ListenerPaho {
    private final static Logger log = LogManager.getLogger(ListenerPaho.class);

    private final int qos = 1;

    private static MqttClient client;

    private static final MemoryPersistence persistence = new MemoryPersistence();
    private final ISensors guiLayout;

    public ListenerPaho(final ISensors _guiLayout) {
        this.guiLayout = _guiLayout;
    }

    public void connect(final String broker) throws MqttException {
        try {
            log.debug("Connecting to MQTT broker: {}", broker);

            final String clientId = UUID.randomUUID().toString();

            client = new MqttClient(broker, clientId, persistence);

            final MqttConnectionOptions connOpts = new MqttConnectionOptions();
            connOpts.setCleanStart(true);
            connOpts.setAutomaticReconnect(true);

            client.connect(connOpts);

            log.debug("Connected");
        }
        catch (final MqttException me) {
            log.debug("reason: {}", Integer.valueOf(me.getReasonCode()));
            log.debug("msg: {}", me.getMessage());
            log.debug("loc: {}", me.getLocalizedMessage());
            log.debug("cause: {}", me.getCause());
            log.debug("excep: {}", me);

            for (final StackTraceElement ste : me.getStackTrace()) {
                log.warn(ste.toString());
            }

            throw me;
        }

        client.setCallback(new CallbacksPaho(client, this.guiLayout));
    }

    public void listen(final String topic) throws Exception {
        try {
            log.debug("Subscribing to topic: {}", topic);

            final MqttSubscription sub = new MqttSubscription(topic, this.qos);

            final IMqttToken token = client
                    .subscribe(new MqttSubscription[] { sub });

            log.debug("token: {}", token.getResponse());
        }
        catch (final Exception e) {
            for (final StackTraceElement ste : e.getStackTrace()) {
                log.warn(ste.toString());
            }

            throw e;
        }
    }

}
