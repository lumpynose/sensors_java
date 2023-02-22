package com.objecteffects.temperature.main;

import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.objecteffects.temperature.gui.Sensors;
import com.objecteffects.temperature.gui.ISensors;
import com.objecteffects.temperature.gui.SensorsNull;
import com.objecteffects.temperature.mqtt.paho.ListenerPaho;

public class MainPaho {
    static {
        final String jar = System.getenv("JAR");

        if (Objects.equals(jar, "jar")) {
            System.setProperty("log4j.configurationFile", "log4j2_file.xml");
        }
        else {
            System.setProperty("log4j.configurationFile", "log4j2_console.xml");
        }
    }

    private final static Logger log = LogManager.getLogger(MainPaho.class);
    private final static AppProperties props = new AppProperties();

    private static void startMqttListener(final ISensors guiLayout) {
        try {
            props.loadProperties();
        }
        catch (final IOException e) {
            for (final StackTraceElement ste : e.getStackTrace()) {
                log.warn(ste.toString());
            }

            throw new RuntimeException(e);
        }

        log.debug("sensors: {}", props.getSensors());

        final ListenerPaho listener = new ListenerPaho(guiLayout);

        try {
            listener.connect(props.getBrokerAddress());

            for (final String topic : props.getTopics()) {
                listener.listen(topic);
            }
        }
        catch (final Exception e) {
            for (final StackTraceElement ste : e.getStackTrace()) {
                log.warn(ste.toString());
            }

            throw new RuntimeException(e);
        }

        log.debug("listener started");
    }

    public static void main(final String[] args)
            throws InvocationTargetException, InterruptedException {
        final ISensors guiLayout;

        if (!GraphicsEnvironment.isHeadless()) {
            guiLayout = new Sensors();

//            javax.swing.SwingUtilities.invokeLater(new Runnable() {
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    guiLayout.setup();
                }
            });
        }
        else {
            guiLayout = new SensorsNull();
        }

        startMqttListener(guiLayout);
    }
}
