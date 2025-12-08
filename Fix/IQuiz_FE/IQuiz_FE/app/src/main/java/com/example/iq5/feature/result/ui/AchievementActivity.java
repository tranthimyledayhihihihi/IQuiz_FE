package com.example.iq5.feature.result.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.iq5.R;
import com.example.iq5.core.navigation.NavigationHelper;
import com.example.iq5.feature.result.adapter.AchievementAdapter;
import com.example.iq5.feature.result.data.ResultRepository;
import com.example.iq5.feature.result.model.Achievement;
import java.util.ArrayList;
import java.util.List;

public class AchievementActivity extends AppCompatActivity {

    private static final String TAG = "AchievementActivity";

    // Khai báo Views đầy đủ (để tránh lỗi Cannot resolve symbol và NPE)
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

        // 3. Load dữ liệu
        loadAchievements();

        // 4. Back button - XỬ LÝ SỰ KIỆN QUAY LẠI MÀN HÌNH TRƯỚC
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                // Sử dụng finish() trực tiếp để đóng Activity hiện tại
                finish(); // <--- Dùng lệnh này thay thế NavigationHelper.goBack(this)
            });
        }
    }

    private void initViews() {
        // Ánh xạ các thành phần chính
        rvUnlocked = findViewById(R.id.rv_achievements_unlocked);
        rvLocked = findViewById(R.id.rv_achievements_locked);
        tvUnlockedCount = findViewById(R.id.tv_unlocked_count);
        tvTotalCount = findViewById(R.id.tv_total_count);
        tvPercentComplete = findViewById(R.id.tv_percent_complete);
        btnBack = findViewById(R.id.btn_back_achieve); // Ánh xạ nút Back

        // Ánh xạ các thành phần cột mốc tiếp theo
        cardNextMilestone = findViewById(R.id.card_next_milestone);
        tvMilestoneTitle = findViewById(R.id.tv_milestone_title);
        tvMilestoneProgress = findViewById(R.id.tv_milestone_progress);
        tvMilestoneReward = findViewById(R.id.tv_milestone_reward);
        tvMilestoneTarget = findViewById(R.id.tv_milestone_target);
        pbMilestoneProgress = findViewById(R.id.pb_milestone_progress);
    }

    private void loadAchievements() {
        List<Achievement> allAchievements = repository.getAchievements();

        Log.d(TAG, "Total achievements loaded: " + allAchievements.size());

        if (allAchievements.isEmpty()) {
            Log.e(TAG, "❌ No achievements loaded! Check JSON file path and format");
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
            // Đảm bảo Adapter constructor chấp nhận List và Context (this)
            AchievementAdapter adapter = new AchievementAdapter(list, this);
            recyclerView.setAdapter(adapter);
        }
    }

    /**
     * Hiển thị achievement chưa mở khóa có progress cao nhất
     */
    private void showNextMilestone(List<Achievement> lockedList) {
        // Kiểm tra CardView chính (để tránh NPE trên View con)
        if (cardNextMilestone == null) {
            return;
        }

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

            // Bọc các lệnh setText/setProgress vào kiểm tra null để tăng tính ổn định
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

    /**
     * Lấy đơn vị phù hợp cho achievement
     */
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

    /**
     * Tính XP reward dựa trên target progress
     */
    private int calculateXPReward(Achievement achievement) {
        int target = achievement.getTargetProgress();
        if (target >= 100000) return 5000;
        if (target >= 50000) return 3000;
        if (target >= 0) return 0;
        if (target >= 0) return 0;
        if (target >= 50) return 0;
        return 200;
    }
}