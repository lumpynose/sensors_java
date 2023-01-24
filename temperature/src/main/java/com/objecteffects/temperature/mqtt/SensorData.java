package com.objecteffects.temperature.mqtt;

import java.util.Objects;

public class SensorData {
    private String sensorName = "";
    private float temperature_F = Float.NaN;
    private float temperature = Float.NaN;
    private float humidity = 0;
    private int battery_ok = 0;
    private String timestamp;

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSensorName() {
        return this.sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public float getTemperature_F() {
        return this.temperature_F;
    }

    public void setTemperature_F(float temperature_F) {
        this.temperature_F = temperature_F;
    }

    public float getTemperature() {
        return this.temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getHumidity() {
        return this.humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public int getBattery_ok() {
        return this.battery_ok;
    }

    public void setBattery_ok(int battery_ok) {
        this.battery_ok = battery_ok;
    }

    @Override
    public String toString() {
        return "SensorData [sensorName=" + this.sensorName + ", temperature_F=" + this.temperature_F + ", temperature="
                + this.temperature + ", humidity=" + this.humidity + ", battery_ok=" + this.battery_ok + "]";
    }

    @Override
    public int hashCode() {
        return (Objects.hashCode(this.sensorName));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        SensorData other = (SensorData) obj;

        return other.sensorName.compareToIgnoreCase(this.sensorName) == 0;
    }
}
