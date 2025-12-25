package com.example.iq5.core.network;

import com.example.iq5.data.model.ApiResponse;
import com.example.iq5.data.model.DailyRewardResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DailyRewardApiService {
    
    @GET("api/dailyreward/user/{userId}")
    Call<DailyRewardResponse> getUserDailyRewards(@Path("userId") int userId);
    
    @GET("api/dailyreward/user/{userId}/today")
    Call<ApiResponse> checkTodayReward(@Path("userId") int userId);
    
    @POST("api/dailyreward/claim")
    Call<ApiResponse> claimDailyReward(@Body ClaimRewardRequest request);
    
    class ClaimRewardRequest {
        public int UserID;
        public String LoaiThuong;
        public int GiaTri;
        public String MoTa;
        
        public ClaimRewardRequest(int userId, String loaiThuong, int giaTri, String moTa) {
            this.UserID = userId;
            this.LoaiThuong = loaiThuong;
            this.GiaTri = giaTri;
            this.MoTa = moTa;
        }
    }
}