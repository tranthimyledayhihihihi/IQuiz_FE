package com.example.iq5.feature.multiplayer.data.models;

import com.google.gson.annotations.SerializedName;

public class ServerMessage {
    @SerializedName("Type")
    private String type;

    @SerializedName("Data")
    private Object data;

    public String getType() { return type; }
    public Object getData() { return data; }
}