package com.example.iq5.core.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

public class PrefsManager {
    private static final String PREF_NAME = "AppPrefs";
    private static final String KEY_JWT_TOKEN = "jwt_token";
    private static final String KEY_USER_ROLE = "user_role";
    private final SharedPreferences prefs;

    public PrefsManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveAuthToken(String token) {
        prefs.edit().putString(KEY_JWT_TOKEN, token).apply();
    }

    @Nullable
    public String getAuthToken() {
        return prefs.getString(KEY_JWT_TOKEN, null);
    }

    public void saveUserRole(String role) {
        prefs.edit().putString(KEY_USER_ROLE, role).apply();
    }

    @Nullable
    public String getUserRole() {
        return prefs.getString(KEY_USER_ROLE, "sinhvien");
    }

    public void clearAuthToken() {
        prefs.edit().remove(KEY_JWT_TOKEN).apply();
    }
    
    public void clear() {
        prefs.edit().clear().apply();
    }
}