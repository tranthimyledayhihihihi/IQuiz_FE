package com.example.iq5.feature.multiplayer.data.models;

public class ClientMessage {
    private String Type;
    private Object Data;

    public ClientMessage(String type, Object data) {
        this.Type = type;
        this.Data = data;
    }

    public String getType() { return Type; }
    public Object getData() { return Data; }
}