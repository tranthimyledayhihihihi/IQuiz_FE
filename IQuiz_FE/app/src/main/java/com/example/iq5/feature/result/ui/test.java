package com.example.iq5.feature.result.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.iq5.R;

// GIỮ NGUYÊN TÊN CLASS KHÔNG CHUẨN (test) ĐỂ KHỚP VỚI MANIFEST CŨ
public class test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Đảm bảo layout kiểm thử đã được set
        setContentView(R.layout.activity_test);

        // 1. Thiết lập Listener chuyển hướng bằng findViewById an toàn
        setupListeners();
    }

    private void setupListeners() {
        // Ánh xạ an toàn (Không cần khai báo biến toàn cục)

        // Khởi chạy ResultActivity
        View btnResult = findViewById(R.id.btn_result);
        if (btnResult != null) {
            btnResult.setOnClickListener(v ->
                    startActivity(new Intent(this, ResultActivity.class)));
        }

        // Khởi chạy DailyRewardActivity
        View btnReward = findViewById(R.id.btn_daily_reward);
        if (btnReward != null) {
            btnReward.setOnClickListener(v ->
                    startActivity(new Intent(this, DailyRewardActivity.class)));
        }

        // Khởi chạy AchievementActivity
        View btnAchievement = findViewById(R.id.btn_achievement);
        if (btnAchievement != null) {
            btnAchievement.setOnClickListener(v ->
                    startActivity(new Intent(this, AchievementActivity.class)));
        }

        // Khởi chạy StatsActivity
        View btnStats = findViewById(R.id.btn_stats);
        if (btnStats != null) {
            btnStats.setOnClickListener(v ->
                    startActivity(new Intent(this, StatsActivity.class)));
        }

        // Khởi chạy StreakActivity
        View btnStreak = findViewById(R.id.btn_streak);
        if (btnStreak != null) {
            btnStreak.setOnClickListener(v ->
                    startActivity(new Intent(this, StreakActivity.class)));
        }
    }

    // Xóa hàm mapViews() nếu không sử dụng biến toàn cục
}