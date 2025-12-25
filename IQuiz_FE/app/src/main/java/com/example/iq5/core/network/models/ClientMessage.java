package com.example.iq5.core.network.models;

public class ClientMessage {
    private String Type;
    private Object Data;

    public ClientMessage(String type, Object data) {
        this.Type = type;
        this.Data = data;
    }

    public String getType() { return Type; }
    public void setType(String type) { this.Type = type; }

    public Object getData() { return Data; }
    public void setData(Object data) { this.Data = data; }
}