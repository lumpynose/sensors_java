package com.objecteffects.temperature.http;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;

public class PromDataMatrix extends PromResponseEnvelope {
    final static Logger log = LogManager.getLogger();

    private PromResponseData data;

    public PromResponseData getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return "PromDataMatrix <<data=" + this.data + ">>";
    }

    class PromResponseData {
        private String resultType;
        private List<PromSensor> result;

        public List<PromSensor> getResult() {
            return this.result;
        }

        @Override
        public String toString() {
            return "PromResponseData <<resultType=" + this.resultType
                    + ", result=" + this.result + ">>";
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

        @Override
        public String toString() {
            return "PromSensor <<metric=" + this.metric + ", value="
                    + this.values + ">>";
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

        @Override
        public String toString() {
            return "PromMetric <<job=" + this.job + ", sensor=" + this.sensor
                    + ">>";
        }
    }

//    class PromValue {
//        long timestamp;
//        String value;
//
//        PromValue(final long _timestamp, final String _value) {
//            this.timestamp = _timestamp;
//            this.value = _value;
//        }
//
//        @Override
//        public String toString() {
//            return "PromValue <<timestamp=" + this.timestamp + ", value="
//                    + this.value + ">>";
//        }
//    }
}
