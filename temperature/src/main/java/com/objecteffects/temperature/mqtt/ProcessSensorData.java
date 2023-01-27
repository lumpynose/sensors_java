package com.objecteffects.temperature.mqtt;

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
import com.google.inject.Inject;
import com.objecteffects.temperature.gui.SensorsLayout;
import com.objecteffects.temperature.main.AppProperties;

public class ProcessSensorData {
    private final Logger log = LogManager.getLogger(this.getClass());

    private final static Collection<SensorData> sensors = Collections.synchronizedSet(new HashSet<>());
    private final AppProperties props;
    private final Map<String, String> propSensors;
    private final SensorsLayout guiLayout;

    @Inject
    public ProcessSensorData(final SensorsLayout _guiLayout) {
        this.guiLayout = _guiLayout;
        this.props = new AppProperties();

        try {
            this.props.loadProperties();
        } catch (final IOException e) {
            e.printStackTrace();

            throw new RuntimeException(e);
        }

        this.propSensors = this.props.getSensors();
    }

    @SuppressWarnings("boxing")
    public void processData(final String topic, final String data) {
        final Gson gson = new Gson();

        final String topic_trimmed = StringUtils.substringAfterLast(topic, "/");

        this.log.debug("topic: {}", topic_trimmed);

        final SensorData target = gson.fromJson(data, SensorData.class);

        if (!this.propSensors.containsKey(topic_trimmed)) {
            return;
        }

        target.setSensorName(this.propSensors.get(topic_trimmed));

        if (Float.isFinite(target.getTemperature_F())) {
            target.setTemperature(target.getTemperature_F());
        } else {
            // (0°C × 9/5) + 32 = 32°F
            final float fahr = (float) (target.getTemperature() * (9.0 / 5.0) + 32.0);
            target.setTemperature(fahr);
        }

        final LocalDateTime dateTime = LocalDateTime.now();
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");

        target.setTimestamp(dtf.format(dateTime));

        this.log.debug("decoded data: {}", target.toString());

        if (sensors.add(target)) {
            this.log.debug("add target: {}", target.getSensorName());

            this.guiLayout.addSensor(target);
        } else {
            this.log.debug("update target: {}", target.getSensorName());

            this.guiLayout.updateSensor(target);
        }

        this.log.debug("length: {}", sensors.size());
    }
}
