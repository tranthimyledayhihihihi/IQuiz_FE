package com.example.iq5.core.network;

import com.example.iq5.data.model.ApiResponse;
import com.example.iq5.data.model.WrongQuestionResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WrongQuestionApiService {
    
    @GET("api/wrongquestion/user/{userId}")
    Call<WrongQuestionResponse> getUserWrongQuestions(@Path("userId") int userId);
    
    @POST("api/wrongquestion/add")
    Call<ApiResponse> addWrongQuestion(@Body AddWrongQuestionRequest request);
    
    class AddWrongQuestionRequest {
        public int UserID;
        public int CauHoiID;
        public String DapAnSai;
        public String DapAnDung;
        
        public AddWrongQuestionRequest(int userId, int cauHoiId, String dapAnSai, String dapAnDung) {
            this.UserID = userId;
            this.CauHoiID = cauHoiId;
            this.DapAnSai = dapAnSai;
            this.DapAnDung = dapAnDung;
        }
    }
}