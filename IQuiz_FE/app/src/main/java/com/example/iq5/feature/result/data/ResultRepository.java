package com.example.iq5.feature.result.data;

import android.content.Context;
import android.util.Log;

import com.example.iq5.data.api.RetrofitClient;
import com.example.iq5.data.model.StreakResponse;
import com.example.iq5.data.model.StreakDayDto;
import com.example.iq5.data.model.DailyRewardResponse;
import com.example.iq5.feature.result.model.Achievement;
import com.example.iq5.feature.result.model.DailyReward;
import com.example.iq5.feature.result.model.StreakDay;
import com.example.iq5.feature.result.model.UserStats;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultRepository {

    private final Context context;

    public ResultRepository(Context context) {
        this.context = context;
    }

    // ================= STREAK =================

    public interface StreakCallback {
        void onSuccess(int currentStreak, String message);
        void onError(String error);
    }

    public void getDailyStreak(StreakCallback callback) {

        String rawToken = context
                .getSharedPreferences("auth", Context.MODE_PRIVATE)
                .getString("jwt_token", null);

        Log.d("AUTH_CHECK", "JWT = " + rawToken);

        if (rawToken == null || rawToken.isEmpty()) {
            callback.onError("Chưa đăng nhập");
            return;
        }

        RetrofitClient.getApiService()
                .getDailyStreak("Bearer " + rawToken)
                .enqueue(new Callback<StreakResponse>() {

                    @Override
                    public void onResponse(
                            Call<StreakResponse> call,
                            Response<StreakResponse> response
                    ) {
                        if (!response.isSuccessful() || response.body() == null) {
                            callback.onError("Lỗi API streak");
                            return;
                        }

                        StreakResponse body = response.body();

                        callback.onSuccess(
                                body.getSoNgayLienTiep(),
                                body.getMessage()
                        );
                    }

                    @Override
                    public void onFailure(Call<StreakResponse> call, Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }



    // ================= DAILY REWARD =================

    public interface DailyRewardCallback {
        void onSuccess(List<DailyReward> rewards, boolean canClaimToday);
        void onError(String error);
    }

    public void getDailyRewards(DailyRewardCallback callback) {
        String token = "Bearer " + context
                .getSharedPreferences("auth", Context.MODE_PRIVATE)
                .getString("jwt_token", "");

        RetrofitClient.getApiService()
                .getDailyRewards(token)
                .enqueue(new Callback<DailyRewardResponse>() {
                    @Override
                    public void onResponse(
                            Call<DailyRewardResponse> call,
                            Response<DailyRewardResponse> response
                    ) {
                        if (!response.isSuccessful() || response.body() == null) {
                            callback.onError("Lỗi API");
                            return;
                        }

                        DailyRewardResponse body = response.body();

                        if (!body.success || body.data == null) {
                            callback.onError(body.message != null
                                    ? body.message
                                    : "Không có dữ liệu");
                            return;
                        }

                        // ✅ Convert BE model → UI model (ĐÚNG VỚI DailyReward)
                        List<DailyReward> rewards = new ArrayList<>();

                        int dayIndex = 1;

                        for (DailyRewardResponse.DailyRewardData item : body.data) {
                            DailyReward reward = new DailyReward();

                            reward.setDayNumber(dayIndex++);   // Ngày 1,2,3,...
                            reward.setReward(item.GiaTri);     // Điểm thưởng
                            reward.setClaimed(body.claimed);   // Đã nhận hôm nay hay chưa

                            // 🔥 đánh dấu ngày hôm nay (cho UI)
                            reward.setToday(!body.claimed);

                            rewards.add(reward);
                        }


                        // 👉 canClaimToday = chưa claim
                        boolean canClaimToday = !body.claimed;

                        callback.onSuccess(rewards, canClaimToday);
                    }

                    @Override
                    public void onFailure(Call<DailyRewardResponse> call, Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }
    // ================= OFFLINE ACHIEVEMENTS =================

    public List<Achievement> getAchievements() {
        List<Achievement> achievements = new ArrayList<>();

        // 🔓 Achievement đã mở (giả lập offline)
        achievements.add(new Achievement(
                1,
                "🎯 Người mới bắt đầu",
                "Hoàn thành quiz đầu tiên",
                true,
                "🎯",
                1,
                1
        ));

        achievements.add(new Achievement(
                2,
                "📚 Học sinh chăm chỉ",
                "Hoàn thành 5 quiz",
                true,
                "📚",
                5,
                5
        ));

        achievements.add(new Achievement(
                3,
                "💯 Hoàn hảo",
                "Đạt điểm tuyệt đối",
                true,
                "💯",
                1,
                1
        ));

        // 🔒 Achievement chưa mở
        achievements.add(new Achievement(
                4,
                "🎓 Thạc sĩ tri thức",
                "Hoàn thành 10 quiz",
                false,
                "🎓",
                7,
                10
        ));

        achievements.add(new Achievement(
                5,
                "🥇 Chuyên gia",
                "Đạt điểm trung bình trên 80",
                false,
                "🥇",
                75,
                80
        ));

        achievements.add(new Achievement(
                6,
                "🏆 Bậc thầy",
                "Đạt điểm trung bình trên 90",
                false,
                "🏆",
                75,
                90
        ));

        achievements.add(new Achievement(
                7,
                "⭐ Siêu sao",
                "Đạt điểm tuyệt đối 3 lần",
                false,
                "⭐",
                1,
                3
        ));

        achievements.add(new Achievement(
                8,
                "🚀 Chinh phục viên",
                "Hoàn thành 20 quiz",
                false,
                "🚀",
                7,
                20
        ));

        return achievements;
    }
    // ================= OFFLINE STATS =================

    public List<UserStats> getStatsMilestones() {
        List<UserStats> stats = new ArrayList<>();

        stats.add(new UserStats(
                "Tổng điểm",
                "Tổng điểm tích lũy trong tuần",
                "4100",
                "🎯"
        ));

        stats.add(new UserStats(
                "Điểm trung bình",
                "Điểm trung bình mỗi ngày",
                "820",
                "📊"
        ));

        stats.add(new UserStats(
                "Chuỗi ngày",
                "Số ngày chơi liên tiếp",
                "5 ngày",
                "🔥"
        ));

        stats.add(new UserStats(
                "Thành tựu",
                "Số thành tựu đã mở khóa",
                "3",
                "🏆"
        ));

        stats.add(new UserStats(
                "Ngày hoàn thành",
                "Số ngày đã chơi trong tuần",
                "5/7",
                "📅"
        ));

        return stats;
    }




}
