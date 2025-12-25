package com.example.iq5.feature.multiplayer.data.models;

public class ErrorData {
    private String message;

    public ErrorData() {}

    public ErrorData(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}