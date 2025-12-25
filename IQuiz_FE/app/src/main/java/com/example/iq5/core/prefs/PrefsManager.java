package com.example.iq5.core.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.Nullable;

public class PrefsManager {
    private static final String PREF_NAME = "AppPrefs";
    private static final String KEY_JWT_TOKEN = "jwt_token";
    private static final String KEY_USER_ROLE = "user_role";
    private static final String KEY_USER_ID = "user_id";  // THÊM KEY NÀY
    private static final String KEY_USER_NAME = "user_name";  // CÓ THỂ THÊM NẾU CẦN
    private static final String KEY_USER_EMAIL = "user_email";  // CÓ THỂ THÊM NẾU CẦN

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

    // THÊM CÁC PHƯƠNG THỨC MỚI
    public void saveUserId(int userId) {
        prefs.edit().putInt(KEY_USER_ID, userId).apply();
    }

    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, 0); // Trả về 0 nếu chưa có
    }

    public void saveUserName(String userName) {
        prefs.edit().putString(KEY_USER_NAME, userName).apply();
    }

    @Nullable
    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, null);
    }

    public void saveUserEmail(String email) {
        prefs.edit().putString(KEY_USER_EMAIL, email).apply();
    }

    @Nullable
    public String getUserEmail() {
        return prefs.getString(KEY_USER_EMAIL, null);
    }

    public void clearAuthToken() {
        prefs.edit().remove(KEY_JWT_TOKEN).apply();
    }

    public void clearUserData() {
        prefs.edit()
                .remove(KEY_JWT_TOKEN)
                .remove(KEY_USER_ID)
                .remove(KEY_USER_NAME)
                .remove(KEY_USER_EMAIL)
                .remove(KEY_USER_ROLE)
                .apply();
    }

    public void clear() {
        prefs.edit().clear().apply();
    }

    // Phương thức kiểm tra đăng nhập
    public boolean isLoggedIn() {
        return getAuthToken() != null && !getAuthToken().isEmpty() && getUserId() > 0;
    }
}