package com.example.iq5.feature.multiplayer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.iq5.R;
import com.google.android.material.button.MaterialButton; // Sử dụng MaterialButton

public class CompareResultActivity extends AppCompatActivity {

    // Khai báo Views
    private TextView tvResultTitle, tvResultSubTitle;
    private TextView tvPlayer1Score, tvPlayer2Score;

    // KHAI BÁO BỔ SUNG: Số câu trả lời đúng
    private TextView tvPlayer1CorrectCount, tvPlayer2CorrectCount;

    private MaterialButton btnRematch, btnExit; // Sử dụng MaterialButton

    // Biến lưu trữ kết quả cuối cùng
    private int player1Score;
    private int player2Score;
    private int player1CorrectCount; // Số câu đúng của bạn
    private int player2CorrectCount; // Số câu đúng của đối thủ
    private int totalQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_result);

        // 1. NHẬN DỮ LIỆU TỪ INTENT (ĐÃ SỬA KHÓA DỮ LIỆU)
        Intent intent = getIntent();
        player1Score = intent.getIntExtra("PLAYER_SCORE", 0);
        player2Score = intent.getIntExtra("OPPONENT_SCORE", 0);
        player1CorrectCount = intent.getIntExtra("PLAYER_CORRECT_COUNT", 0);
        player2CorrectCount = intent.getIntExtra("OPPONENT_CORRECT_COUNT", 0);
        totalQuestions = intent.getIntExtra("TOTAL_QUESTIONS", 10);

        // TODO: Lấy thông tin chi tiết trận đấu (MATCH_ID) để hiển thị thêm

        initView();
        displayResults();
        setupListeners();
    }
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.R;
import com.example.iq5.core.navigation.NavigationHelper;
import android.widget.Toast;


public class CompareResultActivity extends AppCompatActivity {

    private TextView tvResultStatus, tvYourFinalScore, tvOpponentFinalScore;
    private Button btnPlayAgain, btnBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        displayResults();
        setupButtons();
    }

    private void displayResults() {
        int yourScore = getIntent().getIntExtra("your_score", 100);
        int opponentScore = getIntent().getIntExtra("opponent_score", 80);
        boolean isWinner = getIntent().getBooleanExtra("is_winner", true);

        String result = isWinner ? "🎉 CHIẾN THẮNG!" :
                       (yourScore == opponentScore ? "🤝 HÒA!" : "😞 THUA CUỘC");

        Toast.makeText(this, result + "\nBạn: " + yourScore + " - Đối thủ: " + opponentScore,
                      Toast.LENGTH_LONG).show();
    }

    private void setupButtons() {
        // Sử dụng button có sẵn trong activity_result
        if (findViewById(R.id.btn_play_again) != null) {
            findViewById(R.id.btn_play_again).setOnClickListener(v -> {
                NavigationHelper.navigateToFindMatch(this);
                finish();
            });
        }

        if (findViewById(R.id.btn_retry) != null) {
            findViewById(R.id.btn_retry).setOnClickListener(v -> {
                NavigationHelper.navigateToHome(this, false);
                finish();
            });
        }
    }
}

    private void initView() {
        tvResultTitle = findViewById(R.id.tvResultTitle);
        tvResultSubTitle = findViewById(R.id.tvResultSubTitle);

        // Ánh xạ các views cho Điểm số và Số câu đúng
        tvPlayer1Score = findViewById(R.id.tvPlayer1Score);
        tvPlayer2Score = findViewById(R.id.tvPlayer2Score);
        tvPlayer1CorrectCount = findViewById(R.id.tvPlayer1CorrectCount);
        tvPlayer2CorrectCount = findViewById(R.id.tvPlayer2CorrectCount);

        // Ánh xạ nút (Đảm bảo ID khớp với layout mới)
        btnRematch = findViewById(R.id.btnRematch);
        btnExit = findViewById(R.id.btnExit);
    }

    private void displayResults() {
        // 2. HIỂN THỊ ĐIỂM SỐ VÀ THỐNG KÊ (ĐÃ FIX LỖI HIỂN THỊ)

        // Cập nhật Điểm số (36sp)
        tvPlayer1Score.setText(String.valueOf(player1Score));
        tvPlayer2Score.setText(String.valueOf(player2Score));

        // Cập nhật Số câu trả lời đúng (Thống kê)
        String playerStats = player1CorrectCount + "/" + totalQuestions;
        String opponentStats = player2CorrectCount + "/" + totalQuestions;

        tvPlayer1CorrectCount.setText(playerStats);
        tvPlayer2CorrectCount.setText(opponentStats);

        // 3. LOGIC THẮNG/THUA/HÒA
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
            tvResultTitle.setTextColor(ContextCompat.getColor(this, R.color.colorAccent)); // Sử dụng colorAccent cho HÒA
            tvResultSubTitle.setText("Một trận đấu ngang tài ngang sức!");
        }
    }

    private void setupListeners() {
        btnRematch.setOnClickListener(v -> {
            // TODO: Gửi "SEND_REMATCH_REQUEST" qua WebSocket
            btnRematch.setText("Đã gửi lời mời. Đang chờ...");
            btnRematch.setEnabled(false);

            // Giả lập đối thủ chấp nhận sau 2s
            new android.os.Handler().postDelayed(() -> {
                Toast.makeText(this, "Đối thủ đã chấp nhận tái đấu!", Toast.LENGTH_SHORT).show();

                // Bắt đầu lại trận đấu mới
                Intent intent = new Intent(this, PvPBattleActivity.class);
                // Giả sử chuyển lại RoomLobby để bắt đầu trận mới
                intent.putExtra("ROOM_CODE", "ABCD1"); // Mã phòng cũ/mới
                intent.putExtra("IS_HOST", true); // Bạn vẫn là host
                startActivity(intent);
                finish();
            }, 2000);
        });

        btnExit.setOnClickListener(v -> {
            // TODO: Gửi "LEAVE_ROOM" qua WebSocket
            // Quay về màn hình tìm trận chính (FindMatchActivity)
            Intent intent = new Intent(this, FindMatchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}