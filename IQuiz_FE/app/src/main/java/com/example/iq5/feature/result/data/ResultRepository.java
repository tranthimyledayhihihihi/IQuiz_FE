package com.example.iq5.feature.result.data;

import android.content.Context;
import android.util.Log;
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

/**
 * Repository quản lý dữ liệu cho hệ thống Result (Thành tích, Thống kê, Streak, Thưởng).
 * Tải dữ liệu từ các file JSON trong thư mục assets/result/
 */
public class ResultRepository {

    private static final String TAG = "ResultRepository";
    private final Context context;
    private final Gson gson;

    // ĐƯỜNG DẪN ĐÃ KHẮC PHỤC: Khớp với app/src/main/assets/result/
    private static final String ASSETS_ROOT = "result/";

    public ResultRepository(Context context) {
        this.context = context.getApplicationContext();
        this.gson = new Gson();
    }

    // =============================================
    // HELPER METHOD: Load JSON từ Assets
    // =============================================

    /**
     * Đọc nội dung file JSON từ thư mục assets.
     */
    private String loadJsonFromAssets(String fileName) {
        InputStream inputStream = null;
        String fullPath = ASSETS_ROOT + fileName;

        try {
            inputStream = context.getAssets().open(fullPath);
            int size = inputStream.available();
            byte[] buffer = new byte[size];

            int bytesRead = inputStream.read(buffer);
            if (bytesRead == -1) {
                Log.e(TAG, "File empty: " + fullPath);
                return null;
            }

            String jsonContent = new String(buffer, "UTF-8");
            // Log nội dung load thành công (chỉ 50 ký tự đầu)
            Log.d(TAG, "JSON loaded successfully: " + fullPath + " | Content start: " + jsonContent.substring(0, Math.min(jsonContent.length(), 50)) + "...");
            return jsonContent;

        } catch (IOException e) {
            // Lỗi FileNotFoundException
            Log.e(TAG, "❌ FileNotFoundException: Không tìm thấy file JSON tại: " + fullPath);
            e.printStackTrace();
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // =============================================
    // 1. DỮ LIỆU THÀNH TỰU (AchievementActivity)
    // =============================================

    public List<Achievement> getAchievements() {
        Type listType = new TypeToken<List<Achievement>>() {}.getType();
        String json = loadJsonFromAssets("achievements.json");

        if (json != null && !json.isEmpty()) {
            try {
                return gson.fromJson(json, listType);
            } catch (Exception e) {
                // Log lỗi Parse JSON
                Log.e(TAG, "❌ Error parsing achievements JSON (Check syntax/model):", e);
            }
        }
        return Collections.emptyList();
    }

    public int getUnlockedAchievementsCount() {
        List<Achievement> achievements = getAchievements();
        int count = 0;
        for (Achievement achievement : achievements) {
            if (achievement.isUnlocked()) {
                count++;
            }
        }
        return count;
    }

    public int getTotalAchievementsCount() {
        return getAchievements().size();
    }

    // =============================================
    // 2. DỮ LIỆU LỊCH SỬ CHUỖI NGÀY (StreakActivity)
    // =============================================

    public List<StreakDay> getStreakHistory() {
        Type listType = new TypeToken<List<StreakDay>>() {}.getType();
        String json = loadJsonFromAssets("streak_history.json");

        if (json != null && !json.isEmpty()) {
            try {
                List<StreakDay> history = gson.fromJson(json, listType);
                // SẮP XẾP history theo dayNumber giảm dần (quan trọng cho getCurrentStreakDays)
                history.sort(Comparator.comparingInt(StreakDay::getDayNumber).reversed());
                return history;
            } catch (Exception e) {
                Log.e(TAG, "❌ Error parsing streak_history JSON (Check syntax/model):", e);
            }
        }
        return Collections.emptyList();
    }

    /**
     * Lấy số ngày streak hiện tại (liên tục).
     */
    public int getCurrentStreakDays() {
        List<StreakDay> history = getStreakHistory();
        if (history.isEmpty()) {
            return 0;
        }

        int currentStreak = 0;
        // Logic tính streak liên tục dựa trên ngày đã completed (vì đã sắp xếp giảm dần)
        for (StreakDay day : history) {
            if (day.isCompleted()) {
                currentStreak++;
            } else {
                break; // Dừng khi chuỗi bị ngắt
            }
        }
        return currentStreak;
    }

    // =============================================
    // 3. DỮ LIỆU MỐC THỐNG KÊ (StatsActivity)
    // =============================================

    public List<UserStats> getStatsMilestones() {
        Type listType = new TypeToken<List<UserStats>>() {}.getType();
        String json = loadJsonFromAssets("stats_milestones.json");

        if (json != null && !json.isEmpty()) {
            try {
                return gson.fromJson(json, listType);
            } catch (Exception e) {
                Log.e(TAG, "❌ Error parsing stats_milestones JSON (Check syntax/model):", e);
            }
        }
        return Collections.emptyList();
    }

    // =============================================
    // 4. DỮ LIỆU THƯỞNG NGÀY (DailyRewardActivity)
    // =============================================

    public List<DailyReward> getDailyRewards() {
        Type listType = new TypeToken<List<DailyReward>>() {}.getType();
        String json = loadJsonFromAssets("daily_rewards.json");

        if (json != null && !json.isEmpty()) {
            try {
                List<DailyReward> rewards = gson.fromJson(json, listType);

                // Gán isToday cho reward đầu tiên chưa claimed
                boolean todayAssigned = false;
                for (DailyReward r : rewards) {
                    if (!r.isClaimed() && !todayAssigned) {
                        r.setToday(true);
                        todayAssigned = true;
                    } else {
                        r.setToday(false);
                    }
                }
                return rewards;
            } catch (Exception e) {
                Log.e(TAG, "❌ Error parsing daily_rewards JSON (Check syntax/model):", e);
                e.printStackTrace();
            }
        }
        return Collections.emptyList();
    }

    public DailyReward getTodayReward() {
        List<DailyReward> rewards = getDailyRewards();
        // Dựa vào logic đã gán 'isToday'
        for (DailyReward reward : rewards) {
            if (reward.isToday()) {
                return reward;
            }
        }
        return null;
    }

    public int getCurrentRewardStreak() {
        List<DailyReward> rewards = getDailyRewards();
        int streak = 0;
        for (DailyReward reward : rewards) {
            if (reward.isClaimed()) {
                streak++;
            } else {
                break;
            }
        }
        return streak;
    }

    // =============================================
    // 5. PHƯƠNG THỨC BỔ SUNG (Utility Methods)
    // =============================================

    public boolean isDataAvailable(String fileName) {
        String json = loadJsonFromAssets(fileName);
        return json != null && !json.isEmpty();
    }

    public void clearCache() {
        // Có thể implement logic xóa cache ở đây nếu cần
    }
}