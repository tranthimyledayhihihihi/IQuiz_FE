package com.example.iq5.core.network;

import com.example.iq5.data.model.ApiResponse;
import com.example.iq5.data.model.ClaimRewardRequest;
import com.example.iq5.data.model.DailyRewardResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DailyRewardApiService {

    @GET("api/dailyreward/user/{userId}")
    Call<DailyRewardResponse> getUserDailyRewards(
            @Path("userId") int userId,
            @Header("Authorization") String token
    );

    @GET("api/dailyreward/user/{userId}/today")
    Call<ApiResponse> checkTodayReward(
            @Path("userId") int userId,
            @Header("Authorization") String token
    );

    @POST("api/dailyreward/claim")
    Call<ApiResponse> claimDailyReward(
            @Header("Authorization") String token,
            @Body ClaimRewardRequest request
    );
}