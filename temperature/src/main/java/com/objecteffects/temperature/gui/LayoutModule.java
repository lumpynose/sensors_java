package com.objecteffects.temperature.gui;

import com.google.inject.AbstractModule;

public class LayoutModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(SensorsLayout.class).to(SensorsBoxLayout.class);
    }
}
