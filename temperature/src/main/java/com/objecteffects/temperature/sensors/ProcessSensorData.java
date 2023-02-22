package com.objecteffects.temperature.sensors;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.objecteffects.temperature.gui.ISensors;
import com.objecteffects.temperature.main.AppProperties;

public class ProcessSensorData {
    private final static Logger log = LogManager
            .getLogger(ProcessSensorData.class);

    private final static Collection<SensorData> sensors = Collections
            .synchronizedSet(new HashSet<>());
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
    private static AppProperties props;
    private static Map<String, String> propSensors = null;
    private static ISensors guiLayout;
    private static TUnit tunit;

    public ProcessSensorData(final ISensors _guiLayout) {
        guiLayout = _guiLayout;
        props = new AppProperties();

        try {
            props.loadProperties();
        }
        catch (final IOException e) {
            e.printStackTrace();

            throw new RuntimeException(e);
        }

        propSensors = props.getSensors();

        tunit = props.getTUnit();
    }

    public void processData(final String topic, final String data) {
        final Gson gson = new Gson();

        final String topic_trimmed = StringUtils.substringAfterLast(topic, "/");

        log.debug("topic: {}", topic_trimmed);

        final SensorData target = gson.fromJson(data, SensorData.class);

        if (!propSensors.containsKey(topic_trimmed)) {
            return;
        }

        target.setSensorName(propSensors.get(topic_trimmed));

        target.setTemperatureShow((float) tunit.convert(target));
        target.setTemperatureLetter(tunit.toString());

        final LocalDateTime dateTime = LocalDateTime.now();

        target.setTimestamp(this.dtf.format(dateTime));

        log.debug("decoded data: {}", target.toString());

        if (sensors.add(target)) {
            log.debug("add target: {}", target.getSensorName());

            guiLayout.addSensor(target);
        }
        else {
            log.debug("update target: {}", target.getSensorName());

            guiLayout.updateSensor(target);
        }

        log.debug("length: {}", Integer.valueOf(sensors.size()));
    }
}
