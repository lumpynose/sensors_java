package com.objecteffects.temperature.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class WeatherPainter {
    private final static Logger log = LogManager
            .getLogger(WeatherPainter.class);

    final static int PANELWIDTH = 640;
    final static int PANELHEIGHT = 480;

    final JFrame frame;

    public WeatherPainter() {
        this.frame = new JFrame("temperatures");

        this.frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        log.debug("button pressed");

        final JPanel contentPane = new JPanel();

        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(Color.DARK_GRAY);
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.frame.setContentPane(contentPane);

        final JMenuBar menuBar = new JMenuBar();

        menuBar.setOpaque(true);
        menuBar.setBackground(new Color(154, 165, 127));
        menuBar.setPreferredSize(new Dimension(PANELWIDTH, 20));

        this.frame.setJMenuBar(menuBar);

        final GraphPanel graphPanel = new GraphPanel();

        contentPane.add(graphPanel);

        this.frame.pack();
        this.frame.setVisible(true);
    }

    void dispose() {
        this.frame.dispose();
    }

    boolean isShowing() {
        return this.frame.isShowing();
    }
}
