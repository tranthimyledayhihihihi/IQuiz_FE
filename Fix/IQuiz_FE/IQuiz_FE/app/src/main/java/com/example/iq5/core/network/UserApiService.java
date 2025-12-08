package com.example.iq5.core.network;

import com.example.iq5.data.model.UserDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserApiService {
    @GET("users")
    Call<List<UserDto>> getAllUsers();

    @GET("users/{id}")
    Call<UserDto> getUserDetails(@Path("id") int userId);
}