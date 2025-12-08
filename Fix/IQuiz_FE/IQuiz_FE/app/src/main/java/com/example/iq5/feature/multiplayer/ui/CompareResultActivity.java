package com.example.iq5.feature.multiplayer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.iq5.R;
import com.google.android.material.button.MaterialButton;

public class CompareResultActivity extends AppCompatActivity {

    // Text hiển thị trạng thái
    private TextView tvResultTitle, tvResultSubTitle;

    // Điểm
    private TextView tvPlayer1Score, tvPlayer2Score;

    // Số câu đúng
    private TextView tvPlayer1CorrectCount, tvPlayer2CorrectCount;

    // Nút
    private MaterialButton btnRematch, btnExit;

    // Biến dữ liệu
    private int player1Score;
    private int player2Score;
    private int player1CorrectCount;
    private int player2CorrectCount;
    private int totalQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_result);

        // Nhận dữ liệu từ Intent (ưu tiên key mới, fallback key cũ)
        Intent intent = getIntent();

        // Điểm của bạn
        if (intent.hasExtra("PLAYER_SCORE")) {
            player1Score = intent.getIntExtra("PLAYER_SCORE", 0);
        } else {
            player1Score = intent.getIntExtra("your_score", 0);
        }

        // Điểm đối thủ
        if (intent.hasExtra("OPPONENT_SCORE")) {
            player2Score = intent.getIntExtra("OPPONENT_SCORE", 0);
        } else {
            player2Score = intent.getIntExtra("opponent_score", 0);
        }

        // Số câu đúng
        player1CorrectCount = intent.getIntExtra("PLAYER_CORRECT_COUNT", 0);
        player2CorrectCount = intent.getIntExtra("OPPONENT_CORRECT_COUNT", 0);

        // Tổng số câu (có thể không truyền, default 10)
        totalQuestions = intent.getIntExtra("TOTAL_QUESTIONS", 10);

        initView();
        displayResults();
        setupListeners();
    }

    private void initView() {
        tvResultTitle = findViewById(R.id.tvResultTitle);
        tvResultSubTitle = findViewById(R.id.tvResultSubTitle);

        tvPlayer1Score = findViewById(R.id.tvPlayer1Score);
        tvPlayer2Score = findViewById(R.id.tvPlayer2Score);
        tvPlayer1CorrectCount = findViewById(R.id.tvPlayer1CorrectCount);
        tvPlayer2CorrectCount = findViewById(R.id.tvPlayer2CorrectCount);

        btnRematch = findViewById(R.id.btnRematch);
        btnExit = findViewById(R.id.btnExit);
    }

    private void displayResults() {
        // Hiển thị điểm
        tvPlayer1Score.setText(String.valueOf(player1Score));
        tvPlayer2Score.setText(String.valueOf(player2Score));

        // Hiển thị số câu đúng dạng "x/total"
        String playerStats = player1CorrectCount + "/" + totalQuestions;
        String opponentStats = player2CorrectCount + "/" + totalQuestions;

        tvPlayer1CorrectCount.setText(playerStats);
        tvPlayer2CorrectCount.setText(opponentStats);

        // Xác định thắng / thua / hòa
        String resultText;
        int colorResId;

        if (player1Score > player2Score) {
            resultText = "THẮNG";
            colorResId = R.color.colorWin;
            tvResultSubTitle.setText("Bạn đã chiến thắng đối thủ!");
        } else if (player1Score < player2Score) {
            resultText = "THUA";
            colorResId = R.color.colorLose;
            tvResultSubTitle.setText("Bạn đã thua. Cố gắng lần sau nhé!");
        } else {
            resultText = "HÒA";
            colorResId = R.color.colorAccent;
            tvResultSubTitle.setText("Một trận đấu ngang tài ngang sức!");
        }

        tvResultTitle.setText(resultText);
        tvResultTitle.setTextColor(ContextCompat.getColor(this, colorResId));

        // Giữ lại Toast tóm tắt như version cũ
        Toast.makeText(
                this,
                "Kết quả: " + resultText +
                        "\nBạn: " + player1Score + " - Đối thủ: " + player2Score,
                Toast.LENGTH_LONG
        ).show();
    }

    private void setupListeners() {
        // Nút Tái đấu
        btnRematch.setOnClickListener(v -> {
            // TODO: Gửi "SEND_REMATCH_REQUEST" qua WebSocket nếu có backend

            btnRematch.setText("Đã gửi lời mời. Đang chờ...");
            btnRematch.setEnabled(false);

            // Giả lập đối thủ chấp nhận sau 2s
            new android.os.Handler().postDelayed(() -> {
                Toast.makeText(this,
                        "Đối thủ đã chấp nhận tái đấu!",
                        Toast.LENGTH_SHORT).show();

                // Bắt đầu trận PvP mới
                Intent intent = new Intent(this, PvPBattleActivity.class);
                // Tuỳ bạn dùng ROOM_CODE/params gì
                intent.putExtra("ROOM_CODE", "ABCD1");
                intent.putExtra("IS_HOST", true);
                startActivity(intent);
                finish();
            }, 2000);
        });

        // Nút Thoát → quay về màn tìm trận
        btnExit.setOnClickListener(v -> {
            // TODO: Gửi "LEAVE_ROOM" qua WebSocket nếu có backend

            Intent intent = new Intent(this, FindMatchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
