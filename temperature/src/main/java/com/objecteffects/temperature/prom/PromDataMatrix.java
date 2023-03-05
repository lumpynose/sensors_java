package com.objecteffects.temperature.prom;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;

public class PromDataMatrix extends PromResponse {
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

        public List<PromValue> getValues() {
            final List<PromValue> pvValues = new ArrayList<>();

            for (final JsonArray v : this.values) {
                pvValues.add(new PromValue(v.get(0).getAsLong(),
                        v.get(1).getAsString()));
            }
            return pvValues;
        }

        @Override
        public String toString() {
            return "PromSensor <<metric=" + this.metric + ", value="
                    + this.values + ">>";
        }
    }

}
