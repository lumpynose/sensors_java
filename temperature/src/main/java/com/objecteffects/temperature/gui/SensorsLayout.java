package com.objecteffects.temperature.gui;

import com.objecteffects.temperature.mqtt.SensorData;

public interface SensorsLayout {
    void setup();

    void addSensor(SensorData target);

    void updateSensor(SensorData target);
}
