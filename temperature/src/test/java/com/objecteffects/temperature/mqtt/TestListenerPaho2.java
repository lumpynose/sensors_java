package com.objecteffects.temperature.mqtt;

public class TestListenerPaho2 {
    public static void main(String[] args) {
        final String topic1 = "rtl_433/temperature/#";
        final String topic2 = "zigbee2mqtt/temperature/#";
        final String broker = "tcp://192.168.50.9:1883"; 

        ListenerPaho listener = new ListenerPaho();

        try {
            listener.connect(broker);
            listener.listen(topic1);
            listener.listen(topic2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
