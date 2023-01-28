package com.objecteffects.temperature.main;

import com.google.inject.AbstractModule;
import com.objecteffects.temperature.gui.SensorsBoxLayout;
import com.objecteffects.temperature.gui.SensorsLayout;

public class GuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(SensorsLayout.class).to(SensorsBoxLayout.class);
    }
}
