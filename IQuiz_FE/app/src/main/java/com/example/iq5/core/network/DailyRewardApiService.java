package com.example.iq5.core.network;

import com.example.iq5.data.model.ClaimRewardRequest;
import com.example.iq5.data.model.ClaimRewardResponse;
import com.example.iq5.data.model.DailyRewardResponse;
import com.example.iq5.data.model.TodayRewardResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DailyRewardApiService {

    // Lấy lịch sử thưởng
    @GET("api/DailyReward/user/{userId}")
    Call<DailyRewardResponse> getUserDailyRewards(
            @Path("userId") int userId,
            @Header("Authorization") String token
    );

    // Check hôm nay
    @GET("api/DailyReward/user/{userId}/today")
    Call<TodayRewardResponse> checkTodayReward(
            @Path("userId") int userId,
            @Header("Authorization") String token
    );

    // Claim thưởng
    @POST("api/DailyReward/claim")
    Call<ClaimRewardResponse> claimDailyReward(
            @Header("Authorization") String token,
            @Body ClaimRewardRequest request
    );
}
