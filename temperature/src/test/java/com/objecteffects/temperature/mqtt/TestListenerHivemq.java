package com.objecteffects.temperature.mqtt;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import com.objecteffects.temperature.gui.Sensors;
import com.objecteffects.temperature.main.AppProperties;
import com.objecteffects.temperature.mqtt.hivemq.ListenerHivemq;

public class TestListenerHivemq {
    private final static Logger log = LogManager.getLogger();

    private final static AppProperties props = new AppProperties();

    @Test
    @Timeout(value = 2, unit = TimeUnit.MINUTES)
    public void startListener() {
        try {
            props.loadProperties();
        }
        catch (final IOException e) {
            for (final StackTraceElement ste : e.getStackTrace()) {
                log.warn(ste.toString());
            }

            throw new RuntimeException(e);
        }

        final ListenerHivemq listener = new ListenerHivemq(
                new Sensors());

        listener.connect(props.getBrokerAddress());

        for (final String topic : props.getTopics()) {
            log.info("test listening: {}", topic);

            listener.listen(topic);
        }

//        try {
//            Thread.sleep(2000);
//        }
//        catch (final InterruptedException e) {
//            // TODO Auto-generated catch block
//            log.warn("sleep: {}", e);
//        }
    }
}
