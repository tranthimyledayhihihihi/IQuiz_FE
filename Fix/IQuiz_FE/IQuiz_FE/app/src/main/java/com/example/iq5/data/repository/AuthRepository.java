package com.example.iq5.data.repository;

import com.example.iq5.data.model.LoginRequest;
import com.example.iq5.data.model.LoginResponse;

import retrofit2.Call;

public interface AuthRepository {
    // Sẽ trả về kết quả theo dạng RxJava Single/Flowable hoặc Coroutines Suspend Function
    // Dùng Call<T> ở đây cho đơn giản
    Call<LoginResponse> loginUser(LoginRequest request);

    void saveAuthToken(String token);

    // ... và các phương thức liên quan đến đăng ký, đăng xuất, lấy user hiện tại
}