package com.au.example.busyflights.handler;

public class ErrResponse {

    private long traceID;

    private int status;


    private String message;


    public ErrResponse(long traceID, int status, String message) {
        this.traceID = traceID;
        this.status = status;
        this.message = message;
    }

    public long getTraceID() {
        return traceID;
    }

    public void setTraceID(long traceID) {
        this.traceID = traceID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
