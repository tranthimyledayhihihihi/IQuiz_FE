package com.example.iq5.feature.result.ui;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.core.network.ApiClient;
import com.example.iq5.core.network.UserApiService;
import com.example.iq5.core.prefs.PrefsManager;
import com.example.iq5.data.model.UserProfileModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * API Stats Activity - Hiển thị thống kê thực từ backend
 */
public class ApiStatsActivity extends AppCompatActivity {

    private static final String TAG = "ApiStatsActivity";
    
    private LinearLayout containerLayout;
    private UserApiService userApiService;
    
    // UI Components
    private TextView tvTotalScore, tvAverageScore, tvDaysCompleted, tvStreakCount;
    private LinearLayout achievementsContainer, recordsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        createBeautifulUI();
        initApiService();
        loadUserStats();
    }

    private void createBeautifulUI() {
        // Main container
        ScrollView scrollView = new ScrollView(this);
        scrollView.setBackgroundColor(0xFFF5F5F5);
        
        containerLayout = new LinearLayout(this);
        containerLayout.setOrientation(LinearLayout.VERTICAL);
        containerLayout.setPadding(24, 24, 24, 24);
        
        // Back button and title
        createHeader();
        
        // Weekly stats section
        createWeeklyStatsSection();
        
        // Records and achievements section
        createRecordsSection();
        
        scrollView.addView(containerLayout);
        setContentView(scrollView);
    }

    private void createHeader() {
        // Back button
        TextView backButton = new TextView(this);
        backButton.setText("← Biểu đồ tiến độ");
        backButton.setTextSize(18);
        backButton.setTextColor(0xFF333333);
        backButton.setPadding(0, 0, 0, 16);
        backButton.setOnClickListener(v -> finish());
        containerLayout.addView(backButton);
        
        // This week section
        LinearLayout weekSection = new LinearLayout(this);
        weekSection.setOrientation(LinearLayout.HORIZONTAL);
        weekSection.setGravity(Gravity.CENTER_VERTICAL);
        weekSection.setPadding(0, 16, 0, 24);
        
        TextView weekTitle = new TextView(this);
        weekTitle.setText("Tuần này");
        weekTitle.setTextSize(20);
        weekTitle.setTextColor(0xFF333333);
        weekTitle.setLayoutParams(new LinearLayout.LayoutParams(0, 
            LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        weekSection.addView(weekTitle);
        
        // Streak indicator
        tvStreakCount = new TextView(this);
        tvStreakCount.setText("🔥 Streak: 0");
        tvStreakCount.setTextSize(16);
        tvStreakCount.setTextColor(0xFFFF5722);
        weekSection.addView(tvStreakCount);
        
        containerLayout.addView(weekSection);
    }

    private void createWeeklyStatsSection() {
        // Stats container
        LinearLayout statsContainer = new LinearLayout(this);
        statsContainer.setOrientation(LinearLayout.HORIZONTAL);
        statsContainer.setPadding(16, 24, 16, 24);
        
        // Create rounded background
        GradientDrawable statsBackground = new GradientDrawable();
        statsBackground.setColor(Color.WHITE);
        statsBackground.setCornerRadius(20);
        statsBackground.setStroke(1, 0xFFE0E0E0);
        statsContainer.setBackground(statsBackground);
        
        LinearLayout.LayoutParams statsParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        statsParams.setMargins(0, 0, 0, 32);
        statsContainer.setLayoutParams(statsParams);
        
        // Total Score
        tvTotalScore = createStatCard("0", "Tổng điểm", 0xFFE3F2FD);
        LinearLayout.LayoutParams scoreParams = new LinearLayout.LayoutParams(0, 
            LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        scoreParams.setMargins(0, 0, 8, 0);
        tvTotalScore.setLayoutParams(scoreParams);
        statsContainer.addView(tvTotalScore);
        
        // Average Score
        tvAverageScore = createStatCard("0", "Trung bình", 0xFFF3E5F5);
        LinearLayout.LayoutParams avgParams = new LinearLayout.LayoutParams(0, 
            LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        avgParams.setMargins(8, 0, 8, 0);
        tvAverageScore.setLayoutParams(avgParams);
        statsContainer.addView(tvAverageScore);
        
        // Days Completed
        tvDaysCompleted = createStatCard("0/7", "Ngày hoàn thành", 0xFFE8F5E8);
        LinearLayout.LayoutParams daysParams = new LinearLayout.LayoutParams(0, 
            LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        daysParams.setMargins(8, 0, 0, 0);
        tvDaysCompleted.setLayoutParams(daysParams);
        statsContainer.addView(tvDaysCompleted);
        
        containerLayout.addView(statsContainer);
    }

    private TextView createStatCard(String value, String label, int backgroundColor) {
        LinearLayout cardContainer = new LinearLayout(this);
        cardContainer.setOrientation(LinearLayout.VERTICAL);
        cardContainer.setGravity(Gravity.CENTER);
        cardContainer.setPadding(16, 20, 16, 20);
        
        // Create rounded background
        GradientDrawable cardBackground = new GradientDrawable();
        cardBackground.setColor(backgroundColor);
        cardBackground.setCornerRadius(16);
        cardContainer.setBackground(cardBackground);
        
        // Value text
        TextView valueText = new TextView(this);
        valueText.setText(value);
        valueText.setTextSize(24);
        valueText.setTextColor(0xFF333333);
        valueText.setGravity(Gravity.CENTER);
        cardContainer.addView(valueText);
        
        // Label text
        TextView labelText = new TextView(this);
        labelText.setText(label);
        labelText.setTextSize(12);
        labelText.setTextColor(0xFF666666);
        labelText.setGravity(Gravity.CENTER);
        labelText.setPadding(0, 4, 0, 0);
        cardContainer.addView(labelText);
        
        // Return the value text for updating
        return valueText;
    }

    private void createRecordsSection() {
        // Section title
        TextView recordsTitle = new TextView(this);
        recordsTitle.setText("Các Kỷ Lục và Mốc Cá Nhân");
        recordsTitle.setTextSize(18);
        recordsTitle.setTextColor(0xFF333333);
        recordsTitle.setPadding(0, 0, 0, 16);
        containerLayout.addView(recordsTitle);
        
        // Records container
        recordsContainer = new LinearLayout(this);
        recordsContainer.setOrientation(LinearLayout.VERTICAL);
        recordsContainer.setPadding(16, 16, 16, 16);
        
        GradientDrawable recordsBackground = new GradientDrawable();
        recordsBackground.setColor(Color.WHITE);
        recordsBackground.setCornerRadius(16);
        recordsBackground.setStroke(1, 0xFFE0E0E0);
        recordsContainer.setBackground(recordsBackground);
        
        // Loading text
        TextView loadingText = new TextView(this);
        loadingText.setText("🔄 Đang tải kỷ lục...");
        loadingText.setTextSize(14);
        loadingText.setTextColor(0xFF666666);
        loadingText.setGravity(Gravity.CENTER);
        loadingText.setPadding(0, 20, 0, 20);
        recordsContainer.addView(loadingText);
        
        containerLayout.addView(recordsContainer);
    }

    private void initApiService() {
        try {
            PrefsManager prefsManager = new PrefsManager(this);
            Retrofit retrofit = ApiClient.getClient(prefsManager);
            userApiService = retrofit.create(UserApiService.class);
            Log.d(TAG, "✅ API Service initialized");
        } catch (Exception e) {
            Log.e(TAG, "❌ Failed to init API service", e);
            showError("Không thể khởi tạo dịch vụ API");
        }
    }

    private void loadUserStats() {
        Log.d(TAG, "📊 Loading user stats from API...");
        
        if (userApiService == null) {
            showError("API service chưa được khởi tạo");
            return;
        }
        
        Call<UserProfileModel> call = userApiService.getMyProfile();
        
        call.enqueue(new Callback<UserProfileModel>() {
            @Override
            public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Stats loaded successfully");
                    displayUserStats(response.body());
                } else {
                    Log.e(TAG, "❌ Failed to load stats: " + response.code());
                    showError("Không thể tải thống kê người dùng");
                    displayDefaultStats();
                }
            }

            @Override
            public void onFailure(Call<UserProfileModel> call, Throwable t) {
                Log.e(TAG, "❌ Network error loading stats", t);
                showError("Lỗi kết nối: " + t.getMessage());
                displayDefaultStats();
            }
        });
    }

    private void displayUserStats(UserProfileModel profile) {
        Log.d(TAG, "📊 Displaying user stats");
        
        if (profile.getThongKe() != null) {
            UserProfileModel.ThongKeModel stats = profile.getThongKe();
            
            // Update weekly stats
            int totalQuizzes = stats.getSoBaiQuizHoanThanh();
            double avgScore = stats.getDiemTrungBinh();
            double accuracy = stats.getTyLeDung();
            
            // Calculate total score (estimated)
            int totalScore = (int)(totalQuizzes * avgScore);
            
            // Update UI
            tvTotalScore.setText(String.valueOf(totalScore));
            tvAverageScore.setText(String.format("%.0f", avgScore));
            tvDaysCompleted.setText(totalQuizzes + "/7");
            
            // Update streak (mock for now)
            tvStreakCount.setText("🔥 Streak: " + Math.min(totalQuizzes, 7));
            
            // Display records and achievements
            displayRecordsAndAchievements(stats);
            
            Toast.makeText(this, "Đã tải thống kê từ server", Toast.LENGTH_SHORT).show();
        } else {
            displayDefaultStats();
        }
    }

    private void displayRecordsAndAchievements(UserProfileModel.ThongKeModel stats) {
        // Clear loading text
        recordsContainer.removeAllViews();
        
        // Personal records
        addRecordItem("🏆 Tổng số quiz hoàn thành", String.valueOf(stats.getSoBaiQuizHoanThanh()));
        addRecordItem("📊 Điểm trung bình cao nhất", String.format("%.1f", stats.getDiemTrungBinh()));
        addRecordItem("🎯 Độ chính xác", String.format("%.1f%%", stats.getTyLeDung()));
        addRecordItem("💯 Số câu trả lời đúng", String.valueOf(stats.getTongSoCauDung()));
        
        // Achievement milestones
        addSeparator();
        addRecordItem("🥉 Thành tựu đạt được", getAchievementLevel(stats.getSoBaiQuizHoanThanh()));
        addRecordItem("⭐ Cấp độ hiện tại", getUserLevel(stats.getDiemTrungBinh()));
        addRecordItem("🔥 Streak tốt nhất", Math.min(stats.getSoBaiQuizHoanThanh(), 10) + " ngày");
        
        // Progress to next milestone
        addSeparator();
        int nextMilestone = getNextMilestone(stats.getSoBaiQuizHoanThanh());
        if (nextMilestone > 0) {
            int remaining = nextMilestone - stats.getSoBaiQuizHoanThanh();
            addRecordItem("🎯 Mục tiêu tiếp theo", remaining + " quiz nữa để đạt " + nextMilestone);
        }
    }

    private void addRecordItem(String title, String value) {
        LinearLayout recordItem = new LinearLayout(this);
        recordItem.setOrientation(LinearLayout.HORIZONTAL);
        recordItem.setPadding(0, 12, 0, 12);
        recordItem.setGravity(Gravity.CENTER_VERTICAL);
        
        TextView titleText = new TextView(this);
        titleText.setText(title);
        titleText.setTextSize(14);
        titleText.setTextColor(0xFF333333);
        titleText.setLayoutParams(new LinearLayout.LayoutParams(0, 
            LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        recordItem.addView(titleText);
        
        TextView valueText = new TextView(this);
        valueText.setText(value);
        valueText.setTextSize(14);
        valueText.setTextColor(0xFF666666);
        valueText.setGravity(Gravity.END);
        recordItem.addView(valueText);
        
        recordsContainer.addView(recordItem);
    }

    private void addSeparator() {
        TextView separator = new TextView(this);
        separator.setHeight(1);
        separator.setBackgroundColor(0xFFE0E0E0);
        LinearLayout.LayoutParams sepParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 1);
        sepParams.setMargins(0, 8, 0, 8);
        separator.setLayoutParams(sepParams);
        recordsContainer.addView(separator);
    }

    private String getAchievementLevel(int quizCount) {
        if (quizCount >= 50) return "🏆 Huyền thoại";
        if (quizCount >= 20) return "🥇 Chuyên gia";
        if (quizCount >= 10) return "🥈 Thành thạo";
        if (quizCount >= 5) return "🥉 Tập sự";
        return "🆕 Người mới";
    }

    private String getUserLevel(double avgScore) {
        if (avgScore >= 90) return "Cấp 5 - Xuất sắc";
        if (avgScore >= 80) return "Cấp 4 - Giỏi";
        if (avgScore >= 70) return "Cấp 3 - Khá";
        if (avgScore >= 60) return "Cấp 2 - Trung bình";
        return "Cấp 1 - Mới bắt đầu";
    }

    private int getNextMilestone(int current) {
        int[] milestones = {5, 10, 20, 50, 100};
        for (int milestone : milestones) {
            if (current < milestone) {
                return milestone;
            }
        }
        return 0; // No more milestones
    }

    private void displayDefaultStats() {
        tvTotalScore.setText("0");
        tvAverageScore.setText("0");
        tvDaysCompleted.setText("0/7");
        tvStreakCount.setText("🔥 Streak: 0");
        
        // Clear records container and show message
        recordsContainer.removeAllViews();
        TextView noDataText = new TextView(this);
        noDataText.setText("📊 Chưa có dữ liệu thống kê\nHãy chơi quiz để xem tiến độ!");
        noDataText.setTextSize(14);
        noDataText.setTextColor(0xFF757575);
        noDataText.setGravity(Gravity.CENTER);
        noDataText.setPadding(0, 20, 0, 20);
        recordsContainer.addView(noDataText);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Log.e(TAG, "Error: " + message);
    }
}