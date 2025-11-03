package com.example.fe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnResult).setOnClickListener(v ->
                startActivity(new Intent(this, ResultActivity.class)));
        findViewById(R.id.btnReward).setOnClickListener(v ->
                startActivity(new Intent(this, DailyRewardActivity.class)));
        findViewById(R.id.btnStreak).setOnClickListener(v ->
                startActivity(new Intent(this, StreakActivity.class)));
        findViewById(R.id.btnStats).setOnClickListener(v ->
                startActivity(new Intent(this, StatisticsActivity.class)));
        findViewById(R.id.btnAchievements).setOnClickListener(v ->
                startActivity(new Intent(this, AchievementActivity.class)));
    }
}
