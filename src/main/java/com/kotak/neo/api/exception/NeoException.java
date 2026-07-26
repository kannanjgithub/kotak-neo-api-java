package com.kotak.neo.api.exception;

public class NeoException extends RuntimeException {
    private int status;
    private String reason;

    public NeoException(String message) {
        super(message);
    }

    public NeoException(String message, Throwable cause) {
        super(message, cause);
    }

    public NeoException(int status, String reason, String message) {
        super(message != null ? message : reason);
        this.status = status;
        this.reason = reason;
    }

    public int getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }
}
