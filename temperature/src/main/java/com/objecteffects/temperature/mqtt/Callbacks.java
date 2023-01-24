package com.objecteffects.temperature.mqtt;

import java.io.IOException;

import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.objecteffects.temperature.main.AppProperties;

public class Callbacks implements MqttCallback {
    private final Logger log = LogManager.getLogger(this.getClass());

    private final MqttClient client;
    private final ProcessSensorData process;

    public Callbacks(MqttClient _client) {
        this.client = _client;
        this.process = new ProcessSensorData();
    }

    @Override
    public void disconnected(MqttDisconnectResponse disconnectResponse) {
        this.log.warn("disconnected: " + disconnectResponse);

        if (disconnectResponse.getException() == null) {
            this.log.warn("no exception");

            return;
        }

        try {
            this.client.reconnect();
        } catch (MqttException e) {
            e.printStackTrace();

            throw (new RuntimeException(e));
        }
    }

    @Override
    public void mqttErrorOccurred(MqttException exception) {
        this.log.warn("error occurred: " + exception);

        try {
            this.client.reconnect();
        } catch (MqttException e) {
            e.printStackTrace();

            throw (new RuntimeException(e));
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        String messageTxt = new String(mqttMessage.getPayload());
        this.log.debug("topic: " + topic + ", Message: '" + messageTxt + "'");

        this.process.processData(topic, messageTxt);

        MqttProperties props = mqttMessage.getProperties();
        String responseTopic = props.getResponseTopic();

        if (responseTopic != null) {
            this.log.debug("response topic: " + responseTopic);
            String corrData = new String(props.getCorrelationData());

            MqttMessage response = new MqttMessage();

            props = new MqttProperties();

            props.setCorrelationData(corrData.getBytes());
            String content = "Got message with correlation data " + corrData;
            response.setPayload(content.getBytes());

            response.setProperties(props);
            this.client.publish(responseTopic, response);
        }
    }

    @Override
    public void deliveryComplete(IMqttToken token) {
        this.log.debug("delivery complete: " + token);
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        final AppProperties props = new AppProperties();

        try {
            props.loadProperties();
        } catch (IOException e) {
            e.printStackTrace();

            throw (new RuntimeException(e));
        }

        ListenerPaho listenerPaho = new ListenerPaho();

        for (String topic : props.getTopics()) {
            try {
                listenerPaho.listen(topic);
            } catch (Exception e) {
                e.printStackTrace();

                throw (new RuntimeException(e));
            }
        }

        this.log.info("connect complete: " + reconnect);
    }

    @Override
    public void authPacketArrived(int reasonCode, MqttProperties properties) {
        this.log.debug("auth packet arrived: " + reasonCode);
    }
}
