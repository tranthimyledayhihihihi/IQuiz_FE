package com.example.iq5.feature.achievement.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.data.model.AchievementsResponse;
import com.example.iq5.data.repository.AchievementApiRepository;

import java.util.List;

/**
 * Simple Achievement Activity
 * ONLY load achievements (đúng theo AchievementApiRepository hiện tại)
 */
public class SimpleApiAchievementActivity extends AppCompatActivity {

    private AchievementApiRepository achievementRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Layout đơn giản
        setContentView(android.R.layout.simple_list_item_1);

        achievementRepository = new AchievementApiRepository(this);

        loadAchievements();

        Toast.makeText(this,
                "🏆 Đang tải danh sách thành tựu...",
                Toast.LENGTH_SHORT).show();
    }

    private void loadAchievements() {
        achievementRepository.getMyAchievements(
                new AchievementApiRepository.AchievementsCallback() {

                    @Override
                    public void onSuccess(
                            List<AchievementsResponse.Achievement> list) {

                        runOnUiThread(() -> {
                            if (list.isEmpty()) {
                                Toast.makeText(
                                        SimpleApiAchievementActivity.this,
                                        "Chưa có thành tựu nào",
                                        Toast.LENGTH_LONG
                                ).show();
                                return;
                            }

                            // Hiển thị thành tựu đầu tiên để test
                            AchievementsResponse.Achievement first = list.get(0);

                            Toast.makeText(
                                    SimpleApiAchievementActivity.this,
                                    "🏆 " + first.tenThanhTuu +
                                            " (+" + first.diemThuong + ")",
                                    Toast.LENGTH_LONG
                            ).show();
                        });
                    }

                    @Override
                    public void onUnauthorized() {
                        runOnUiThread(() -> {
                            Toast.makeText(
                                    SimpleApiAchievementActivity.this,
                                    "❌ Token hết hạn – vui lòng đăng nhập lại",
                                    Toast.LENGTH_LONG
                            ).show();
                            finish();
                        });
                    }

                    @Override
                    public void onError(String error) {
                        runOnUiThread(() -> {
                            Toast.makeText(
                                    SimpleApiAchievementActivity.this,
                                    "❌ Lỗi: " + error,
                                    Toast.LENGTH_LONG
                            ).show();
                        });
                    }
                }
        );
    }
}
