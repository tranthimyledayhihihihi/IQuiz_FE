package com.example.iq5.feature.result.ui;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.iq5.R;
import com.example.iq5.core.navigation.NavigationHelper;
import com.example.iq5.feature.result.adapter.StatsAdapter;
import com.example.iq5.feature.result.model.UserStats;
import com.example.iq5.feature.result.data.ResultRepository;
import java.util.List;

public class StatsActivity extends AppCompatActivity {

    private RecyclerView rvStatsMilestones;
    private TextView tvTotalScore, tvAverageScore, tvDaysCompletedStat;
    private ResultRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // 1. Ánh xạ View
        rvStatsMilestones = findViewById(R.id.rv_stats_milestones);
        tvTotalScore = findViewById(R.id.tv_total_score);
        tvAverageScore = findViewById(R.id.tv_average_score);
        tvDaysCompletedStat = findViewById(R.id.tv_days_completed_stat);

        // 2. Khởi tạo Repository và lấy dữ liệu từ JSON
        repository = new ResultRepository(this);
        List<UserStats> statsList = repository.getStatsMilestones();

        // 3. Cấu hình RecyclerView cho các Mốc thống kê
        rvStatsMilestones.setLayoutManager(new LinearLayoutManager(this));
        StatsAdapter adapter = new StatsAdapter(statsList, this);
        rvStatsMilestones.setAdapter(adapter);

        // 4. Cập nhật thống kê tuần (demo data - trong production sẽ tính từ database)
        updateWeeklyStats();

        // 5. Xử lý nút Back
        findViewById(R.id.btn_back_stats).setOnClickListener(v -> NavigationHelper.goBack(this));
    }

    /**
     * Cập nhật thống kê cho tuần hiện tại.
     * TODO: Trong production, tính toán từ dữ liệu thực tế.
     */
    private void updateWeeklyStats() {
        // Demo data - Giả định tuần này có 5 ngày đã chơi
        int totalScore = 4100;  // Tổng điểm trong tuần
        int daysCompleted = 5;   // Số ngày đã chơi
        int totalDaysInWeek = 7;
        int averageScore = daysCompleted > 0 ? totalScore / daysCompleted : 0;

        // Cập nhật UI
        tvTotalScore.setText(totalScore + "\nTổng điểm");
        tvAverageScore.setText(averageScore + "\nTrung bình");
        tvDaysCompletedStat.setText(daysCompleted + "/" + totalDaysInWeek + "\nNgày hoàn thành");
    }
}
