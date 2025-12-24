package com.example.iq5.feature.result.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.core.navigation.NavigationHelper;
import com.example.iq5.feature.result.adapter.DailyRewardAdapter;
import com.example.iq5.feature.result.data.ResultRepository;
import com.example.iq5.feature.result.model.DailyReward;

import java.util.List;

public class DailyRewardActivity extends AppCompatActivity {

    private RecyclerView rvRewards;
    private Button btnClaimReward;
    private ImageView btnBack;

    private ResultRepository repository;
    private List<DailyReward> rewardsList;
    private DailyRewardAdapter adapter;

    private boolean canClaimToday = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_reward);

        // 1️⃣ Ánh xạ view
        rvRewards = findViewById(R.id.rv_daily_rewards);
        btnClaimReward = findViewById(R.id.btn_claim_reward);
        btnBack = findViewById(R.id.btn_back_reward);

        rvRewards.setLayoutManager(new GridLayoutManager(this, 4));

        // 2️⃣ Init repository
        repository = new ResultRepository(this);

        // 3️⃣ GỌI API DAILY REWARD (ASYNC)
        repository.getDailyRewards(new ResultRepository.DailyRewardCallback() {
            @Override
            public void onSuccess(List<DailyReward> rewards, boolean canClaim) {
                runOnUiThread(() -> {
                    rewardsList = rewards;
                    canClaimToday = canClaim;

                    adapter = new DailyRewardAdapter(rewardsList, DailyRewardActivity.this);
                    rvRewards.setAdapter(adapter);

                    updateClaimButtonState();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(
                            DailyRewardActivity.this,
                            "Lỗi tải thưởng ngày",
                            Toast.LENGTH_SHORT
                    ).show();
                    btnClaimReward.setEnabled(false);
                });
            }
        });

        // 4️⃣ Xử lý nút Nhận Thưởng
        btnClaimReward.setOnClickListener(v -> claimTodayReward());

        // 5️⃣ Nút back
        btnBack.setOnClickListener(v -> NavigationHelper.goBack(this));
    }

    /**
     * Cập nhật trạng thái nút Nhận Thưởng
     */
    private void updateClaimButtonState() {
        if (!canClaimToday) {
            btnClaimReward.setEnabled(false);
            btnClaimReward.setText("ĐÃ NHẬN HÔM NAY");
            return;
        }

        btnClaimReward.setEnabled(true);
        btnClaimReward.setText("NHẬN THƯỞNG");
    }

    /**
     * Xử lý nhận thưởng hôm nay (UI)
     * (BE đã xử lý nghiệp vụ, FE chỉ hiển thị)
     */
    private void claimTodayReward() {
        if (!canClaimToday) {
            Toast.makeText(this,
                    "Bạn đã nhận thưởng hôm nay rồi!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this,
                "🎉 Nhận thưởng thành công!",
                Toast.LENGTH_SHORT).show();

        canClaimToday = false;

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

        updateClaimButtonState();
    }
}
