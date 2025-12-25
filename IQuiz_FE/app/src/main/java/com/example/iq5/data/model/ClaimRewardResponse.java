package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;

public class ClaimRewardResponse {
    @SerializedName("success")
    public boolean success;

    @SerializedName("rewardId")
    public int rewardId;

    @SerializedName("claimId")
    public int claimId;

    @SerializedName("message")
    public String message;
}