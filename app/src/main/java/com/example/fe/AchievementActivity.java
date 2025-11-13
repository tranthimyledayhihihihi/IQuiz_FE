package com.example.fe;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class AchievementActivity extends AppCompatActivity {

    private GridLayout gridAchievements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        gridAchievements = findViewById(R.id.grid_achievements);

        // TODO: Lấy dữ liệu Thành tựu (Bảng ThanhTuu) từ API 4.
        List<Achievement> achievements = getDummyAchievements();

        displayAchievements(achievements);
    }

    // Class giả lập Thành tựu
    private static class Achievement {
        String name;
        String iconRes;
        boolean isUnlocked;
        int currentProgress;
        int targetProgress;
        public Achievement(String n, String i, boolean u, int cur, int tar) {
            name = n; iconRes = i; isUnlocked = u; currentProgress = cur; targetProgress = tar;
        }
    }

    private List<Achievement> getDummyAchievements() {
        List<Achievement> list = new ArrayList<>();
        list.add(new Achievement("Chiến thắng đầu tiên", "ic_badge_1", true, 1, 1));
        list.add(new Achievement("Chuỗi 7 ngày", "ic_badge_2", true, 7, 7));
        list.add(new Achievement("100 câu trả lời", "ic_badge_3", false, 45, 100));
        list.add(new Achievement("Nhà vô địch", "ic_badge_4", false, 0, 10));
        return list;
    }

    private void displayAchievements(List<Achievement> achievements) {
        gridAchievements.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (Achievement achievement : achievements) {
            View achievementView = inflater.inflate(R.layout.item_achievement, gridAchievements, false);

            ImageView icon = achievementView.findViewById(R.id.img_achievement_icon);
            TextView name = achievementView.findViewById(R.id.tv_achievement_name);
            ProgressBar progressBar = achievementView.findViewById(R.id.pb_achievement_progress);

            name.setText(achievement.name);
            int iconId = getResources().getIdentifier(achievement.iconRes, "drawable", getPackageName());
            icon.setImageResource(iconId > 0 ? iconId : R.drawable.ic_badge_default);

            if (achievement.isUnlocked) {
                // Đã mở khóa: Icon sáng, Progress ẩn, hoặc hiển thị "Đã mở khóa"
                icon.setAlpha(1.0f);
                progressBar.setVisibility(View.GONE);
                // Cần set background FrameLayout badge là Vàng
            } else {
                // Chưa mở khóa: Icon mờ [cite: 171], hiển thị Progress Xanh lá [cite: 173]
                icon.setAlpha(0.3f);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setMax(achievement.targetProgress);
                progressBar.setProgress(achievement.currentProgress);
                // Cần set background FrameLayout badge là Xám [cite: 171]
            }

            achievementView.setOnClickListener(v ->
                    Toast.makeText(this, achievement.name + (achievement.isUnlocked ? getString(R.string.achievement_unlocked) : getString(R.string.achievement_locked, achievement.currentProgress, achievement.targetProgress)), Toast.LENGTH_SHORT).show()
            );

            gridAchievements.addView(achievementView);
        }
    }
}