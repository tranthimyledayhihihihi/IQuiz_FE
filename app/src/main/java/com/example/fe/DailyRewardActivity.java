package com.example.fe;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DailyRewardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_reward);

        findViewById(R.id.btnClaim).setOnClickListener(v ->
                Toast.makeText(this, "🎉 Đã nhận thưởng thành công!", Toast.LENGTH_SHORT).show());
    }
}
