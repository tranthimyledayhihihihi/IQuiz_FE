package com.example.iq5.feature.result.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.R;
import com.example.iq5.feature.result.data.ResultRepository;

public class StreakActivity extends AppCompatActivity {

    private TextView tvCurrentDays;
    private TextView tvStreakMessage;
    private ResultRepository repository;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streak);

        // 1️⃣ known view
        tvCurrentDays = findViewById(R.id.tv_current_days);
        tvStreakMessage = findViewById(R.id.tv_streak_message);

        // 2️⃣ repository
        repository = new ResultRepository(this);

        // 3️⃣ gọi API streak (KHÔNG HISTORY)
        repository.getDailyStreak(new ResultRepository.StreakCallback() {
            @Override
            public void onSuccess(int currentStreak, String message) {
                runOnUiThread(() -> {
                    tvCurrentDays.setText(currentStreak + " NGÀY");
                    tvStreakMessage.setText(message);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    tvCurrentDays.setText("0 NGÀY");
                    tvStreakMessage.setText("Lỗi tải streak");
                });
            }
        });

        // 4️⃣ back
        View btnBack = findViewById(R.id.btn_back_streak);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }
}
