package com.objecteffects.temperature.main;

import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.objecteffects.temperature.gui.ISensors;
import com.objecteffects.temperature.gui.Sensors;
import com.objecteffects.temperature.gui.SensorsNull;
import com.objecteffects.temperature.mqtt.paho.ListenerPaho;
import com.objecteffects.temperature.sensors.TUnit;

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
    private static Map<String, String> propSensors = null;
    private static TUnit tunit;

    private static void startMqttListener(final ISensors guiLayout) {
        getProps();

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

    private static void getProps() {
        try {
            props.loadProperties();
        }
        catch (final IOException e) {
            for (final StackTraceElement ste : e.getStackTrace()) {
                log.warn(ste.toString());
            }

            throw new RuntimeException(e);
        }

        tunit = props.getTUnit();

        propSensors = props.getSensors();

        log.debug("sensors: {}", props.getSensors());
    }

    public static TUnit getTunit() {
        return tunit;
    }

    public static Map<String, String> getPropSensors() {
        return propSensors;
    }

    public static void main(final String[] args)
            throws InvocationTargetException, InterruptedException {
        final ISensors guiLayout;
        Toolkit.getDefaultToolkit().getScreenSize();
        if (!GraphicsEnvironment.isHeadless()) {
            if (Desktop.isDesktopSupported()) {
                log.debug("desktop supported");
            }
            else {
                log.debug("desktop NOT supported");
            }

            if (SystemTray.isSupported()) {
                log.debug("system tray supported");
            }
            else {
                log.debug("system try NOT supported");
            }

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
