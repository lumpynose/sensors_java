package com.objecteffects.temperature.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.objecteffects.temperature.mqtt.SensorData;

final public class SensorsGridLayout implements SensorsLayout {
    private final Logger log = LogManager.getLogger(this.getClass());

    private static final String NAME = "name";
    private final static String TEMPERATURE = "temperature";
    private final static String HUMIDITY = "humidity";
    private final static String TIME = "time";
    private final static String NFORMAT = " %s ";
    private final static String TFORMAT = "%3.0f\u00B0 F";
    private final static String HFORMAT = "%3.0f%%";

    private final static JFrame frame = new JFrame("temperatures");

    private final static Color color1 = new Color(128, 128, 0);
    private final static Color color2 = new Color(143, 188, 143);
    private final static Color color3 = new Color(189, 183, 107);

    private final static Font nameFont = new Font("Arial Bold", Font.BOLD, 24);
    private final static Font temperatureFont = new Font("Arial Bold", Font.BOLD, 20);
    private final static Font humidityFont = new Font("Arial", Font.PLAIN, 18);
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

    @SuppressWarnings("boxing")
    public void addSensor(final SensorData data) {
        final Map<String, JLabel> labelsMap = new HashMap<>();

        panelsMap.put(data.getSensorName(), labelsMap);

        final LayoutManager panelLayout = new GridLayout(0, 1, 0, 0);

        final JPanel labelsPanel = new JPanel(panelLayout);

        labelsPanel.setName(data.getSensorName());

        labelsPanel.setBackground(color2);
        // labelsPanel.setBorder(BorderFactory.createLineBorder(color1, 1));

        this.log.debug("label: {}", data.getSensorName());

        // funky spaces added to the name to make it not so tight.
        final JLabel nameLabel = new JLabel(String.format(NFORMAT, data.getSensorName()), SwingConstants.CENTER);
        // final JLabel nameLabel = new JLabel(data.getSensorName(),
        // SwingConstants.CENTER);

        nameLabel.setName(NAME);
        nameLabel.setOpaque(true);
        nameLabel.setBackground(color1);
        nameLabel.setFont(nameFont);
        labelsPanel.add(nameLabel);
        // label1.setBorder(BorderFactory.createLineBorder(color1, 1));

        final String temperature = String.format(TFORMAT, Double.valueOf(data.getTemperature()));
        final JLabel temperatureLabel = new JLabel(temperature, SwingConstants.CENTER);

        temperatureLabel.setName(TEMPERATURE);
        temperatureLabel.setOpaque(true);
        temperatureLabel.setBackground(color2);
        temperatureLabel.setFont(temperatureFont);
        // label2.setBorder(BorderFactory.createLineBorder(color1, 1));

        labelsPanel.add(temperatureLabel);

        labelsMap.put(TEMPERATURE, temperatureLabel);

        if (Float.isFinite(data.getHumidity())) {
            final String humidity = String.format(HFORMAT, Double.valueOf(data.getHumidity()));
            final JLabel humidityLabel = new JLabel(humidity, SwingConstants.CENTER);

            humidityLabel.setName(HUMIDITY);
            humidityLabel.setOpaque(true);
            humidityLabel.setBackground(color2);
            humidityLabel.setFont(humidityFont);
            // humidityLabel.setBorder(BorderFactory.createLineBorder(color1, 1));

            labelsPanel.add(humidityLabel);

            labelsMap.put(HUMIDITY, humidityLabel);
        }

        final JLabel timeLabel = new JLabel(data.getTimestamp(), SwingConstants.CENTER);

        timeLabel.setName(TIME);
        timeLabel.setOpaque(true);
        timeLabel.setBackground(color2);
        timeLabel.setFont(timeFont);
        // label3.setBorder(BorderFactory.createLineBorder(color1, 1));

        labelsPanel.add(timeLabel);

        labelsMap.put(TIME, timeLabel);

        labelsPanel.validate();

        // mainPanel.add(labelsPanel);

        sortPanels(labelsPanel);

        frame.pack();

        frame.setVisible(true);

        this.log.debug("component count: {}", mainPanel.getComponentCount());

        if (this.log.isDebugEnabled()) {
            for (final Component component : mainPanel.getComponents()) {
                this.log.debug("component: {}", component.getName());

//            if (component instanceof JPanel) {
//                for (Component componentInner : ((JPanel) component).getComponents()) {
//                    this.log.debug("componentInner: {}", componentInner.getName());
//                }
//            }
            }
        }
    }

    private void sortPanels(final JPanel labelsPanel) {
        // final List<Component> panelList = Arrays.asList(mainPanel.getComponents());
        final List<Component> panelList = new ArrayList<>();

        panelList.addAll(Arrays.asList(mainPanel.getComponents()));
        panelList.add(labelsPanel);

        Collections.sort(panelList, new Comparator<Component>() {
            @Override
            public int compare(final Component c1, final Component c2) {
                return ((JPanel) c1).getName().compareTo(((JPanel) c2).getName());
            }
        });

        mainPanel.removeAll();

        for (final Component panel : panelList) {
            this.log.debug("panel: {}", panel.getName());
            mainPanel.add(panel);
        }
    }

    public void updateSensor(final SensorData data) {
        if (!panelsMap.containsKey(data.getSensorName())) {
            this.log.warn("missing label: {}", data.getSensorName());

            return;
        }

        this.log.debug("updating: {}", data.getSensorName());

        final Map<String, JLabel> labels = panelsMap.get(data.getSensorName());

        final JLabel label2 = labels.get(TEMPERATURE);
        final String temperature = String.format(TFORMAT, Double.valueOf(data.getTemperature()));
        label2.setText(temperature.toString());

        final JLabel label3 = labels.get(TIME);
        label3.setText(data.getTimestamp());
    }
}