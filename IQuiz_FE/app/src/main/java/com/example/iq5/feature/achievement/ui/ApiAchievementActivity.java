package com.example.iq5.feature.achievement.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.R;
import com.example.iq5.core.network.AchievementApiService;
import com.example.iq5.data.repository.AchievementApiRepository;
import com.example.iq5.utils.ApiHelper;

import java.util.List;

/**
 * Achievement Activity sử dụng API thật từ backend
 * Hiển thị thành tựu dựa trên thống kê thực từ database
 */
public class ApiAchievementActivity extends AppCompatActivity {

    private static final String TAG = "ApiAchievementActivity";
    
    // UI Components
    private TextView tvTitle;
    private TextView tvStats;
    private LinearLayout layoutAchievements;
    private TextView tvStreak;
    
    // Repository
    private AchievementApiRepository achievementRepository;
    
    // Data
    private List<AchievementApiService.Achievement> achievements;
    private AchievementApiService.StreakResponse currentStreak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Tạo layout đơn giản bằng code
        createSimpleLayout();

        initRepository();
        loadData();
        
        Toast.makeText(this, "🏆 Đang tải thành tựu từ API...", 
            Toast.LENGTH_SHORT).show();
    }
    
    /**
     * Tạo layout đơn giản bằng code
     */
    private void createSimpleLayout() {
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(32, 32, 32, 32);
        
        // Title
        tvTitle = new TextView(this);
        tvTitle.setText("🏆 THÀNH TỰU CỦA BẠN");
        tvTitle.setTextSize(20);
        tvTitle.setPadding(0, 0, 0, 16);
        mainLayout.addView(tvTitle);
        
        // Stats
        tvStats = new TextView(this);
        tvStats.setText("📊 Đang tải thống kê...");
        tvStats.setTextSize(14);
        tvStats.setPadding(0, 0, 0, 16);
        mainLayout.addView(tvStats);
        
        // Streak
        tvStreak = new TextView(this);
        tvStreak.setText("🔥 Đang tải streak...");
        tvStreak.setTextSize(14);
        tvStreak.setPadding(0, 0, 0, 16);
        mainLayout.addView(tvStreak);
        
        // Achievements container
        layoutAchievements = new LinearLayout(this);
        layoutAchievements.setOrientation(LinearLayout.VERTICAL);
        mainLayout.addView(layoutAchievements);
        
        setContentView(mainLayout);
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
                    displayAchievements(achievementList);
                    
                    Log.d(TAG, "✅ Loaded " + achievementList.size() + " achievements");
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
                    
                    // Hiển thị fallback
                    tvStats.setText("❌ Không thể tải thành tựu: " + error);
                });
            }
        });
    }
    
    /**
     * Hiển thị danh sách thành tựu
     */
    private void displayAchievements(List<AchievementApiService.Achievement> achievementList) {
        if (achievementList == null || achievementList.isEmpty()) {
            tvStats.setText("📊 Chưa có thành tựu nào. Hãy hoàn thành quiz để mở khóa!");
            return;
        }
        
        // Đếm thành tựu đã mở khóa
        int unlockedCount = 0;
        for (AchievementApiService.Achievement achievement : achievementList) {
            if (achievement.isUnlocked()) {
                unlockedCount++;
            }
        }
        
        tvStats.setText(String.format("📊 Đã mở khóa: %d/%d thành tựu", 
            unlockedCount, achievementList.size()));
        
        // Hiển thị từng thành tựu
        layoutAchievements.removeAllViews();
        
        for (AchievementApiService.Achievement achievement : achievementList) {
            TextView tvAchievement = new TextView(this);
            
            String status = achievement.isUnlocked() ? "✅" : "🔒";
            String text = String.format("%s %s %s\n   %s\n   %s", 
                status,
                achievement.getIcon() != null ? achievement.getIcon() : "🏆",
                achievement.getTenThanhTuu(),
                achievement.getMoTa(),
                achievement.getRequirement() != null ? achievement.getRequirement() : ""
            );
            
            tvAchievement.setText(text);
            tvAchievement.setTextSize(12);
            tvAchievement.setPadding(16, 8, 16, 8);
            
            // Màu sắc khác nhau cho thành tựu đã/chưa mở khóa
            if (achievement.isUnlocked()) {
                tvAchievement.setBackgroundColor(0xFF4CAF50); // Green
                tvAchievement.setTextColor(0xFFFFFFFF); // White text
            } else {
                tvAchievement.setBackgroundColor(0xFFE0E0E0); // Gray
                tvAchievement.setTextColor(0xFF666666); // Dark gray text
            }
            
            layoutAchievements.addView(tvAchievement);
            
            // Margin
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvAchievement.getLayoutParams();
            params.setMargins(0, 8, 0, 8);
            tvAchievement.setLayoutParams(params);
        }
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
                    
                    String message = String.format("🔥 Chuỗi ngày: %d ngày liên tiếp", 
                        streak.getSoNgayLienTiep());
                    tvStreak.setText(message);
                    
                    Log.d(TAG, "✅ Loaded streak: " + streak.getSoNgayLienTiep() + " days");
                });
            }
            
            @Override
            public void onUnauthorized() {
                runOnUiThread(() -> {
                    tvStreak.setText("❌ Không thể tải streak - Token hết hạn");
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    tvStreak.setText("🔥 Chuỗi ngày: 0 ngày (Lỗi: " + error + ")");
                });
            }
        });
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}