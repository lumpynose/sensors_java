package com.objecteffects.temperature.gui;

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

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.objecteffects.temperature.sensors.SensorData;

final public class SensorsBoxLayout implements SensorsLayout {
    private final static Logger log = LogManager
            .getLogger(SensorsBoxLayout.class);

    private static final String NAME = "name";
    private final static String TEMPERATURE = "temperature";
    private final static String HUMIDITY = "humidity";
    private final static String TIME = "time";
    private final static String LUMINANCE = "luminance";
    private final static String PRESSURE = "pressure";
    private final static String VOC = "pressure";
    private final static String NFORMAT = " %s ";
    private final static String TFORMAT = "%3.0f\u00B0 %s";
    private final static String HFORMAT = "%3.0f%%";
    private final static String LUMFORMAT = "%d lux";
    private final static String PRESSFORMAT = "%3.0f mb";
    private final static String VOCFORMAT = "%d voc";

    private final static JFrame frame = new JFrame("temperatures");

    private final static Color color1 = new Color(128, 128, 0);
    private final static Color color2 = new Color(143, 188, 143);
    private final static Color color3 = new Color(189, 183, 107);

    private final static Font nameFont = new Font("Arial Bold", Font.BOLD, 24);
    private final static Font temperatureFont = new Font("Arial Bold",
            Font.BOLD, 20);
    private final static Font humidityFont = new Font("Arial", Font.PLAIN, 18);
    private final static Font luminanceFont = new Font("Arial", Font.PLAIN, 16);
    private final static Font pressureFont = new Font("Arial", Font.PLAIN, 16);
    private final static Font vocFont = new Font("Arial", Font.PLAIN, 16);
    private final static Font timeFont = new Font("Arial", Font.PLAIN, 12);

    private final static Map<String, Map<String, JLabel>> panelsMap = new HashMap<>();

    private static JPanel mainPanel;

    @Override
    public void setup() {
        mainPanel = new JPanel();

        mainPanel.setBackground(color3);
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LayoutManager panelLayout = new BoxLayout(mainPanel,
                BoxLayout.Y_AXIS);

        mainPanel.setLayout(panelLayout);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.getContentPane().add(mainPanel);

        frame.pack();

        frame.setVisible(true);
    }

    @Override
    public void addSensor(final SensorData data) {
        final Map<String, JLabel> labelsMap = new HashMap<>();

        panelsMap.put(data.getSensorName(), labelsMap);

        final LayoutManager panelLayout = new GridLayout(0, 1);

        final JPanel labelsPanel = new JPanel(panelLayout);

        labelsPanel.setName(data.getSensorName());
        labelsPanel.setLayout(panelLayout);
        labelsPanel.setBackground(color2);
        labelsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        log.debug("label: {}", data.getSensorName());

        // funky spaces added to the name to make it not so tight.
        final JLabel nameLabel = new JLabel(
                String.format(NFORMAT, data.getSensorName()),
                SwingConstants.CENTER);

        nameLabel.setName(NAME);
        nameLabel.setOpaque(true);
        nameLabel.setBackground(color1);
        nameLabel.setFont(nameFont);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        labelsPanel.add(nameLabel);

        final String temperature = String.format(TFORMAT,
                Double.valueOf(data.getTemperatureShow()),
                data.getTemperatureLetter());
        final JLabel temperatureLabel = new JLabel(temperature,
                SwingConstants.CENTER);

        temperatureLabel.setName(TEMPERATURE);
        temperatureLabel.setOpaque(true);
        temperatureLabel.setBackground(color2);
        temperatureLabel.setFont(temperatureFont);
        temperatureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        labelsPanel.add(temperatureLabel);

        labelsMap.put(TEMPERATURE, temperatureLabel);

        if (Float.isFinite(data.getHumidity())) {
            final String humidity = String.format(HFORMAT,
                    Double.valueOf(data.getHumidity()));
            final JLabel humidityLabel = new JLabel(humidity,
                    SwingConstants.CENTER);

            humidityLabel.setName(HUMIDITY);
            humidityLabel.setOpaque(true);
            humidityLabel.setBackground(color2);
            humidityLabel.setFont(humidityFont);
            humidityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            labelsPanel.add(humidityLabel);

            labelsMap.put(HUMIDITY, humidityLabel);
        }

        if (data.getLuminance() > Integer.MIN_VALUE) {
            final String luminance = String.format(LUMFORMAT,
                    Integer.valueOf(data.getLuminance()));
            final JLabel luminanceLabel = new JLabel(luminance,
                    SwingConstants.CENTER);

            luminanceLabel.setName(LUMINANCE);
            luminanceLabel.setOpaque(true);
            luminanceLabel.setBackground(color2);
            luminanceLabel.setFont(luminanceFont);
            luminanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            labelsPanel.add(luminanceLabel);

            labelsMap.put(LUMINANCE, luminanceLabel);
        }

        if (Float.isFinite(data.getPressure())) {
            final String pressure = String.format(PRESSFORMAT,
                    Float.valueOf(data.getPressure()));
            final JLabel pressureLabel = new JLabel(pressure,
                    SwingConstants.CENTER);

            pressureLabel.setName(PRESSURE);
            pressureLabel.setOpaque(true);
            pressureLabel.setBackground(color2);
            pressureLabel.setFont(pressureFont);
            pressureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            labelsPanel.add(pressureLabel);

            labelsMap.put(PRESSURE, pressureLabel);
        }

        if (data.getVoc() > Integer.MIN_VALUE) {
            final String voc = String.format(VOCFORMAT,
                    Integer.valueOf(data.getVoc()));
            final JLabel vocLabel = new JLabel(voc, SwingConstants.CENTER);

            vocLabel.setName(VOC);
            vocLabel.setOpaque(true);
            vocLabel.setBackground(color2);
            vocLabel.setFont(vocFont);
            vocLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            labelsPanel.add(vocLabel);

            labelsMap.put(VOC, vocLabel);
        }

        final JLabel timeLabel = new JLabel(data.getTimestamp(),
                SwingConstants.CENTER);

        timeLabel.setName(TIME);
        timeLabel.setOpaque(true);
        timeLabel.setBackground(color2);
        timeLabel.setFont(timeFont);
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        labelsPanel.add(timeLabel);

        labelsMap.put(TIME, timeLabel);

        labelsPanel.validate();

        sortPanels(labelsPanel);

        frame.pack();

        frame.setVisible(true);

        log.debug("component count: {}",
                Integer.valueOf(mainPanel.getComponentCount()));

        if (log.isDebugEnabled()) {
            for (final Component component : mainPanel.getComponents()) {
                log.debug("component: {}", component.getName());

                if (component instanceof JPanel) {
                    for (final Component componentInner : ((JPanel) component)
                            .getComponents()) {
                        log.debug("componentInner: {}",
                                componentInner.getName());
                    }
                }
            }
        }
    }

