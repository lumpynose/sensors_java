package com.objecteffects.temperature.sensors;

import java.util.Objects;

public class SensorData {
    private String sensorName = "";
    private float temperature_F = Float.NaN;
    private float temperature = Float.NaN;
    private float humidity = Float.NaN;
    private int battery_ok = 0;
    private String timestamp;

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(final String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSensorName() {
        return this.sensorName;
    }

    public void setSensorName(final String sensorName) {
        this.sensorName = sensorName;
    }

    public float getTemperature_F() {
        return this.temperature_F;
    }

    public void setTemperature_F(final float temperature_F) {
        this.temperature_F = temperature_F;
    }

    public float getTemperature() {
        return this.temperature;
    }

    public void setTemperature(final float temperature) {
        this.temperature = temperature;
    }

    public float getHumidity() {
        return this.humidity;
    }

    public void setHumidity(final float humidity) {
        this.humidity = humidity;
    }

    public int getBattery_ok() {
        return this.battery_ok;
    }

    public void setBattery_ok(final int battery_ok) {
        this.battery_ok = battery_ok;
    }

    @Override
    public String toString() {
        return "SensorData [sensorName=" + this.sensorName + ", temperature_F=" + this.temperature_F + ", temperature="
                + this.temperature + ", humidity=" + this.humidity + ", battery_ok=" + this.battery_ok + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.sensorName);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        final SensorData other = (SensorData) obj;

        return other.sensorName.compareToIgnoreCase(this.sensorName) == 0;
    }
}
