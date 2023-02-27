package com.objecteffects.temperature.http;

import java.util.Arrays;

public abstract class PromResponseEnvelope {
    private String status;
    private String errorType;
    private String error;
    private String[] warnings;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(final String _status) {
        this.status = _status;
    }

    public String getErrorType() {
        return this.errorType;
    }

    public void setErrorType(final String _errorType) {
        this.errorType = _errorType;
    }

    public String getError() {
        return this.error;
    }

    public void setError(final String _error) {
        this.error = _error;
    }

    public String[] getWarnings() {
        return this.warnings;
    }

    public void setWarnings(final String[] _warnings) {
        this.warnings = _warnings;
    }

    @Override
    public String toString() {
        return "PromResponseEnvelope [status=" + this.status + ", errorType="
                + this.errorType + ", error=" + this.error + ", warnings="
                + Arrays.toString(this.warnings) + "]";
    }
}
