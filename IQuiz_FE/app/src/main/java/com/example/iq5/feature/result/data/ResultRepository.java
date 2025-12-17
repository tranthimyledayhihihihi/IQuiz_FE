package com.example.iq5.feature.result.data;

import android.content.Context;
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

/**
 * Repository quản lý dữ liệu cho hệ thống Result (Thành tích, Thống kê, Streak, Thưởng).
 * Tải dữ liệu từ các file JSON trong thư mục assets/result_data/
 */
public class ResultRepository {

    private final Context context;
    private final Gson gson;

    // Đường dẫn gốc cho các file JSON của hệ thống Result
    private static final String ASSETS_PATH = "Result/";

    public ResultRepository(Context context) {
        this.context = context.getApplicationContext();
        this.gson = new Gson();
    }

    // =============================================
    // HELPER METHOD: Load JSON từ Assets
    // =============================================

    /**
     * Đọc nội dung file JSON từ thư mục assets.
     *
     * @param path Đường dẫn tương đối từ thư mục assets (VD: "result_data/achievements.json")
     * @return Chuỗi JSON hoặc null nếu có lỗi
     */
    private String loadJsonFromAssets(String path) {
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(path);
            int size = inputStream.available();
            byte[] buffer = new byte[size];

            // Đọc toàn bộ dữ liệu vào buffer
            int bytesRead = inputStream.read(buffer);
            if (bytesRead == -1) {
                return null;
            }

            return new String(buffer, "UTF-8");

        } catch (IOException e) {
            // Log lỗi để debug
            e.printStackTrace();
            return null;
        } finally {
            // Đảm bảo đóng InputStream
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

    /**
     * Tải danh sách các thành tựu (Achievement) từ JSON.
     *
     * @return List<Achievement> hoặc danh sách rỗng nếu có lỗi
     */
    public List<Achievement> getAchievements() {
        Type listType = new TypeToken<List<Achievement>>() {}.getType();
        String json = loadJsonFromAssets(ASSETS_PATH + "achievements.json");

        if (json != null && !json.isEmpty()) {
            try {
                return gson.fromJson(json, listType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Collections.emptyList();
    }

    /**
     * Lấy số lượng thành tựu đã mở khóa.
     *
     * @return Số thành tựu đã unlock
     */
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

    /**
     * Lấy tổng số thành tựu.
     *
     * @return Tổng số thành tựu
     */
    public int getTotalAchievementsCount() {
        return getAchievements().size();
    }

    // =============================================
    // 2. DỮ LIỆU LỊCH SỬ CHUỖI NGÀY (StreakActivity)
    // =============================================

    /**
     * Tải lịch sử streak chi tiết (StreakDay) từ JSON.
     *
     * @return List<StreakDay> hoặc danh sách rỗng nếu có lỗi
     */
    public List<StreakDay> getStreakHistory() {
        Type listType = new TypeToken<List<StreakDay>>() {}.getType();
        String json = loadJsonFromAssets(ASSETS_PATH + "streak_history.json");

        if (json != null && !json.isEmpty()) {
            try {
                return gson.fromJson(json, listType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Collections.emptyList();
    }

    /**
     * Lấy số ngày streak hiện tại (ngày cao nhất đã completed).
     *
     * @return Số ngày streak hiện tại
     */
    public int getCurrentStreakDays() {
        List<StreakDay> history = getStreakHistory();
        if (history.isEmpty()) {
            return 0;
        }

        // Giả định danh sách đã sắp xếp theo dayNumber giảm dần
        for (StreakDay day : history) {
            if (day.isCompleted()) {
                return day.getDayNumber();
            }
        }
        return 0;
    }

    // =============================================
    // 3. DỮ LIỆU MỐC THỐNG KÊ (StatsActivity)
    // =============================================

    /**
     * Tải các mốc thống kê (UserStats) từ JSON.
     *
     * @return List<UserStats> hoặc danh sách rỗng nếu có lỗi
     */
    public List<UserStats> getStatsMilestones() {
        Type listType = new TypeToken<List<UserStats>>() {}.getType();
        String json = loadJsonFromAssets(ASSETS_PATH + "stats_milestones.json");

        if (json != null && !json.isEmpty()) {
            try {
                return gson.fromJson(json, listType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Collections.emptyList();
    }

    // =============================================
    // 4. DỮ LIỆU THƯỞNG NGÀY (DailyRewardActivity)
    // =============================================

    /**
     * Tải danh sách phần thưởng hàng ngày (DailyReward) từ JSON.
     *
     * @return List<DailyReward> hoặc danh sách rỗng nếu có lỗi
     */
    public List<DailyReward> getDailyRewards() {
        Type listType = new TypeToken<List<DailyReward>>() {}.getType();
        String json = loadJsonFromAssets(ASSETS_PATH + "daily_rewards.json");

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
                e.printStackTrace();
            }
        }
        return Collections.emptyList();
    }

    /**
     * Lấy phần thưởng của ngày hiện tại (ngày chưa claimed đầu tiên).
     *
     * @return DailyReward của ngày hôm nay hoặc null
     */
    public DailyReward getTodayReward() {
        List<DailyReward> rewards = getDailyRewards();
        for (DailyReward reward : rewards) {
            if (!reward.isClaimed()) {
                return reward;
            }
        }
        return null;
    }

    /**
     * Lấy ngày streak hiện tại (số ngày đã claimed liên tục).
     *
     * @return Số ngày đã claimed
     */
    public int getCurrentRewardStreak() {
        List<DailyReward> rewards = getDailyRewards();
        int streak = 0;
        for (DailyReward reward : rewards) {
            if (reward.isClaimed()) {
                streak++;
            } else {
                break; // Dừng khi gặp ngày chưa claimed
            }
        }
        return streak;
    }

    // =============================================
    // 5. PHƯƠNG THỨC BỔ SUNG (Utility Methods)
    // =============================================

    /**
     * Kiểm tra xem có dữ liệu trong assets hay không.
     *
     * @param fileName Tên file cần kiểm tra
     * @return true nếu file tồn tại và có dữ liệu
     */
    public boolean isDataAvailable(String fileName) {
        String json = loadJsonFromAssets(ASSETS_PATH + fileName);
        return json != null && !json.isEmpty();
    }

    /**
     * Xóa cache (nếu có implement caching trong tương lai).
     */
    public void clearCache() {
        // TODO: Implement caching mechanism nếu cần
    }
}