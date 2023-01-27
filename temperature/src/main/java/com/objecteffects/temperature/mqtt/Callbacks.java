package com.objecteffects.temperature.mqtt;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;

import com.objecteffects.temperature.gui.SensorsLayout;
import com.objecteffects.temperature.main.AppProperties;

public class Callbacks implements MqttCallback {
    private final Logger log = LogManager.getLogger(this.getClass());

    private final MqttClient client;
    private final ProcessSensorData process;
    private final SensorsLayout guiLayout;

    public Callbacks(final MqttClient _client, final SensorsLayout _guiLayout) {
        this.client = _client;
        this.guiLayout = _guiLayout;
        this.process = new ProcessSensorData(_guiLayout);
    }

    @Override
    public void disconnected(final MqttDisconnectResponse disconnectResponse) {
        this.log.warn("disconnected: {}", disconnectResponse);

        if (disconnectResponse.getException() == null) {
            this.log.warn("no exception");

            return;
        }

        try {
            this.client.reconnect();
        } catch (final MqttException e) {
            e.printStackTrace();

            throw new RuntimeException(e);
        }
    }

    @Override
    public void mqttErrorOccurred(final MqttException exception) {
        this.log.warn("error occurred: {}", exception);

        try {
            this.client.reconnect();
        } catch (final MqttException e) {
            e.printStackTrace();

            throw new RuntimeException(e);
        }
    }

    @Override
    public void messageArrived(final String topic, final MqttMessage mqttMessage) throws Exception {
        final String messageTxt = new String(mqttMessage.getPayload());
        this.log.debug("topic: {}, message: {}", topic, messageTxt);

        this.process.processData(topic, messageTxt);

        MqttProperties props = mqttMessage.getProperties();
        final String responseTopic = props.getResponseTopic();

        if (responseTopic != null) {
            this.log.debug("response topic: {}", responseTopic);
            final String corrData = new String(props.getCorrelationData());

            final MqttMessage response = new MqttMessage();

            props = new MqttProperties();

            props.setCorrelationData(corrData.getBytes());
            final String content = "Got message with correlation data " + corrData;
            response.setPayload(content.getBytes());

            response.setProperties(props);
            this.client.publish(responseTopic, response);
        }
    }

    @Override
    public void deliveryComplete(final IMqttToken token) {
        this.log.debug("delivery complete: {}", token);
    }

    @SuppressWarnings("boxing")
    @Override
    public void connectComplete(final boolean reconnect, final String serverURI) {
        final AppProperties props = new AppProperties();

        try {
            props.loadProperties();
        } catch (final IOException e) {
            e.printStackTrace();

            throw new RuntimeException(e);
        }

        final ListenerPaho listenerPaho = new ListenerPaho(this.guiLayout);

        for (final String topic : props.getTopics()) {
            try {
                listenerPaho.listen(topic);
            } catch (final Exception e) {
                e.printStackTrace();

                throw new RuntimeException(e);
            }
        }

        this.log.info("connect complete: {}", reconnect);
    }

    @SuppressWarnings("boxing")
    @Override
    public void authPacketArrived(final int reasonCode, final MqttProperties properties) {
        this.log.debug("auth packet arrived: {}", reasonCode);
    }
}
