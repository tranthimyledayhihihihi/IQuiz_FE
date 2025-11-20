package com.example.iq5.feature.result.ui;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.iq5.R;
import com.example.iq5.feature.result.adapter.DailyRewardAdapter;
import com.example.iq5.feature.result.model.DailyReward;
import java.util.ArrayList;
import java.util.List;

public class DailyRewardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load layout XML cho màn hình Thưởng Hàng Ngày
        setContentView(R.layout.activity_daily_reward);

        RecyclerView rvRewards = findViewById(R.id.rv_daily_rewards);
        // Sử dụng GridLayoutManager để sắp xếp 4 cột như trong thiết kế
        rvRewards.setLayoutManager(new GridLayoutManager(this, 4));

        // 1. Tạo dữ liệu giả (mock data)
        // Ví dụ: Tổng 16 ngày, hôm nay là ngày thứ 7
        List<DailyReward> mockRewards = createMockRewards(16, 7);

        // 2. Kết nối Adapter
        // Truyền context (this) để Adapter có thể truy cập tài nguyên màu
        DailyRewardAdapter adapter = new DailyRewardAdapter(mockRewards, this);
        rvRewards.setAdapter(adapter);

        // 3. Xử lý sự kiện nút Nhận Thưởng (chỉ demo)
        Button btnClaim = findViewById(R.id.btn_claim_reward);
        btnClaim.setOnClickListener(v -> {
            // Đây là nơi bạn sẽ gọi ViewModel để xử lý nhận thưởng thực tế
            // Trong demo, ta chỉ cần hiển thị giao diện.
        });
    }

    /**
     * Tạo danh sách phần thưởng giả lập cho demo UI.
     * @param totalDays Tổng số ngày trong chuỗi thưởng.
     * @param todayNumber Số thứ tự của ngày hôm nay (bắt đầu từ 1).
     * @return Danh sách các đối tượng DailyReward.
     */
    private List<DailyReward> createMockRewards(int totalDays, int todayNumber) {
        List<DailyReward> rewards = new ArrayList<>();
        for (int i = 1; i <= totalDays; i++) {
            boolean isClaimed = i < todayNumber;
            boolean isToday = i == todayNumber;

            rewards.add(new DailyReward(i, isClaimed, isToday));
        }
        return rewards;
    }
}