package com.example.iq5.core.network;

import com.example.iq5.data.model.ApiResponse;
import com.example.iq5.data.model.LoginRequest;
import com.example.iq5.data.model.LoginResponse;
import com.example.iq5.data.model.RegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApiService {
    
    @POST("Account/login")
    Call<LoginResponse> login(@Body LoginRequest request);
    
    @POST("Account/register")
    Call<ApiResponse> register(@Body RegisterRequest request);
    
    @POST("Account/logout")
    Call<ApiResponse> logout();
}