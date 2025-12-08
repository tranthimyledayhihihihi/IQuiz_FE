package com.example.iq5.core.network;

import com.example.iq5.data.model.LoginRequest;
import com.example.iq5.data.model.LoginResponse;
import com.example.iq5.data.model.UserDto;
import com.example.iq5.data.model.RegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthApiService {
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("auth/register")
    Call<UserDto> register(@Body RegisterRequest request);

    @GET("auth/me")
    Call<UserDto> getMe();
}