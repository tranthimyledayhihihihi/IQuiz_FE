package com.example.iq5.feature.result.ui;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.iq5.R;
import com.example.iq5.core.navigation.NavigationHelper;
import com.example.iq5.feature.result.adapter.StreakHistoryAdapter;
import com.example.iq5.feature.result.data.ResultRepository;
import com.example.iq5.feature.result.model.StreakDay;
import java.util.List;

public class StreakActivity extends AppCompatActivity {

    private TextView tvCurrentDays;
    private RecyclerView rvStreakHistory;
    private ResultRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streak);

        // 1. Ánh xạ View
        tvCurrentDays = findViewById(R.id.tv_current_days);
        rvStreakHistory = findViewById(R.id.rv_streak_history);

        // 2. Khởi tạo Repository và lấy dữ liệu từ JSON
        repository = new ResultRepository(this);
        List<StreakDay> historyData = repository.getStreakHistory();

        // 3. Cập nhật số ngày Streak hiện tại từ Repository
        int currentStreak = repository.getCurrentStreakDays();
        tvCurrentDays.setText(currentStreak + " NGÀY");

        // 4. Cấu hình RecyclerView cho lịch sử Streak
        rvStreakHistory.setLayoutManager(new LinearLayoutManager(this));
        StreakHistoryAdapter adapter = new StreakHistoryAdapter(historyData);
        rvStreakHistory.setAdapter(adapter);

        // 5. Xử lý nút Back
        findViewById(R.id.btn_back_streak).setOnClickListener(v -> NavigationHelper.goBack(this));
    }
}