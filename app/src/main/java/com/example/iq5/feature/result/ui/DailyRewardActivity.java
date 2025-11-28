package com.example.iq5.feature.result.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView; // Import ImageView
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.iq5.R;
import com.example.iq5.core.navigation.NavigationHelper;
import com.example.iq5.feature.result.adapter.DailyRewardAdapter;
import com.example.iq5.feature.result.model.DailyReward;
import com.example.iq5.feature.result.data.ResultRepository;
import java.util.List;

public class DailyRewardActivity extends AppCompatActivity {

    private RecyclerView rvRewards;
    private Button btnClaimReward;
    private ImageView btnBack; // Khai báo
    private ResultRepository repository;
    private List<DailyReward> rewardsList;
    private DailyRewardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_reward);

        // 1. Ánh xạ View
        rvRewards = findViewById(R.id.rv_daily_rewards);
        btnClaimReward = findViewById(R.id.btn_claim_reward);
        btnBack = findViewById(R.id.btn_back_reward); // Ánh xạ ID mới

        // 2. Khởi tạo Repository và lấy dữ liệu từ JSON
        repository = new ResultRepository(this);
        rewardsList = repository.getDailyRewards();

        // 3. Cấu hình RecyclerView với GridLayoutManager (4 cột)
        if (rvRewards != null) {
            rvRewards.setLayoutManager(new GridLayoutManager(this, 4));
            adapter = new DailyRewardAdapter(rewardsList, this);
            rvRewards.setAdapter(adapter);
        }

        // 4. Kiểm tra và cập nhật trạng thái nút Nhận Thưởng
        updateClaimButtonState();

        // 5. Xử lý sự kiện nút Nhận Thưởng
        if (btnClaimReward != null) {
            btnClaimReward.setOnClickListener(v -> claimTodayReward());
        }

        // 6. Xử lý nút Back (Dùng ID mới)
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                // Quay lại Activity trước đó
                NavigationHelper.goBack(this);
            });
        }
    }

    /**
     * Cập nhật trạng thái nút Nhận Thưởng dựa trên dữ liệu.
     */
    private void updateClaimButtonState() {
        if (btnClaimReward == null) return;

        DailyReward todayReward = repository.getTodayReward();

        if (todayReward != null && !todayReward.isClaimed()) {
            btnClaimReward.setEnabled(true);
            btnClaimReward.setText("NHẬN THƯỞNG - " + todayReward.getReward() + " ĐIỂM");
        } else {
            btnClaimReward.setEnabled(false);
            btnClaimReward.setText("ĐÃ NHẬN HÔM NAY");
        }
    }

    /**
     * Xử lý nhận thưởng ngày hôm nay.
     */
    private void claimTodayReward() {
        DailyReward todayReward = repository.getTodayReward();

        if (todayReward != null && !todayReward.isClaimed()) {
            int rewardPoints = todayReward.getReward();

            Toast.makeText(this,
                    "🎉 Đã nhận " + rewardPoints + " điểm!",
                    Toast.LENGTH_SHORT).show();

            todayReward.setClaimed(true);
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
            updateClaimButtonState();
        } else {
            Toast.makeText(this,
                    "Bạn đã nhận thưởng hôm nay rồi!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}