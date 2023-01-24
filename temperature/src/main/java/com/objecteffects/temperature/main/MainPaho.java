package com.objecteffects.temperature.main;

import java.io.IOException;

import com.objecteffects.temperature.gui.GuiLayout;
import com.objecteffects.temperature.mqtt.ListenerPaho;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainPaho {
    private final static Logger log = LogManager.getLogger(MainPaho.class);

    private final static AppProperties props = new AppProperties();
    private static final GuiLayout guiLayout = new GuiLayout();

    private static void runGui() {
        guiLayout.setup();

        log.debug("runGui started");
    }

    private static void startListener() {
        try {
            props.loadProperties();
        } catch (IOException e) {
            e.printStackTrace();

            throw (new RuntimeException(e));
        }

        log.debug("properties: ", props.getSensors());

        ListenerPaho listenerPaho = new ListenerPaho();

        try {
            listenerPaho.connect(props.getBrokerAddress());

            for (String topic : props.getTopics()) {
                listenerPaho.listen(topic);
            }
        } catch (Exception e) {
            e.printStackTrace();

            throw (new RuntimeException(e));
        }

        log.debug("listener started");
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                runGui();
                startListener();

                log.debug("runnable started");
            }
        });
    }
}
