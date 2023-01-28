package com.objecteffects.temperature.main;

import com.google.inject.AbstractModule;
import com.objecteffects.temperature.gui.SensorsBoxLayout;
import com.objecteffects.temperature.gui.SensorsLayout;
import com.objecteffects.temperature.sensors.ProcessSensorData;

public class GuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(SensorsLayout.class).to(SensorsBoxLayout.class);
        bind(ProcessSensorData.class);
    }
}
