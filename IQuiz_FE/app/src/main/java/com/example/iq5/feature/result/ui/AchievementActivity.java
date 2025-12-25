package com.example.iq5.feature.result.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.iq5.R;
import com.example.iq5.feature.result.adapter.AchievementAdapter;
import com.example.iq5.feature.result.data.ResultRepository;
import com.example.iq5.feature.result.model.Achievement;
import java.util.ArrayList;
import java.util.List;

public class AchievementActivity extends AppCompatActivity {

    private static final String TAG = "AchievementActivity";

    // UI Components
    private RecyclerView rvUnlocked, rvLocked;
    private TextView tvUnlockedCount, tvTotalCount, tvPercentComplete;
    private ImageView btnBack;
    private ResultRepository repository;

    // Views cho Cột mốc tiếp theo
    private CardView cardNextMilestone;
    private TextView tvMilestoneTitle, tvMilestoneProgress, tvMilestoneReward, tvMilestoneTarget;
    private ProgressBar pbMilestoneProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        // 1. Ánh xạ View
        initViews();

        // 2. Khởi tạo Repository
        repository = new ResultRepository(this);

        // 3. Load achievements với stats thật
        loadAchievementsWithRealStats();

        // 4. Back button
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
        
        // 5. Debug shortcuts
        if (tvUnlockedCount != null) {
            tvUnlockedCount.setOnLongClickListener(v -> {
                com.example.iq5.utils.DebugHelper.openDebugAchievement(this);
                return true;
            });
        }
        
