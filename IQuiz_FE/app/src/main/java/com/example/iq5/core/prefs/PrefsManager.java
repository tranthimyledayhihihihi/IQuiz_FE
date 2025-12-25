package com.example.iq5.core.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

public class PrefsManager {

    private static final String PREF_NAME = "AppPrefs";

    private static final String KEY_JWT_TOKEN = "jwt_token";
    private static final String KEY_USER_ROLE = "user_role";
    private static final String KEY_USER_ID   = "user_id";

    private final SharedPreferences prefs;

    public PrefsManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // ================= AUTH TOKEN =================

    public void saveAuthToken(String token) {
        prefs.edit().putString(KEY_JWT_TOKEN, token).apply();
    }

    @Nullable
    public String getAuthToken() {
        return prefs.getString(KEY_JWT_TOKEN, null);
    }

    // 👉 Alias để dùng chung toàn app (KHÔNG phá code cũ)
    public String getToken() {
        return getAuthToken();
    }

    public void clearAuthToken() {
        prefs.edit().remove(KEY_JWT_TOKEN).apply();
    }

    // ================= USER ROLE =================

    public void saveUserRole(String role) {
        prefs.edit().putString(KEY_USER_ROLE, role).apply();
    }

    @Nullable
    public String getUserRole() {
        return prefs.getString(KEY_USER_ROLE, "sinhvien");
    }

    // ================= USER ID =================

    public void saveUserId(int userId) {
        prefs.edit().putInt(KEY_USER_ID, userId).apply();
    }

    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    // ================= CLEAR =================

    public void clear() {
        prefs.edit().clear().apply();
    }

    // ✅ FIX: method này trước bị rỗng
    public void clearToken() {
        clearAuthToken();
    }
}
