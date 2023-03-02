package com.objecteffects.temperature.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

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
        return new Dimension(WeatherPainter.PANELWIDTH,
                WeatherPainter.PANELHEIGHT);
    }

    @Override
    public void paintComponent(final Graphics g) {
        final Graphics2D g2d = (Graphics2D) g.create();

        super.paintComponent(g2d);

        g2d.translate(0, getHeight() - 1);
        g2d.scale(1, -1);

        setBorder(BorderFactory.createLineBorder(Color.GRAY, 10));

        g2d.setStroke(new BasicStroke(3f));

        g2d.draw(new RoundRectangle2D.Float(100, 100, 20, 100, 10, 10));
        g2d.draw(new RoundRectangle2D.Float(100, 100, 10, 10, 5, 5));

        g2d.setStroke(new BasicStroke(2f));
        g2d.draw(new Rectangle2D.Float(150, 150, 1, 1));

        g2d.setFont(new Font("Dialog", Font.PLAIN, 24));
        g2d.drawString("This is my custom Panel?", 10, 40);
    }
}