        if (tvTotalCount != null) {
            tvTotalCount.setOnLongClickListener(v -> {
                com.example.iq5.utils.DebugHelper.openNetworkTest(this);
                return true;
            });
        }
    }

    private void initViews() {
        rvUnlocked = findViewById(R.id.rv_achievements_unlocked);
        rvLocked = findViewById(R.id.rv_achievements_locked);
        tvUnlockedCount = findViewById(R.id.tv_unlocked_count);
        tvTotalCount = findViewById(R.id.tv_total_count);
        tvPercentComplete = findViewById(R.id.tv_percent_complete);
        btnBack = findViewById(R.id.btn_back_achieve);

        cardNextMilestone = findViewById(R.id.card_next_milestone);
        tvMilestoneTitle = findViewById(R.id.tv_milestone_title);
        tvMilestoneProgress = findViewById(R.id.tv_milestone_progress);
        tvMilestoneReward = findViewById(R.id.tv_milestone_reward);
        tvMilestoneTarget = findViewById(R.id.tv_milestone_target);
        pbMilestoneProgress = findViewById(R.id.pb_milestone_progress);
    }

    /**
     * Load achievements với stats thật từ SharedPreferences
     */
    private void loadAchievementsWithRealStats() {
        Log.d(TAG, "🏆 Loading achievements with real stats...");
        
        try {
            // Get real stats from SharedPreferences
            android.content.SharedPreferences prefs = getSharedPreferences("quiz_stats", MODE_PRIVATE);
            int totalQuizzes = prefs.getInt("total_quizzes", 0);
            int totalCorrect = prefs.getInt("total_correct", 0);
            int perfectScores = prefs.getInt("perfect_scores", 0);
            int totalScore = prefs.getInt("total_score", 0);
            
            double averageScore = totalQuizzes > 0 ? (double) totalScore / totalQuizzes : 0.0;
            
            Log.d(TAG, String.format("📊 Real stats: %d quizzes, %.1f avg, %d perfect", 
                totalQuizzes, averageScore, perfectScores));
            
            // Generate achievements based on real stats
            List<Achievement> achievements = createAchievementsFromStats(totalQuizzes, averageScore, perfectScores);
            
            // Show success message
            Toast.makeText(this, 
                String.format("✅ Thành tựu real-time: %d quiz đã chơi", totalQuizzes), 
                Toast.LENGTH_SHORT).show();
            
            // Display achievements
            displayAchievements(achievements);
            
        } catch (Exception e) {
            Log.e(TAG, "❌ Error loading real stats: " + e.getMessage());
            Toast.makeText(this, "❌ Lỗi tải stats: " + e.getMessage(), 
                Toast.LENGTH_LONG).show();
            
            // Fallback to default achievements
            List<Achievement> defaultAchievements = createDefaultAchievements();
            displayAchievements(defaultAchievements);
        }
    }
    
    /**
     * Tạo thành tựu dựa trên stats thật
     */
    private List<Achievement> createAchievementsFromStats(int totalQuizzes, double averageScore, int perfectScores) {
        List<Achievement> achievements = new ArrayList<>();
        
        // 1. First Quiz
        achievements.add(new Achievement(1, "🎯 Người mới bắt đầu", 
            "Hoàn thành quiz đầu tiên", 
            totalQuizzes >= 1, "🎯", 
            Math.min(totalQuizzes, 1), 1));
        
        // 2. 5 Quizzes
        achievements.add(new Achievement(2, "📚 Học sinh chăm chỉ", 
            "Hoàn thành 5 quiz", 
            totalQuizzes >= 5, "📚", 
            Math.min(totalQuizzes, 5), 5));
        
        // 3. 10 Quizzes
        achievements.add(new Achievement(3, "🎓 Thạc sĩ tri thức", 
            "Hoàn thành 10 quiz", 
            totalQuizzes >= 10, "🎓", 
            Math.min(totalQuizzes, 10), 10));
        
        // 4. High Average 80
        achievements.add(new Achievement(4, "🥇 Chuyên gia", 
            "Đạt điểm trung bình trên 80", 
            averageScore >= 80, "🥇", 
            (int) Math.min(averageScore, 80), 80));
        
        // 5. High Average 90
        achievements.add(new Achievement(5, "🏆 Bậc thầy", 
            "Đạt điểm trung bình trên 90", 
            averageScore >= 90, "🏆", 
            (int) Math.min(averageScore, 90), 90));
        
        // 6. First Perfect Score
        achievements.add(new Achievement(6, "💯 Hoàn hảo", 
            "Đạt điểm tuyệt đối lần đầu", 
            perfectScores >= 1, "💯", 
            Math.min(perfectScores, 1), 1));
        
        // 7. 3 Perfect Scores
        achievements.add(new Achievement(7, "⭐ Siêu sao", 
            "Đạt điểm tuyệt đối 3 lần", 
            perfectScores >= 3, "⭐", 
            Math.min(perfectScores, 3), 3));
        
        // 8. 20 Quizzes (long term goal)
        achievements.add(new Achievement(8, "🚀 Chinh phục viên", 
            "Hoàn thành 20 quiz", 
            totalQuizzes >= 20, "🚀", 
            Math.min(totalQuizzes, 20), 20));
        
        Log.d(TAG, String.format("✅ Generated %d achievements from real stats", achievements.size()));
        return achievements;
    }

    /**
     * Tạo thành tựu mặc định khi không có dữ liệu
     */
    private List<Achievement> createDefaultAchievements() {
        List<Achievement> achievements = new ArrayList<>();
        
        // Thành tựu đã mở khóa (giả lập dựa trên việc user đã chơi)
        achievements.add(new Achievement(1, "🎯 Người mới bắt đầu", 
            "Hoàn thành quiz đầu tiên", true, "🎯", 100, 100));
        achievements.add(new Achievement(2, "📚 Học sinh chăm chỉ", 
            "Hoàn thành 5 quiz", true, "📚", 100, 100));
        achievements.add(new Achievement(3, "💯 Hoàn hảo", 
            "Đạt điểm tuyệt đối lần đầu", true, "💯", 100, 100));
            
        // Thành tựu chưa mở khóa với progress thực tế
        achievements.add(new Achievement(4, "🎓 Thạc sĩ tri thức", 
            "Hoàn thành 10 quiz", false, "🎓", 7, 10));
        achievements.add(new Achievement(5, "🥇 Chuyên gia", 
            "Đạt điểm trung bình trên 80", false, "🥇", 75, 80));
        achievements.add(new Achievement(6, "🏆 Bậc thầy", 
            "Đạt điểm trung bình trên 90", false, "🏆", 75, 90));
        achievements.add(new Achievement(7, "⭐ Siêu sao", 
            "Đạt điểm tuyệt đối 3 lần", false, "⭐", 1, 3));
        achievements.add(new Achievement(8, "🚀 Chinh phục viên", 
            "Hoàn thành 20 quiz", false, "🚀", 7, 20));
            
        Log.d(TAG, "✅ Created " + achievements.size() + " default achievements");
        return achievements;
    }

    /**
     * Display achievements after loading
     */
    private void displayAchievements(List<Achievement> allAchievements) {
        Log.d(TAG, "Displaying " + allAchievements.size() + " achievements");

        if (allAchievements.isEmpty()) {
            Log.e(TAG, "❌ No achievements to display!");
            if (cardNextMilestone != null) {
                cardNextMilestone.setVisibility(View.GONE);
            }
            return;
        }

        List<Achievement> unlockedList = new ArrayList<>();
        List<Achievement> lockedList = new ArrayList<>();

        for (Achievement ach : allAchievements) {
            if (ach.isUnlocked()) {
                unlockedList.add(ach);
            } else {
                lockedList.add(ach);
            }
        }

        updateStats(allAchievements.size(), unlockedList.size());
        setupRecyclerView(rvUnlocked, unlockedList);
        setupRecyclerView(rvLocked, lockedList);
        showNextMilestone(lockedList);
    }

    private void updateStats(int total, int unlocked) {
        int percent = total > 0 ? (int) ((unlocked * 100.0) / total) : 0;

        if (tvUnlockedCount != null) tvUnlockedCount.setText(String.valueOf(unlocked));
        if (tvTotalCount != null) tvTotalCount.setText(String.valueOf(total));
        if (tvPercentComplete != null) tvPercentComplete.setText(percent + "%");
    }

    private void setupRecyclerView(RecyclerView recyclerView, List<Achievement> list) {
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            AchievementAdapter adapter = new AchievementAdapter(list, this);
            recyclerView.setAdapter(adapter);
        }
    }

    private void showNextMilestone(List<Achievement> lockedList) {
        if (cardNextMilestone == null) return;

        if (lockedList.isEmpty()) {
            cardNextMilestone.setVisibility(View.GONE);
            return;
        }
        Achievement nextAchievement = null;
        float maxProgress = -1;

        for (Achievement achievement : lockedList) {
            if (achievement.getTargetProgress() <= 0) continue;

            float progress = (float) achievement.getCurrentProgress() / achievement.getTargetProgress();
            if (progress > maxProgress) {
                maxProgress = progress;
                nextAchievement = achievement;
            }
        }

        if (nextAchievement != null) {
            cardNextMilestone.setVisibility(View.VISIBLE);

            String unit = getUnitForAchievement(nextAchievement);

            if (tvMilestoneTitle != null) {
                tvMilestoneTitle.setText(nextAchievement.getTitle());
            }
            if (tvMilestoneProgress != null) {
                tvMilestoneProgress.setText("Hiện tại: " + nextAchievement.getCurrentProgress() + " " + unit);
            }
            if (tvMilestoneTarget != null) {
                tvMilestoneTarget.setText(nextAchievement.getTargetProgress() + " " + unit);
            }

            int progressPercent = (int) (maxProgress * 100);
            if (pbMilestoneProgress != null) {
                pbMilestoneProgress.setProgress(progressPercent);
            }

            int xpReward = calculateXPReward(nextAchievement);
            if (tvMilestoneReward != null) {
                tvMilestoneReward.setText("+" + xpReward + " XP");
            }
        } else {
            cardNextMilestone.setVisibility(View.GONE);
        }
    }

    private String getUnitForAchievement(Achievement achievement) {
        String title = achievement.getTitle().toLowerCase();
        if (title.contains("streak") || title.contains("ngày")) {
            return "ngày";
        } else if (title.contains("câu")) {
            return "câu";
        } else if (title.contains("trận")) {
            return "trận";
        } else if (title.contains("điểm")) {
            return "điểm";
        } else if (title.contains("lần")) {
            return "lần";
        }
        return "";
    }
    private int calculateXPReward(Achievement achievement) {
        int target = achievement.getTargetProgress();
        if (target >= 100) return 500;
        if (target >= 50) return 300;
        if (target >= 20) return 200;
        if (target >= 10) return 150;
        if (target >= 5) return 100;
        return 50;
    }
}