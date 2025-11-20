// Trong StreakActivity.java (Thay thế logic tạo mock data cũ)
package com.example.iq5.feature.result.ui;

import com.example.iq5.feature.result.adapter.StreakHistoryAdapter;
import com.example.iq5.feature.result.model.StreakDay; // Thêm import
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.iq5.R;
import com.example.iq5.feature.result.adapter.StreakHistoryAdapter;
import com.example.iq5.feature.result.model.StreakDay;
import java.util.ArrayList;
import java.util.List;
import android.widget.TextView;
import android.widget.ImageView; // Cần thiết cho nút back

// Bổ sung các thư viện thiếu
import android.view.View;
import android.view.ViewGroup;

public class StreakActivity extends AppCompatActivity {

    private TextView tvCurrentDays;
    private RecyclerView rvStreakHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Đảm bảo layout là activity_streak
        setContentView(R.layout.activity_streak);

        // Ánh xạ View
        tvCurrentDays = findViewById(R.id.tv_current_days);
        rvStreakHistory = findViewById(R.id.rv_streak_history);

        // Xử lý nút back
        findViewById(R.id.btn_back_streak).setOnClickListener(v -> finish());

        // Cập nhật số ngày Streak hiện tại (Giả lập)
        int currentStreak = 30; // Giả sử lấy từ UserStreak model
        tvCurrentDays.setText(currentStreak + " NGÀY");

        // Chuẩn bị dữ liệu lịch sử
        List<StreakDay> historyData = createMockHistory();

        // Cấu hình Adapter
        StreakHistoryAdapter adapter = new StreakHistoryAdapter(historyData);
        rvStreakHistory.setAdapter(adapter);
        rvStreakHistory.setLayoutManager(new LinearLayoutManager(this));

    } // Đóng hàm onCreate đúng cách

    private List<StreakDay> createMockHistory() {
        List<StreakDay> history = new ArrayList<>();
        // Đảm bảo bạn sử dụng tên lớp StreakDay (không phải UserStreak)

        // Ví dụ: Ngày 30 (Hoàn thành)
        history.add(new StreakDay(30, "20/11/2025", 500, true));
        // Ví dụ: Ngày 29 (Hoàn thành)
        history.add(new StreakDay(29, "19/11/2025", 500, true));
        // Ví dụ: Ngày 28 (Bị lỡ)
        history.add(new StreakDay(28, "18/11/2025", 0, false));
        // Ví dụ: Ngày 27 (Hoàn thành)
        history.add(new StreakDay(27, "17/11/2025", 500, true));
        // Ví dụ: Ngày 26 (Bị lỡ)
        history.add(new StreakDay(26, "16/11/2025", 0, false));

        return history;
    }
} // Đóng class Activity