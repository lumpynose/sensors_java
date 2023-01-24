package com.objecteffects.temperature.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import com.google.common.base.Splitter;

public class AppProperties {
    private final static Properties appProps = new Properties();
    private final static String PROPERTIES = "mqtt.properties";

    public void loadProperties() throws FileNotFoundException, IOException {
        try (InputStream root = new AppProperties().getClass().getClassLoader().getResourceAsStream(PROPERTIES)) {
            appProps.load(root);
        }
    }

    public String[] getTopics() {
        final Iterable<String> iter = Splitter.on(",").omitEmptyStrings().trimResults()
                .split(appProps.getProperty("mqtt.topics"));

        List<String> array = new ArrayList<>();
        for (String e : iter) {
            array.add(e);
        }

        return array.toArray(new String[0]);
    }

    public String getBrokerAddress() {
        String brokerAddress = appProps.getProperty("mqtt.brokerAddress");

        return brokerAddress;
    }

    public Map<String, String> getSensors() {
        final Map<String, String> splitKeyValues = Splitter.on(",").omitEmptyStrings().trimResults()
                .withKeyValueSeparator("=").split(appProps.getProperty("mqtt.sensors"));

        return splitKeyValues;
    }
}
