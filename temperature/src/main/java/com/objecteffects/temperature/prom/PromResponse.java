package com.objecteffects.temperature.prom;

import java.util.Arrays;

public abstract class PromResponse {
    private String status;
    private String errorType;
    private String error;
    private String[] warnings;

    public String getStatus() {
        return this.status;
    }

    public String getErrorType() {
        return this.errorType;
    }

    public String getError() {
        return this.error;
    }

    public String[] getWarnings() {
        return this.warnings;
    }

    public class PromMetric {
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

    public class PromValue {
        long timestamp;
        String value;

        public long getTimestamp() {
            return this.timestamp;
        }

        public String getValue() {
            return this.value;
        }

        PromValue(final long _timestamp, final String _value) {
            this.timestamp = _timestamp;
            this.value = _value;
        }

        @Override
        public String toString() {
            return "PromValue <<timestamp=" + this.timestamp + ", value="
                    + this.value + ">>";
        }
    }

    @Override
    public String toString() {
        return "PromResponse [status=" + this.status + ", errorType="
                + this.errorType + ", error=" + this.error + ", warnings="
                + Arrays.toString(this.warnings) + "]";
    }
}
