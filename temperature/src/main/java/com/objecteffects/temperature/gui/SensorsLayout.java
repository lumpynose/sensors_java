package com.objecteffects.temperature.gui;

import com.objecteffects.temperature.sensors.SensorData;

public interface SensorsLayout {
    void setup();

    void addSensor(SensorData target);

    void updateSensor(SensorData target);
}
