package com.objecteffects.temperature.http;

import java.util.List;

import com.google.gson.JsonArray;

public class PromData2 extends PromResponseEnvelope {
    private PromResponseData data;

    public PromResponseData getData() {
        return this.data;
    }

    class PromResponseData {
        private String resultType;
        private PromSensor[] result;

        public PromSensor[] getResult() {
            return this.result;
        }
    }

    class PromSensor {
        private PromMetric metric;
        private List<JsonArray> values;

        public PromMetric getMetric() {
            return this.metric;
        }

        public List<JsonArray> getValues() {
            return this.values;
        }
    }

    class PromMetric {
        private String job;
        private String sensor;

        public String getJob() {
            return this.job;
        }

        public String getSensor() {
            return this.sensor;
        }
    }

    class PromValue {
        private long timestamp;
        private float value;
    }
}
