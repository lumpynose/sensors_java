package com.objecteffects.temperature.main;

import com.google.common.base.Splitter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class AppProperties2 {
    private static Properties appProps;

    public static void main(String[] args) {
        try {
            loadProperties();
            getTopics();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static void loadProperties() throws FileNotFoundException, IOException {        
        final String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        final String appConfigPath = rootPath + "app.properties";

        appProps = new Properties();
        
        try (InputStream prop = new FileInputStream(appConfigPath)) {
            appProps.load(prop);
        }
    }

    public String getBrokerAddress() {
        String brokerAddress = appProps.getProperty("mqtt.brokerAddress");

        return brokerAddress;
    }

    public static Map<String, String> getSensors() {
        final Map<String, String> splitKeyValues = Splitter.on(",")
                .omitEmptyStrings()
                .trimResults()
                .withKeyValueSeparator(":")
                .split(appProps.getProperty("mqtt.sensors"));
        
        return splitKeyValues;
    }
    
    public static String[] getTopics() {
        final Iterable<String> iter = Splitter.on(",")
                .omitEmptyStrings()
                .trimResults()
                .split(appProps.getProperty("mqtt.topics"));
        
        List<String> array = new ArrayList<>();
        for (String e : iter) {
            array.add(e);
        }
        
        return array.toArray(new String[0]);
    }
}
