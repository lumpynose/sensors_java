package com.objecteffects.temperature.main;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.objecteffects.temperature.gui.LayoutModule;
import com.objecteffects.temperature.gui.SensorsLayout;
import com.objecteffects.temperature.mqtt.ListenerPaho;

public class MainPaho {
    private final static Logger log = LogManager.getLogger(MainPaho.class);

    private final static AppProperties props = new AppProperties();

    private static void startListener(final SensorsLayout guiLayout) {
        try {
            props.loadProperties();
        } catch (final IOException e) {
            e.printStackTrace();

            throw new RuntimeException(e);
        }

        log.debug("properties: {}", props.getSensors());

        final ListenerPaho listenerPaho = new ListenerPaho(guiLayout);

        try {
            listenerPaho.connect(props.getBrokerAddress());

            for (final String topic : props.getTopics()) {
                listenerPaho.listen(topic);
            }
        } catch (final Exception e) {
            e.printStackTrace();

            throw new RuntimeException(e);
        }

        log.debug("listener started");
    }

    public static void main(final String[] args) {
        final Injector injector = Guice.createInjector(new LayoutModule());
        final SensorsLayout guiLayout = injector.getInstance(SensorsLayout.class);

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                guiLayout.setup();

                startListener(guiLayout);

                log.debug("runnable started");
            }
        });
    }
}
