package com.example.iq5.feature.result.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.core.prefs.PrefsManager;
import com.example.iq5.data.api.ApiService;
import com.example.iq5.data.api.RetrofitClient;
import com.example.iq5.data.model.DailyRewardClaimResponse;
import com.example.iq5.feature.result.adapter.DailyRewardAdapter;
import com.example.iq5.feature.result.model.DailyReward;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DailyRewardActivity extends AppCompatActivity {

    private RecyclerView rvRewards;
    private Button btnClaimReward;
    private ImageView btnBack;

    private DailyRewardAdapter adapter;
    private final List<DailyReward> rewardsList = new ArrayList<>();

    private ApiService apiService;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_reward);

        // ================= INIT VIEW =================
        rvRewards = findViewById(R.id.rv_daily_rewards);
        btnClaimReward = findViewById(R.id.btn_claim_reward);
        btnBack = findViewById(R.id.btn_back_reward);

        rvRewards.setLayoutManager(new GridLayoutManager(this, 4));
        adapter = new DailyRewardAdapter(rewardsList, this);
        rvRewards.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());

        // ================= AUTH + API =================
        PrefsManager prefs = new PrefsManager(this);
        token = prefs.getToken();
        apiService = RetrofitClient.getApiService();

        if (token == null || token.isEmpty()) {
            Toast.makeText(this,
                    "⚠️ Bạn chưa đăng nhập",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // ================= BUILD UI (7 NGÀY) =================
        buildDefaultRewardList();

        // ================= CLAIM =================
        btnClaimReward.setOnClickListener(v -> claimDailyReward());
    }

    // =================================================
    // BUILD UI 7 NGÀY (FE DỰNG – BE CHỈ QUYẾT ĐỊNH CLAIM)
    // =================================================
    private void buildDefaultRewardList() {
        rewardsList.clear();

        for (int day = 1; day <= 7; day++) {
            DailyReward reward = new DailyReward();
            reward.setDayNumber(day);
            reward.setReward(50);       // FE quy ước 50 điểm / ngày
            reward.setClaimed(false);
            reward.setToday(day == 7); // Quy ước: ngày cuối là hôm nay
            rewardsList.add(reward);
        }

        adapter.notifyDataSetChanged();
        updateClaimButtonState();
    }

    // =================================================
    // CLAIM DAILY REWARD – ACHIEVEMENT CONTROLLER
    // POST /api/user/achievement/daily-reward
    // =================================================
    private void claimDailyReward() {

        apiService.claimDailyReward("Bearer " + token)
                .enqueue(new Callback<DailyRewardClaimResponse>() {
                    @Override
                    public void onResponse(
                            Call<DailyRewardClaimResponse> call,
                            Response<DailyRewardClaimResponse> response) {

                        if (!response.isSuccessful() || response.body() == null) {
                            Toast.makeText(
                                    DailyRewardActivity.this,
                                    "❌ Không nhận được phản hồi từ server",
                                    Toast.LENGTH_SHORT
                            ).show();
                            return;
                        }

                        DailyRewardClaimResponse res = response.body();

                        // 🔔 Thông báo từ BE
                        Toast.makeText(
                                DailyRewardActivity.this,
                                res.message,
                                Toast.LENGTH_LONG
                        ).show();

                        if (res.awarded) {
                            // ✅ Đánh dấu hôm nay đã nhận
                            for (DailyReward r : rewardsList) {
                                if (r.isToday()) {
                                    r.setClaimed(true);
                                }
                            }
                            adapter.notifyDataSetChanged();
                            updateClaimButtonState();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<DailyRewardClaimResponse> call,
                            Throwable t) {

                        Toast.makeText(
                                DailyRewardActivity.this,
                                "❌ Lỗi mạng khi nhận thưởng",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    // =================================================
    // UPDATE BUTTON STATE
    // =================================================
    private void updateClaimButtonState() {
        DailyReward today = null;

        for (DailyReward r : rewardsList) {
            if (r.isToday()) {
                today = r;
                break;
            }
        }

        if (today != null && !today.isClaimed()) {
            btnClaimReward.setEnabled(true);
            btnClaimReward.setText("NHẬN THƯỞNG - " + today.getReward() + " ĐIỂM");
        } else {
            btnClaimReward.setEnabled(false);
            btnClaimReward.setText("ĐÃ NHẬN HÔM NAY");
        }
    }
}
