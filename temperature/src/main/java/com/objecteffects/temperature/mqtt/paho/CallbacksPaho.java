package com.objecteffects.temperature.mqtt.paho;

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

import com.objecteffects.temperature.gui.ISensors;
import com.objecteffects.temperature.main.AppProperties;
import com.objecteffects.temperature.sensors.ProcessSensorData;

public class CallbacksPaho implements MqttCallback {
    private final static Logger log = LogManager.getLogger(CallbacksPaho.class);

    private final MqttClient client;
    private final ProcessSensorData process;
    private final ISensors guiLayout;

    public CallbacksPaho(final MqttClient _client,
            final ISensors _guiLayout) {
        this.client = _client;
        this.guiLayout = _guiLayout;
        this.process = new ProcessSensorData(_guiLayout);
    }

    @Override
    public void disconnected(final MqttDisconnectResponse disconnectResponse) {
        log.warn("disconnected: {}", disconnectResponse);

        if (disconnectResponse.getException() == null) {
            log.warn("no exception");

            return;
        }

        try {
            this.client.reconnect();
        }
        catch (final MqttException e) {
            for (final StackTraceElement ste : e.getStackTrace()) {
                log.warn(ste.toString());
            }

            throw new RuntimeException(e);
        }
    }

    @Override
    public void mqttErrorOccurred(final MqttException exception) {
        log.warn("error occurred: {}", exception);

        try {
            this.client.reconnect();
        }
        catch (final MqttException e) {
            for (final StackTraceElement ste : e.getStackTrace()) {
                log.warn(ste.toString());
            }

            throw new RuntimeException(e);
        }
    }

    @Override
    public void messageArrived(final String topic,
            final MqttMessage mqttMessage) throws Exception {
        final String messageTxt = new String(mqttMessage.getPayload());
        log.debug("topic: {}, message: {}", topic, messageTxt);

        this.process.processData(topic, messageTxt);

        MqttProperties props = mqttMessage.getProperties();
        final String responseTopic = props.getResponseTopic();

        if (responseTopic != null) {
            log.debug("response topic: {}", responseTopic);
            final String corrData = new String(props.getCorrelationData());

            final MqttMessage response = new MqttMessage();

            props = new MqttProperties();

            props.setCorrelationData(corrData.getBytes());
            final String content = "Got message with correlation data "
                    + corrData;
            response.setPayload(content.getBytes());

            response.setProperties(props);
            this.client.publish(responseTopic, response);
        }
    }

    @Override
    public void deliveryComplete(final IMqttToken token) {
        log.debug("delivery complete: {}", token);
    }

    @Override
    public void connectComplete(final boolean reconnect,
            final String serverURI) {
        final AppProperties props = new AppProperties();

        try {
            props.loadProperties();
        }
        catch (final IOException e) {
            for (final StackTraceElement ste : e.getStackTrace()) {
                log.warn(ste.toString());
            }

            throw new RuntimeException(e);
        }

        final ListenerPaho listener = new ListenerPaho(this.guiLayout);

        for (final String topic : props.getTopics()) {
            try {
                listener.listen(topic);
            }
            catch (final Exception e) {
                for (final StackTraceElement ste : e.getStackTrace()) {
                    log.warn(ste.toString());
                }

                throw new RuntimeException(e);
            }
        }

        log.warn("connect complete: {}", Boolean.toString(reconnect));
    }

    @Override
    public void authPacketArrived(final int reasonCode,
            final MqttProperties properties) {
        log.debug("auth packet arrived: {}", Integer.toString(reasonCode));
    }
}
