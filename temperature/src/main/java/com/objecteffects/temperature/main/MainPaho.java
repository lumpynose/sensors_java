package com.objecteffects.temperature.main;

import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.objecteffects.temperature.gui.ISensors;
import com.objecteffects.temperature.gui.Sensors;
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
    private static ISensors guiLayout;
    private static ListenerPaho listener;

    public static void startMqttListener() {
        getProps();

        try {
            listener.connect(Configuration.getBrokerAddress());

            for (final String topic : Configuration.getTopics()) {
                listener.listen(topic);
            }
        }
        catch (final Exception ex) {
            log.warn("connect", ex);

            throw new RuntimeException(ex);
        }

        log.debug("listener started");
    }

    private static void getProps() {
        try {
            Configuration.loadConfiguration();
        }
        catch (final ConfigurationException ex) {
            log.error("loadConfiguration", ex);

            throw new RuntimeException(ex);
        }
    }

    public static void main(final String[] args) {
        Toolkit.getDefaultToolkit().getScreenSize();

        if (GraphicsEnvironment.isHeadless()) {
            guiLayout = new SensorsNull();
        }
        else {
            guiLayout = new Sensors();
        }

        listener = new ListenerPaho(guiLayout);

        guiSetup();

        startMqttListener();
    }

    private static void guiSetup() {
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

        try {
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    guiLayout.setup();
                }
            });
        }
        catch (final InvocationTargetException e) {
            log.warn(e);

            throw new RuntimeException(e);
        }
        catch (final InterruptedException e) {
            log.warn(e);

            throw new RuntimeException(e);
        }
    }
}
