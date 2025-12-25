package com.example.iq5.data.repository;

import android.content.Context;
import android.util.Log;

import com.example.iq5.core.prefs.PrefsManager;
import com.example.iq5.data.api.ApiService;
import com.example.iq5.data.api.RetrofitClient;
import com.example.iq5.data.model.AchievementsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository xử lý Achievement
 * GỌI ĐÚNG API: /api/user/Achievement/me
 */
public class AchievementApiRepository {

    private static final String TAG = "AchievementApiRepository";

    private final ApiService apiService;
    private final PrefsManager prefsManager;

    public AchievementApiRepository(Context context) {

        // ✅ Lấy token từ Prefs
        prefsManager = new PrefsManager(context);

        // ✅ DÙNG DUY NHẤT RetrofitClient
        apiService = RetrofitClient.getApiService();
    }

    // =====================================================
    // GET MY ACHIEVEMENTS
    // =====================================================
    public void getMyAchievements(final AchievementsCallback callback) {

        String token = "Bearer " + prefsManager.getToken();

        Log.d(TAG, "🏆 Calling API getMyAchievements");
        Log.d(TAG, "🌐 URL = http://10.0.2.2:5048/api/user/Achievement/me");
        Log.d(TAG, "🔑 Token = " + token);

        Call<List<AchievementsResponse.Achievement>> call =
                apiService.getMyAchievements(token);

        call.enqueue(new Callback<List<AchievementsResponse.Achievement>>() {
            @Override
            public void onResponse(
                    Call<List<AchievementsResponse.Achievement>> call,
                    Response<List<AchievementsResponse.Achievement>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Success: " + response.body().size() + " achievements");
                    callback.onSuccess(response.body());

                } else if (response.code() == 401) {
                    Log.e(TAG, "❌ Unauthorized");
                    callback.onUnauthorized();

                } else {
                    Log.e(TAG, "❌ API Error: " + response.code());
                    callback.onError("API Error: " + response.code());
                }
            }

            @Override
            public void onFailure(
                    Call<List<AchievementsResponse.Achievement>> call,
                    Throwable t) {

                Log.e(TAG, "❌ Network error", t);
                callback.onError(t.getMessage());
            }
        });
    }

    // =====================================================
    // CALLBACK
    // =====================================================
    public interface AchievementsCallback {
        void onSuccess(List<AchievementsResponse.Achievement> achievements);
        void onUnauthorized();
        void onError(String error);
    }
}
