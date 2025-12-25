package com.example.iq5.feature.result.data;

import android.content.Context;
import android.util.Log;

import com.example.iq5.core.prefs.PrefsManager;
import com.example.iq5.data.api.RetrofitClient;
import com.example.iq5.data.model.StreakResponse;
import com.example.iq5.feature.result.model.Achievement;
import com.example.iq5.feature.result.model.UserStats;
import com.example.iq5.feature.result.model.StreakDay;
import com.example.iq5.feature.result.model.DailyReward;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository quản lý dữ liệu Result
 */
public class ResultRepository {

    private static final String TAG = "ResultRepository";
    private final Context context;
    private final Gson gson;
    private static final String ASSETS_PATH = "result/";

    public ResultRepository(Context context) {
        this.context = context.getApplicationContext();
        this.gson = new Gson();
    }

    // =====================================================
    // HELPER: Load JSON từ assets
    // =====================================================
    private String loadJsonFromAssets(String path) {
        try (InputStream inputStream = context.getAssets().open(path)) {
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            return new String(buffer, "UTF-8");
        } catch (IOException e) {
            Log.e(TAG, "❌ Error loading JSON: " + path, e);
            return null;
        }
    }

    // =====================================================
    // 1. ACHIEVEMENTS
    // =====================================================
    public List<Achievement> getAchievements() {
        Type listType = new TypeToken<List<Achievement>>() {}.getType();
        String json = loadJsonFromAssets(ASSETS_PATH + "achievements.json");
        return json != null ? gson.fromJson(json, listType) : Collections.emptyList();
    }

    public int getUnlockedAchievementsCount() {
        int count = 0;
        for (Achievement a : getAchievements()) {
            if (a.isUnlocked()) count++;
        }
        return count;
    }

    public int getTotalAchievementsCount() {
        return getAchievements().size();
    }

    // =====================================================
    // 2. STREAK HISTORY (OFFLINE)
    // =====================================================
    public List<StreakDay> getStreakHistory() {
        Type listType = new TypeToken<List<StreakDay>>() {}.getType();
        String json = loadJsonFromAssets(ASSETS_PATH + "streak_history.json");
        if (json != null) {
            List<StreakDay> history = gson.fromJson(json, listType);
            history.sort(Comparator.comparingInt(StreakDay::getDayNumber).reversed());
            return history;
        }
        return Collections.emptyList();
    }

    public int getCurrentStreakDays() {
        List<StreakDay> history = getStreakHistory();
        int currentStreak = 0;
        for (StreakDay day : history) {
            if (day.isCompleted()) currentStreak++;
            else break;
        }
        return currentStreak;
    }

    // =====================================================
    // 3. STATS
    // =====================================================
    public List<UserStats> getStatsMilestones() {
        Type listType = new TypeToken<List<UserStats>>() {}.getType();
        String json = loadJsonFromAssets(ASSETS_PATH + "stats_milestones.json");
        return json != null ? gson.fromJson(json, listType) : Collections.emptyList();
    }

    // =====================================================
    // 4. DAILY REWARD
    // =====================================================
    public List<DailyReward> getDailyRewards() {
        Type listType = new TypeToken<List<DailyReward>>() {}.getType();
        String json = loadJsonFromAssets(ASSETS_PATH + "daily_rewards.json");
        return json != null ? gson.fromJson(json, listType) : Collections.emptyList();
    }

    public int getCurrentRewardStreak() {
        int streak = 0;
        for (DailyReward r : getDailyRewards()) {
            if (r.isClaimed()) streak++;
            else break;
        }
        return streak;
    }

    // =====================================================
    // 5. STREAK API (ONLINE)
    // =====================================================
    public interface StreakCallback {
        void onSuccess(int currentStreak, String message);
        void onError(String error);
    }

    public void getDailyStreak(StreakCallback callback) {
        PrefsManager prefsManager = new PrefsManager(context);
        String rawToken = prefsManager.getToken();

        if (rawToken == null || rawToken.isEmpty()) {
            callback.onError("Chưa đăng nhập");
            return;
        }

        RetrofitClient.getApiService()
                .getDailyStreak("Bearer " + rawToken)
                .enqueue(new Callback<StreakResponse>() {
                    @Override
                    public void onResponse(Call<StreakResponse> call, Response<StreakResponse> response) {
                        if (!response.isSuccessful() || response.body() == null) {
                            callback.onError("Lỗi API streak");
                            return;
                        }

                        StreakResponse body = response.body();
                        callback.onSuccess(body.getSoNgayLienTiep(), body.getMessage());
                    }

                    @Override
                    public void onFailure(Call<StreakResponse> call, Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }

    // =====================================================
    // UTILS
    // =====================================================
    public boolean isDataAvailable(String fileName) {
        String json = loadJsonFromAssets(fileName);
        return json != null && !json.isEmpty();
    }

    public void clearCache() {
        // Optional: implement cache clearing logic
    }
}
