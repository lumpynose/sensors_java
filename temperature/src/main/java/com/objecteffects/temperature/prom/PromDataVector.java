package com.objecteffects.temperature.prom;

import java.util.List;

import com.google.gson.JsonPrimitive;

public class PromDataVector extends PromResponse {
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
        private List<JsonPrimitive> value;

        public PromMetric getMetric() {
            return this.metric;
        }

        public PromValue getValue() {
            return new PromValue(this.value.get(0).getAsLong(),
                    this.value.get(1).getAsString());
        }

        @Override
        public String toString() {
            return "PromSensor <<metric=" + this.metric + ", value="
                    + this.value + ">>";
        }
    }
}
