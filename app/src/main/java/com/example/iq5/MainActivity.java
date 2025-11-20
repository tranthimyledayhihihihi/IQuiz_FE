package com.example.iq5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.example.iq5.feature.result.ui.*; // Import tất cả các Activity demo

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Kết nối các nút với phương thức chuyển Activity
        findViewById(R.id.btn_result).setOnClickListener(v -> startActivity(new Intent(this, ResultActivity.class)));
        findViewById(R.id.btn_daily_reward).setOnClickListener(v -> startActivity(new Intent(this, DailyRewardActivity.class)));
        findViewById(R.id.btn_achievement).setOnClickListener(v -> startActivity(new Intent(this, AchievementActivity.class)));
        findViewById(R.id.btn_stats).setOnClickListener(v -> startActivity(new Intent(this, StatsActivity.class)));
        findViewById(R.id.btn_streak).setOnClickListener(v -> startActivity(new Intent(this, StreakActivity.class)));
    }
}