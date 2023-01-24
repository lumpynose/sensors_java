package com.objecteffects.temperature.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.objecteffects.temperature.mqtt.SensorData;

final public class GuiLayout {
    private final Logger log = LogManager.getLogger(this.getClass());

    private final static String TEMPERATURE = "temperature";
    private final static String TIME = "time";
    private final static String TFORMAT = "%3.0f";

    private final static JFrame frame = new JFrame("temperatures");

    private final static Color color1 = new Color(128, 128, 0);
    private final static Color color2 = new Color(143, 188, 143);
    private final static Color color3 = new Color(189, 183, 107);

    private final static Font nameFont = new Font("Arial Bold", Font.BOLD, 24);
    private final static Font temperatureFont = new Font("Arial Bold", Font.BOLD, 20);
    private final static Font timeFont = new Font("Arial", Font.PLAIN, 12);

    private final static Map<String, Map<String, JLabel>> panelsMap = new HashMap<>();

    private static JPanel mainPanel;

    public void setup() {
        final LayoutManager panelLayout = new GridLayout(0, 1, 8, 8);

        mainPanel = new JPanel(panelLayout);

        // mainPanel.setBorder(BorderFactory.createLineBorder(color1, 0));
        mainPanel.setBackground(color3);

        frame.getContentPane().setLayout(panelLayout);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.getContentPane().add(mainPanel, BorderLayout.PAGE_END);

        frame.pack();

        frame.setVisible(true);
    }

    public void addSensor(SensorData data) {
        Map<String, JLabel> labelsMap = new HashMap<>();
        panelsMap.put(data.getSensorName(), labelsMap);

        final LayoutManager panelLayout = new GridLayout(0, 1, 0, 0);

        final JPanel labelsPanel = new JPanel(panelLayout);

        labelsPanel.setName(data.getSensorName());
        // labelsPanel.setBorder(BorderFactory.createLineBorder(color1, 1));
        // labelsPanel.setBackground(color1);

        this.log.debug("label: " + data.getSensorName());

        // funky spaces added to the name to make it not so tight.
        final JLabel label1 = new JLabel(" " + data.getSensorName() + " ", SwingConstants.CENTER);

        label1.setName("name");
        label1.setOpaque(true);
        // label1.setBorder(BorderFactory.createLineBorder(color1, 1));
        label1.setBackground(color1 /* color2 */);
        label1.setFont(nameFont);
        labelsPanel.add(label1);

        String temperature = String.format(TFORMAT, Double.valueOf((data.getTemperature())));
        final JLabel label2 = new JLabel(temperature, SwingConstants.CENTER);

        label2.setName(TEMPERATURE);
        label2.setOpaque(true);
        // label2.setBorder(BorderFactory.createLineBorder(color1, 1));
        label2.setBackground(color2);
        label2.setFont(temperatureFont);

        labelsPanel.add(label2);

        labelsMap.put(TEMPERATURE, label2);

        final JLabel label3 = new JLabel(data.getTimestamp(), SwingConstants.CENTER);

        label3.setName(TIME);
        label3.setOpaque(true);
        // label3.setBorder(BorderFactory.createLineBorder(color1, 1));
        label3.setBackground(color2);
        label3.setFont(timeFont);

        labelsPanel.add(label3);

        labelsMap.put(TIME, label3);

        labelsPanel.validate();

        mainPanel.add(labelsPanel);

        frame.pack();

        frame.setVisible(true);

        this.log.debug("component count:" + mainPanel.getComponentCount());

        for (Component component : mainPanel.getComponents()) {
            this.log.debug("component: " + component.getName());

            if (component instanceof JPanel) {
                for (Component componentInner : ((JPanel) component).getComponents()) {
                    this.log.debug("componentInner: " + componentInner.getName());
                }
            }
        }
    }

    public void updateSensor(SensorData data) {
        if (!panelsMap.containsKey(data.getSensorName())) {
            this.log.warn("missing label " + data.getSensorName());

            return;
        }

        Map<String, JLabel> labels = panelsMap.get(data.getSensorName());

        final JLabel label2 = labels.get(TEMPERATURE);
        String temperature = String.format(TFORMAT, Double.valueOf((data.getTemperature())));
        label2.setText(temperature.toString());

        final JLabel label3 = labels.get(TIME);
        label3.setText(data.getTimestamp());
    }
}
