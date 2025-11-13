package com.example.fe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class StreakActivity extends AppCompatActivity {

    private TextView tvCurrentStreakCount;
    private RecyclerView rvStreakHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streak);

        tvCurrentStreakCount = findViewById(R.id.tv_current_streak_count);
        rvStreakHistory = findViewById(R.id.rv_streak_history);

        // TODO: Lấy dữ liệu chuỗi ngày hiện tại (Từ API 2)
        int currentStreak = 7;
        tvCurrentStreakCount.setText(String.valueOf(currentStreak));

        // Thiết lập RecyclerView cho lịch sử
        // ... Cần tạo StreakAdapter
        rvStreakHistory.setLayoutManager(new LinearLayoutManager(this));
    }
}