    private void sortPanels(final JPanel labelsPanel) {
        final List<Component> panelList = new ArrayList<>();

        panelList.addAll(Arrays.asList(mainPanel.getComponents()));
        panelList.add(labelsPanel);

        Collections.sort(panelList, new Comparator<Component>() {
            @Override
            public int compare(final Component c1, final Component c2) {
                return ((JPanel) c1).getName()
                        .compareTo(((JPanel) c2).getName());
            }
        });

        mainPanel.removeAll();

        for (final Component panel : panelList) {
            log.debug("panel: {}", panel.getName());
            mainPanel.add(panel);
        }
    }

    @Override
    public void updateSensor(final SensorData data) {
        if (!panelsMap.containsKey(data.getSensorName())) {
            log.warn("missing label: {}", data.getSensorName());

            return;
        }

        log.debug("updating: {}", data.getSensorName());

        final Map<String, JLabel> labels = panelsMap.get(data.getSensorName());

        final JLabel temperatureLabel = labels.get(TEMPERATURE);
        final String temperature = String.format(TFORMAT,
                Double.valueOf(data.getTemperatureShow()));
        temperatureLabel.setText(temperature);

        final JLabel timeLabel = labels.get(TIME);
        timeLabel.setText(data.getTimestamp());

        if (Float.isFinite(data.getHumidity())) {
            final JLabel humidityLabel = labels.get(HUMIDITY);

            if (humidityLabel != null) {
                final String humidity = String.format(HFORMAT,
                        Double.valueOf(data.getHumidity()));
                humidityLabel.setText(humidity);
            }
        }

        if (data.getLuminance() > Integer.MIN_VALUE) {
            final JLabel luminanceLabel = labels.get(LUMINANCE);

            if (luminanceLabel != null) {
                final String luminance = String.format(LUMFORMAT,
                        Integer.valueOf(data.getLuminance()));
                luminanceLabel.setText(luminance);
            }
        }

        if (Float.isFinite(data.getPressure())) {
            final JLabel pressureLabel = labels.get(PRESSURE);

            if (pressureLabel != null) {
                final String pressure = String.format(PRESSFORMAT,
                        Double.valueOf(data.getPressure()));
                pressureLabel.setText(pressure);
            }
        }

        if (data.getVoc() > Integer.MIN_VALUE) {
            final JLabel vocLabel = labels.get(VOC);

            if (vocLabel != null) {
                final String voc = String.format(VOCFORMAT,
                        Integer.valueOf(data.getVoc()));
                vocLabel.setText(voc);
            }
        }
    }
}
