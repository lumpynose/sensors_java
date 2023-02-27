package com.objecteffects.temperature.gui;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class WeatherPainter {
    private final static Logger log = LogManager
            .getLogger(WeatherPainter.class);

    final JFrame gframe;

    WeatherPainter() {
        this.gframe = new JFrame("temperatures");

        log.debug("button pressed");

        final GraphPanel gp = new GraphPanel();

        this.gframe.setContentPane(gp);

        this.gframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        this.gframe.pack();
        this.gframe.setVisible(true);
    }

    void dispose() {
        this.gframe.dispose();
    }

    boolean isShowing() {
        return this.gframe.isShowing();
    }
}
