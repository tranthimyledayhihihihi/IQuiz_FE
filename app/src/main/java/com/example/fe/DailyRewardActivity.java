package com.example.fe;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DailyRewardActivity extends AppCompatActivity {

    private TextView tvRewardAmount, tvRewardMessage;
    private Button btnClaimReward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_reward);

        tvRewardAmount = findViewById(R.id.tv_reward_amount);
        tvRewardMessage = findViewById(R.id.tv_reward_message);
        btnClaimReward = findViewById(R.id.btn_claim_reward);

        // Giả lập phần thưởng (Lấy từ API 3. Thưởng mỗi ngày)
        int rewardPoints = 500;
        tvRewardAmount.setText(getString(R.string.reward_points_format, rewardPoints));

        btnClaimReward.setOnClickListener(v -> {
            claimReward(rewardPoints);
        });

        // TODO: Kiểm tra xem đã nhận thưởng hôm nay chưa
    }

    private void claimReward(int points) {
        // TODO: Gọi API 3. Thưởng mỗi ngày (để xác nhận đã nhận)
        Toast.makeText(this, "Đã nhận thành công " + points + " điểm!", Toast.LENGTH_LONG).show();
        tvRewardMessage.setText(getString(R.string.daily_reward_claimed));
        btnClaimReward.setEnabled(false);
        btnClaimReward.setText(getString(R.string.reward_claimed));
    }
}