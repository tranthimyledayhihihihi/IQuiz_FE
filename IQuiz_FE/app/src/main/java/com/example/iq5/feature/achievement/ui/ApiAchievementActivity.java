package com.example.iq5.feature.achievement.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.core.network.AchievementApiService;
import com.example.iq5.data.repository.AchievementApiRepository;
import com.example.iq5.utils.ApiHelper;

import java.util.List;

/**
 * Achievement Activity sử dụng API thật từ backend
 * Version đơn giản không phụ thuộc vào layout phức tạp
 */
public class ApiAchievementActivity extends AppCompatActivity {

    private static final String TAG = "ApiAchievementActivity";
    
    // Repository
    private AchievementApiRepository achievementRepository;
    
    // Data
    private List<AchievementApiService.Achievement> achievements;
    private AchievementApiService.StreakResponse currentStreak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Sử dụng layout Android mặc định
        setContentView(android.R.layout.activity_list_item);

        initRepository();
        loadData();
        
        Toast.makeText(this, "🏆 Achievement Activity - Đang tải dữ liệu từ API...", 
            Toast.LENGTH_LONG).show();
    }
    
    private void initRepository() {
        achievementRepository = new AchievementApiRepository(this);
    }
    
    /**
     * Load tất cả dữ liệu achievement
     */
    private void loadData() {
        loadAchievements();
        loadStreak();
    }
    
    /**
     * Load danh sách thành tựu
     */
    private void loadAchievements() {
        achievementRepository.getMyAchievements(new AchievementApiRepository.AchievementsCallback() {
            @Override
            public void onSuccess(List<AchievementApiService.Achievement> achievementList) {
                runOnUiThread(() -> {
                    achievements = achievementList;
                    
                    String message = "✅ Đã tải " + achievementList.size() + " thành tựu từ API!";
                    Toast.makeText(ApiAchievementActivity.this, message, Toast.LENGTH_LONG).show();
                    
                    // Hiển thị thành tựu đầu tiên nếu có
                    if (!achievementList.isEmpty()) {
                        AchievementApiService.Achievement first = achievementList.get(0);
                        Toast.makeText(ApiAchievementActivity.this, 
                            "🏆 Thành tựu đầu tiên: " + first.getTenThanhTuu(), 
                            Toast.LENGTH_LONG).show();
                    }
                });
            }
            
            @Override
            public void onUnauthorized() {
                runOnUiThread(() -> {
                    Toast.makeText(ApiAchievementActivity.this, 
                        "❌ Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show();
                    
                    // Clear token và quay về login
                    ApiHelper.clearToken(ApiAchievementActivity.this);
                    finish();
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(ApiAchievementActivity.this, 
                        "❌ Lỗi tải thành tựu: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }
    
    /**
     * Load thông tin streak
     */
    private void loadStreak() {
        achievementRepository.getMyStreak(new AchievementApiRepository.StreakCallback() {
            @Override
            public void onSuccess(AchievementApiService.StreakResponse streak) {
                runOnUiThread(() -> {
                    currentStreak = streak;
                    
                    String message = "🔥 Streak: " + streak.getSoNgayLienTiep() + " ngày liên tiếp";
                    Toast.makeText(ApiAchievementActivity.this, message, Toast.LENGTH_LONG).show();
                });
            }
            
            @Override
            public void onUnauthorized() {
                runOnUiThread(() -> {
                    Toast.makeText(ApiAchievementActivity.this, 
                        "❌ Không thể tải streak - Token hết hạn", Toast.LENGTH_SHORT).show();
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(ApiAchievementActivity.this, 
                        "❌ Lỗi tải streak: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    /**
     * Nhận thưởng hàng ngày (có thể gọi từ menu hoặc button)
     */
    public void claimDailyReward() {
        achievementRepository.claimDailyReward(new AchievementApiRepository.DailyRewardCallback() {
            @Override
            public void onSuccess(boolean awarded, String message) {
                runOnUiThread(() -> {
                    if (awarded) {
                        Toast.makeText(ApiAchievementActivity.this, 
                            "🎁 " + message, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ApiAchievementActivity.this, 
                            "ℹ️ " + message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            
            @Override
            public void onAlreadyClaimed() {
                runOnUiThread(() -> {
                    Toast.makeText(ApiAchievementActivity.this, 
                        "ℹ️ Bạn đã nhận thưởng hôm nay rồi!", Toast.LENGTH_SHORT).show();
                });
            }
            
            @Override
            public void onUnauthorized() {
                runOnUiThread(() -> {
                    Toast.makeText(ApiAchievementActivity.this, 
                        "❌ Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show();
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(ApiAchievementActivity.this, 
                        "❌ Lỗi nhận thưởng: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "👋 Thoát Achievement Activity", Toast.LENGTH_SHORT).show();
    }
}