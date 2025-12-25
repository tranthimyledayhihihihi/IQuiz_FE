package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;

public class DailyRewardClaimResponse {

    @SerializedName("awarded")
    public boolean awarded;

    @SerializedName("message")
    public String message;
}
