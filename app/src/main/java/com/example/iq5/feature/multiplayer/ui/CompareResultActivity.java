package com.example.iq5.feature.multiplayer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.iq5.R;

public class CompareResultActivity extends AppCompatActivity {

    private TextView tvResultTitle, tvResultSubTitle, tvPlayer1Score, tvPlayer2Score;
    private Button btnRematch, btnExit;

    private int player1Score;
    private int player2Score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_result);

        player1Score = getIntent().getIntExtra("PLAYER_1_SCORE", 0);
        player2Score = getIntent().getIntExtra("PLAYER_2_SCORE", 0);
        // TODO: Lấy thông tin chi tiết trận đấu (MATCH_ID) để hiển thị thêm

        initView();
        displayResults();
        setupListeners();

        // TODO: Gửi/lắng nghe WebSocket cho sự kiện "REMATCH"
        // 1. "SEND_REMATCH_REQUEST": Gửi yêu cầu tái đấu
        // 2. "RECEIVE_REMATCH_REQUEST": Nhận yêu cầu
        // 3. "REMATCH_ACCEPTED": Cả 2 đồng ý
        // 4. "REMATCH_DECLINED": 1 người từ chối/rời đi
    }

    private void initView() {
        tvResultTitle = findViewById(R.id.tvResultTitle);
        tvResultSubTitle = findViewById(R.id.tvResultSubTitle);
        tvPlayer1Score = findViewById(R.id.tvPlayer1Score);
        tvPlayer2Score = findViewById(R.id.tvPlayer2Score);
        btnRematch = findViewById(R.id.btnRematch);
        btnExit = findViewById(R.id.btnExit);
    }

    private void displayResults() {
        tvPlayer1Score.setText(String.valueOf(player1Score));
        tvPlayer2Score.setText(String.valueOf(player2Score));

        if (player1Score > player2Score) {
            tvResultTitle.setText("THẮNG");
            tvResultTitle.setTextColor(ContextCompat.getColor(this, R.color.colorWin));
            tvResultSubTitle.setText("Bạn đã chiến thắng đối thủ!");
        } else if (player1Score < player2Score) {
            tvResultTitle.setText("THUA");
            tvResultTitle.setTextColor(ContextCompat.getColor(this, R.color.colorLose));
            tvResultSubTitle.setText("Bạn đã thua. Cố gắng lần sau nhé!");
        } else {
            tvResultTitle.setText("HÒA");
            tvResultTitle.setTextColor(ContextCompat.getColor(this, R.color.colorTextSecondary));
            tvResultSubTitle.setText("Một trận đấu ngang tài ngang sức!");
        }
    }

    private void setupListeners() {
        btnRematch.setOnClickListener(v -> {
            // TODO: Gửi "SEND_REMATCH_REQUEST"
            btnRematch.setText("Đã gửi lời mời. Đang chờ...");
            btnRematch.setEnabled(false);
            // Giả lập đối thủ chấp nhận sau 2s
            new android.os.Handler().postDelayed(() -> {
                Toast.makeText(this, "Đối thủ đã chấp nhận tái đấu!", Toast.LENGTH_SHORT).show();
                // TODO: Bắt đầu lại trận đấu mới
                // Intent intent = new Intent(this, PvPBattleActivity.class);
                // startActivity(intent);
                // finish();
            }, 2000);
        });

        btnExit.setOnClickListener(v -> {
            // TODO: Gửi "LEAVE_ROOM"
            Intent intent = new Intent(this, FindMatchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}