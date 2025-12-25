package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DailyRewardResponse {
    @SerializedName("success")
    public boolean success;

    @SerializedName("data")
    public List<DailyRewardData> data;

    @SerializedName("message")
    public String message;

    public static class DailyRewardData {
        @SerializedName("rewardId")
        public int rewardId;

        @SerializedName("userId")
        public int userId;

        @SerializedName("claimedOn")
        public String claimedOn;

        @SerializedName("claimType")
        public String claimType;

        @SerializedName("claimId")
        public int claimId;

        @SerializedName("giaTri")
        public int GiaTri;

        @SerializedName("meTa")
        public String meTa;
    }
}