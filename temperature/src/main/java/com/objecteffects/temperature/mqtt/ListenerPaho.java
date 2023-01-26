package com.objecteffects.temperature.mqtt;

import java.util.UUID;

import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttSubscription;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ListenerPaho {
    private final Logger log = LogManager.getLogger(this.getClass());

    private final int qos = 1;

    private static MemoryPersistence persistence = new MemoryPersistence();
    private static MqttClient client;

    @SuppressWarnings("boxing")
    public void connect(final String broker) throws MqttException {
        try {
            this.log.debug("Connecting to MQTT broker: {}", broker);

            final String clientId = UUID.randomUUID().toString();

            client = new MqttClient(broker, clientId, persistence);

            MqttConnectionOptions connOpts = new MqttConnectionOptions();
            connOpts.setCleanStart(true);
            connOpts.setAutomaticReconnect(true);

            client.connect(connOpts);

            this.log.debug("Connected");
        } catch (MqttException me) {
            this.log.debug("reason: {}", me.getReasonCode());
            this.log.debug("msg: {}", me.getMessage());
            this.log.debug("loc: {}", me.getLocalizedMessage());
            this.log.debug("cause: {}", me.getCause());
            this.log.debug("excep: {}", me);

            me.printStackTrace();

            throw me;
        }

        client.setCallback(new Callbacks(client));
    }

    public void listen(String topic) throws Exception {
        try {
            this.log.debug("Subscribing to topic: {}", topic);

            MqttSubscription sub = new MqttSubscription(topic, this.qos);

            IMqttToken token = client.subscribe(new MqttSubscription[] { sub });

            this.log.debug("token: {}", token.getResponse());
        } catch (Exception e) {
            e.printStackTrace();

            throw (e);
        }
    }

}
