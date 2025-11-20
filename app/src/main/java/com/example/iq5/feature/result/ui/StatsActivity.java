package com.example.iq5.feature.result.ui;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.iq5.R;
import com.example.iq5.feature.result.adapter.StatsAdapter;
import com.example.iq5.feature.result.model.UserStats;
import java.util.ArrayList;
import java.util.List;
import android.view.View; // Import cần thiết cho findViewById

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // KHẮC PHỤC LỖI: Đảm bảo Activity gọi đúng file layout chứa ID rv_stats_milestones
        setContentView(R.layout.activity_stats);

        // 1. Khởi tạo RecyclerView cho các Mốc thống kê
        RecyclerView rvStats = findViewById(R.id.rv_stats_milestones);
        if (rvStats != null) { // Kiểm tra null để tránh crash nếu ID vẫn bị lỗi
            rvStats.setLayoutManager(new LinearLayoutManager(this));

            // 2. Tạo dữ liệu giả
            List<UserStats> mockStats = createMockStats();

            // 3. Khởi tạo và kết nối Adapter
            // Giả định StatsAdapter yêu cầu Context (this)
            StatsAdapter adapter = new StatsAdapter(mockStats, this);
            rvStats.setAdapter(adapter);
        }

        // 4. Demo cập nhật text cho các thẻ thống kê chính
        // SỬA LỖI: Sử dụng findViewById an toàn
        TextView tvTotalScore = findViewById(R.id.tv_total_score);
        TextView tvAverageScore = findViewById(R.id.tv_average_score);

        if (tvTotalScore != null) {
            tvTotalScore.setText("4,100");
        }
        if (tvAverageScore != null) {
            tvAverageScore.setText("820");
        }


        // 5. Xử lý sự kiện nút Back
        View btnBack = findViewById(R.id.btn_back_stats);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }

    /**
     * Tạo dữ liệu giả cho các mốc thống kê (UserStats).
     */
    private List<UserStats> createMockStats() {
        List<UserStats> stats = new ArrayList<>();
        // Giả định UserStats có constructor: (statName, description, value)
        stats.add(new UserStats("Top Score", "Điểm cao nhất từng đạt", "1,200"));
        stats.add(new UserStats("Total Matches", "Tổng số trận đã đấu", "150"));
        stats.add(new UserStats("Win Rate", "Tỉ lệ thắng", "75%"));
        return stats;
    }
}