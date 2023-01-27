package com.objecteffects.temperature.mqtt;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttSubscription;

import com.google.inject.Inject;
import com.objecteffects.temperature.gui.SensorsLayout;

public class ListenerPaho {
    private final Logger log = LogManager.getLogger(this.getClass());

    private final int qos = 1;

    private MqttClient client;

    private final MemoryPersistence persistence = new MemoryPersistence();
    private final SensorsLayout guiLayout;

    @Inject
    public ListenerPaho(final SensorsLayout _guiLayout) {
        this.guiLayout = _guiLayout;
    }

    @SuppressWarnings("boxing")
    public void connect(final String broker) throws MqttException {
        try {
            this.log.debug("Connecting to MQTT broker: {}", broker);

            final String clientId = UUID.randomUUID().toString();

            this.client = new MqttClient(broker, clientId, this.persistence);

            final MqttConnectionOptions connOpts = new MqttConnectionOptions();
            connOpts.setCleanStart(true);
            connOpts.setAutomaticReconnect(true);

            this.client.connect(connOpts);

            this.log.debug("Connected");
        } catch (final MqttException me) {
            this.log.debug("reason: {}", me.getReasonCode());
            this.log.debug("msg: {}", me.getMessage());
            this.log.debug("loc: {}", me.getLocalizedMessage());
            this.log.debug("cause: {}", me.getCause());
            this.log.debug("excep: {}", me);

            me.printStackTrace();

            throw me;
        }

        this.client.setCallback(new Callbacks(this.client, this.guiLayout));
    }

    public void listen(final String topic) throws Exception {
        try {
            this.log.debug("Subscribing to topic: {}", topic);

            final MqttSubscription sub = new MqttSubscription(topic, this.qos);

            final IMqttToken token = this.client.subscribe(new MqttSubscription[] { sub });

            this.log.debug("token: {}", token.getResponse());
        } catch (final Exception e) {
            e.printStackTrace();

            throw e;
        }
    }

}
