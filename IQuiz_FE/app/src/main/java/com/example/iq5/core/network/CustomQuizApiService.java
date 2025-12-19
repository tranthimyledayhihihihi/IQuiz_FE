package com.example.iq5.core.network;

import com.example.iq5.data.model.ApiResponse;
import com.example.iq5.data.model.CustomQuizResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CustomQuizApiService {
    
    @GET("api/customquiz/user/{userId}")
    Call<CustomQuizResponse> getUserCustomQuizzes(@Path("userId") int userId);
    
    @GET("api/customquiz")
    Call<CustomQuizResponse> getAllCustomQuizzes();
    
    @POST("api/customquiz/create")
    Call<ApiResponse> createCustomQuiz(@Body CreateCustomQuizRequest request);
    
    class CreateCustomQuizRequest {
        public int UserID;
        public String TenQuiz;
        public String MoTa;
        public int SoLuongCauHoi;
        public int ThoiGianGioiHan;
        
        public CreateCustomQuizRequest(int userId, String tenQuiz, String moTa, int soLuongCauHoi, int thoiGianGioiHan) {
            this.UserID = userId;
            this.TenQuiz = tenQuiz;
            this.MoTa = moTa;
            this.SoLuongCauHoi = soLuongCauHoi;
            this.ThoiGianGioiHan = thoiGianGioiHan;
        }
    }
}