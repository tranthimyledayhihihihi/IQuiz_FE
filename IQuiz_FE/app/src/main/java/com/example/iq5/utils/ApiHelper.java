package com.example.iq5.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.iq5.data.api.ApiService;
import com.example.iq5.data.api.RetrofitClient;


public class ApiHelper {
    
    private static final String TAG = "ApiHelper";
    private static final String PREF_NAME = "QuizAppPrefs";
    private static final String KEY_TOKEN = "jwt_token";
    private static final String KEY_USER_ID = "user_id";

    public static void saveToken(Context context, String token) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_TOKEN, token).apply();
        Log.d(TAG, "✅ Token đã được lưu");
    }

    public static String getToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_TOKEN, null);
    }

    public static String getAuthHeader(Context context) {
        String token = getToken(context);
        if (token != null) {
            return "Bearer " + token;
        }
        return null;
    }

    public static boolean isLoggedIn(Context context) {
        return getToken(context) != null;
    }

    public static void clearToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(KEY_TOKEN).remove(KEY_USER_ID).apply();
        Log.d(TAG, "✅ Token đã được xóa (logout)");
    }

    public static void saveUserId(Context context, int userId) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_USER_ID, userId).apply();
    }

    public static int getUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_USER_ID, -1);
    }

    public static ApiService getApiService() {
        return RetrofitClient.getApiService();
    }
    public static void checkApiHealth(final HealthCheckCallback callback) {
        ApiService apiService = getApiService();
        
        apiService.healthCheck().enqueue(new retrofit2.Callback<com.example.iq5.data.model.HealthCheckResponse>() {
            @Override
            public void onResponse(retrofit2.Call<com.example.iq5.data.model.HealthCheckResponse> call, 
                                 retrofit2.Response<com.example.iq5.data.model.HealthCheckResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean isHealthy = "OK".equals(response.body().getDatabaseStatus());
                    callback.onResult(isHealthy, response.body().getMessage());
                } else {
                    callback.onResult(false, "API không phản hồi");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<com.example.iq5.data.model.HealthCheckResponse> call, Throwable t) {
                callback.onResult(false, "Lỗi kết nối: " + t.getMessage());
            }
        });
    }


    public interface HealthCheckCallback {
        void onResult(boolean isHealthy, String message);
    }
}
