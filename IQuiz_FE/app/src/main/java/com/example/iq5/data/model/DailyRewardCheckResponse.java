package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;

public class DailyRewardCheckResponse {
    @SerializedName("success")
    public boolean success;

    @SerializedName("claimed")
    public boolean claimed;

    @SerializedName("message")
    public String message;

    @SerializedName("type")
    public String type;

    @SerializedName("value")
    public int value;
}