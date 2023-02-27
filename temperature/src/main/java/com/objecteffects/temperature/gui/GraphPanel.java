package com.objecteffects.temperature.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("serial")
class GraphPanel extends JPanel {
    @SuppressWarnings("unused")
    private final static Logger log = LogManager.getLogger(GraphPanel.class);

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(640, 640);
    }

    @Override
    public void paintComponent(final Graphics g) {

        final Graphics2D g2d = (Graphics2D) g;

        g2d.translate(0, (getHeight() - 1) / 2);
        g2d.scale(1, -1);

        setBorder(BorderFactory.createLineBorder(Color.RED, 20));

        super.paintComponent(g2d);

        g2d.draw(new Rectangle2D.Double(0, 0, 20, 100));

        g2d.drawString("This is my custom Panel!", 10, 20);
    }
}
