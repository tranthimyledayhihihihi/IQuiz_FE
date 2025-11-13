package com.example.iq5.data.repository;

import com.example.iq5.core.network.AuthApiService;
import com.example.iq5.core.prefs.PrefsManager;
import com.example.iq5.data.model.LoginRequest;
import com.example.iq5.data.model.LoginResponse;

import retrofit2.Call;

public class AuthRepositoryImpl implements AuthRepository {

    private final AuthApiService apiService;
    private final PrefsManager prefsManager;

    // Dependency Injection (Cần Dagger/Hilt để inject)
    public AuthRepositoryImpl(AuthApiService apiService, PrefsManager prefsManager) {
        this.apiService = apiService;
        this.prefsManager = prefsManager;
    }

    @Override
    public Call<LoginResponse> loginUser(LoginRequest request) {
        // Gọi API Service
        return apiService.login(request);
    }

    @Override
    public void saveAuthToken(String token) {
        prefsManager.saveAuthToken(token);
    }

    // ... các phương thức khác
}