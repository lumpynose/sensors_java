package com.objecteffects.temperature.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.junit.jupiter.api.Test;

final class WPTests {
    final static int PANELWIDTH = 640;
    final static int PANELHEIGHT = 480;

    final JFrame frame;

    WPTests() {
        this.frame = new JFrame("temperatures");
    }

    public void createGui() {
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addMenuBar();

        final JPanel contentPane = new JPanel();

        contentPane.setDoubleBuffered(true);

        this.frame.setContentPane(contentPane);

        final JLayeredPane layeredPane = new JLayeredPane();

        layeredPane.setPreferredSize(new Dimension(PANELWIDTH, PANELHEIGHT));

        contentPane.add(layeredPane);

        addGraphPanels(layeredPane);

        layeredPane.setVisible(true);

        this.frame.pack();
        this.frame.setVisible(true);
    }

    void addMenuBar() {
        final JMenuBar menuBar = new JMenuBar();

        menuBar.setOpaque(true);
        menuBar.setBackground(new Color(154, 165, 64));
        menuBar.setPreferredSize(new Dimension(PANELWIDTH, 20));

        this.frame.setJMenuBar(menuBar);
    }

    void addGraphPanels(final JLayeredPane layeredPane) {
        final GraphPanel1 graphPanel1 = new GraphPanel1();
        graphPanel1.setSize(PANELWIDTH, PANELHEIGHT);
        graphPanel1.setLocation(0, 0);

        layeredPane.add(graphPanel1, Integer.valueOf(8));

        final GraphPanel2 graphPanel2 = new GraphPanel2();
        graphPanel2.setBounds(0, 0, PANELWIDTH, PANELHEIGHT);

        layeredPane.add(graphPanel2, Integer.valueOf(12));
    }

    void dispose() {
        this.frame.dispose();
    }

    boolean isShowing() {
        return this.frame.isShowing();
    }

    @Test
    void WPTest() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final WPTests wp = new WPTests();
                wp.createGui();
            }
        });

        try {
            Thread.sleep(1000 * 300);
        }
        catch (@SuppressWarnings("unused") final InterruptedException e) {
            return;
        }
    }
}
