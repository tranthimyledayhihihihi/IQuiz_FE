package com.example.iq5.feature.multiplayer.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import com.example.iq5.R;
import com.example.iq5.feature.auth.ui.HomeActivity;

public class GameResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);

        // Nhận dữ liệu từ Intent
        int yourScore = getIntent().getIntExtra("yourScore", 0);
        int opponentScore = getIntent().getIntExtra("opponentScore", 0);

        // Tìm Views với xử lý null-safe
        TextView tvResultIcon = findViewById(R.id.tvResultIcon);
        TextView tvYourFinalScore = findViewById(R.id.tvYourFinalScore);
        TextView tvOpponentFinalScore = findViewById(R.id.tvOpponentFinalScore);
        TextView tvResultMessage = findViewById(R.id.tvResultMessage);
        Button btnPlayAgain = findViewById(R.id.btnPlayAgain);
        Button btnBackToLobby = findViewById(R.id.btnBackToLobby);

        // Kiểm tra null để tránh crash
        if (tvYourFinalScore == null || tvOpponentFinalScore == null ||
                tvResultMessage == null || tvResultIcon == null) {
            finish();
            return;
        }

        // Hiển thị điểm
        tvYourFinalScore.setText(String.valueOf(yourScore));
        tvOpponentFinalScore.setText(String.valueOf(opponentScore));

        // Xử lý kết quả
        if (yourScore > opponentScore) {
            tvResultMessage.setText("👑 BẠN CHIẾN THẮNG!");
            tvResultIcon.setText("👑");
            tvResultMessage.setBackgroundColor(Color.parseColor("#2ecc71"));
        } else if (yourScore < opponentScore) {
            tvResultMessage.setText("😢 BẠN THUA CUỘC!");
            tvResultIcon.setText("😢");
            tvResultMessage.setBackgroundColor(Color.parseColor("#e74c3c"));
        } else {
            tvResultMessage.setText("🤝 HÒA!");
            tvResultIcon.setText("🤝");
            tvResultMessage.setBackgroundColor(Color.parseColor("#f39c12"));
        }

        // Nút "Chơi Lại" (🔄) → Quay về MultiplayerLobbyActivity
        if (btnPlayAgain != null) {
            btnPlayAgain.setOnClickListener(v -> {
                Intent intent = new Intent(this, MultiplayerLobbyActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        }

        // Nút "Về Lobby" (🏠) → Quay về HomeActivity (trang chủ)
        if (btnBackToLobby != null) {
            btnBackToLobby.setOnClickListener(v -> {
                Intent intent = new Intent(this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        }

        // Xử lý nút Back → Về HomeActivity (trang chủ)
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(GameResultActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}