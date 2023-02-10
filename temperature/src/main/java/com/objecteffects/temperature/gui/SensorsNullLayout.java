package com.objecteffects.temperature.gui;

import com.objecteffects.temperature.sensors.SensorData;

public class SensorsNullLayout implements SensorsLayout {
    @Override
    public void setup() {
        return;
    }

    @Override
    public void addSensor(final SensorData target) {
        return;
    }

    @Override
    public void updateSensor(final SensorData target) {
        return;
    }
}
