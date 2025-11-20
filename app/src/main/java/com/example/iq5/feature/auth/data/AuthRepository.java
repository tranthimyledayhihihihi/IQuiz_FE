package com.example.iq5.feature.auth.data;

import android.content.Context;

import com.example.iq5.feature.auth.model.HomeResponse;
import com.example.iq5.feature.auth.model.LoginResponse;
import com.example.iq5.feature.auth.model.ProfileResponse;
import com.example.iq5.feature.auth.model.RegisterResponse;
import com.example.iq5.feature.auth.model.SettingsResponse;
import com.example.iq5.feature.auth.model.SplashResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;

public class AuthRepository {

    private final Context context;
    private final Gson gson = new Gson();

    public AuthRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    private String loadJsonFromAssets(String path) {
        try {
            InputStream is = context.getAssets().open(path);
            int size = is.available();
            byte[] buffer = new byte[size];
            int read = is.read(buffer);
            is.close();
            if (read <= 0) return null;
            return new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public SplashResponse getSplashData() {
        String json = loadJsonFromAssets("auth/splash.json");
        return gson.fromJson(json, SplashResponse.class);
    }

    public LoginResponse getLoginData() {
        String json = loadJsonFromAssets("auth/login.json");
        return gson.fromJson(json, LoginResponse.class);
    }

    public RegisterResponse getRegisterData() {
        String json = loadJsonFromAssets("auth/register.json");
        return gson.fromJson(json, RegisterResponse.class);
    }

    public ProfileResponse getProfileData() {
        String json = loadJsonFromAssets("auth/profile.json");
        return gson.fromJson(json, ProfileResponse.class);
    }

    public SettingsResponse getSettingsData() {
        String json = loadJsonFromAssets("auth/settings.json");
        return gson.fromJson(json, SettingsResponse.class);
    }

    public HomeResponse getHomeData() {
        String json = loadJsonFromAssets("auth/home.json");
        return gson.fromJson(json, HomeResponse.class);
    }
}
