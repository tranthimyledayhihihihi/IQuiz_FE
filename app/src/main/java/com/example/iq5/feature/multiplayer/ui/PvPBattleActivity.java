package com.example.iq5.feature.multiplayer.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.iq5.R;

public class PvPBattleActivity extends AppCompatActivity {

    private TextView tvPlayer1Score, tvPlayer2Score, tvTimer, tvQuestion;
    private Button btnAnswerA, btnAnswerB, btnAnswerC, btnAnswerD;
    private ProgressBar progressTime;
    private CountDownTimer countDownTimer;

    private int player1Score = 0;
    private int player2Score = 0;
    private int currentQuestionIndex = 0;
    private final int TOTAL_QUESTIONS = 10;
    private final long QUESTION_TIME_LIMIT = 15000; // 15 giây

    private String correctAnswer = "A"; // Giả lập đáp án đúng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pvp_battle);

        initView();
        setupListeners();
        loadQuestion(currentQuestionIndex);

        // TODO: Kết nối WebSocket để nhận/gửi sự kiện
        // 1. "RECEIVE_QUESTION": Nhận câu hỏi mới từ server
        // 2. "SEND_ANSWER": Gửi câu trả lời của mình
        // 3. "UPDATE_SCORE": Nhận cập nhật điểm của đối thủ
        // 4. "GAME_END": Nhận sự kiện kết thúc trận đấu
    }

    private void initView() {
        tvPlayer1Score = findViewById(R.id.tvPlayer1Score);
        tvPlayer2Score = findViewById(R.id.tvPlayer2Score);
        tvTimer = findViewById(R.id.tvTimer);
        tvQuestion = findViewById(R.id.tvQuestion);
        btnAnswerA = findViewById(R.id.btnAnswerA);
        btnAnswerB = findViewById(R.id.btnAnswerB);
        btnAnswerC = findViewById(R.id.btnAnswerC);
        btnAnswerD = findViewById(R.id.btnAnswerD);
        progressTime = findViewById(R.id.progressTime);
        progressTime.setMax((int) (QUESTION_TIME_LIMIT / 1000));
    }

    private void setupListeners() {
        btnAnswerA.setOnClickListener(v -> submitAnswer("A"));
        btnAnswerB.setOnClickListener(v -> submitAnswer("B"));
        btnAnswerC.setOnClickListener(v -> submitAnswer("C"));
        btnAnswerD.setOnClickListener(v -> submitAnswer("D"));
    }

    private void loadQuestion(int index) {
        if (index >= TOTAL_QUESTIONS) {
            endGame();
            return;
        }

        // TODO: Lấy câu hỏi từ API/WebSocket
        // Giả lập câu hỏi mới
        tvQuestion.setText("Câu hỏi " + (index + 1) + ": Thủ đô của Việt Nam là gì?");
        btnAnswerA.setText("A. Hà Nội");
        btnAnswerB.setText("B. TP. HCM");
        btnAnswerC.setText("C. Đà Nẵng");
        btnAnswerD.setText("D. Hải Phòng");
        correctAnswer = "A";

        resetAnswerButtons();
        startTimer();
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(QUESTION_TIME_LIMIT, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsLeft = (int) (millisUntilFinished / 1000);
                tvTimer.setText(String.valueOf(secondsLeft));
                progressTime.setProgress(secondsLeft);
            }

            @Override
            public void onFinish() {
                tvTimer.setText("0");
                // Tự động chuyển câu khi hết giờ
                submitAnswer("TIMEOUT"); // Gửi sự kiện hết giờ
            }
        }.start();
    }

    private void submitAnswer(String answer) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        setAnswersEnabled(false);

        // TODO: Gửi câu trả lời ("SEND_ANSWER") lên server

        boolean isCorrect = answer.equals(correctAnswer);
        if (isCorrect) {
            player1Score += 10; // Tính điểm
            tvPlayer1Score.setText("Điểm: " + player1Score);
        }

        // Hiển thị đáp án đúng/sai
        showAnswerResult(answer, isCorrect);

        // TODO: Lắng nghe "UPDATE_SCORE" từ đối thủ
        // Giả lập đối thủ trả lời
        // onOpponentAnswered(true, 10);

        // Chờ 2s rồi chuyển câu
        new android.os.Handler().postDelayed(() -> {
            currentQuestionIndex++;
            loadQuestion(currentQuestionIndex);
        }, 2000);
    }

    // Hàm này được gọi khi nhận sự kiện "UPDATE_SCORE"
    private void onOpponentAnswered(boolean correct, int scoreGained) {
        if (correct) {
            player2Score += scoreGained;
            tvPlayer2Score.setText("Điểm: " + player2Score);
            // Có thể thêm hiệu ứng nhỏ (VD: +10 bay lên)
        }
    }

    private void showAnswerResult(String selectedAnswer, boolean isCorrect) {
        // Reset all
        btnAnswerA.setBackgroundColor(ContextCompat.getColor(this, R.color.colorButtonNormal));
        btnAnswerB.setBackgroundColor(ContextCompat.getColor(this, R.color.colorButtonNormal));
        btnAnswerC.setBackgroundColor(ContextCompat.getColor(this, R.color.colorButtonNormal));
        btnAnswerD.setBackgroundColor(ContextCompat.getColor(this, R.color.colorButtonNormal));

        // Highlight đáp án đúng
        Button correctButton = getButtonByAnswer(correctAnswer);
        if (correctButton != null) {
            correctButton.setBackgroundColor(Color.GREEN);
        }

        // Highlight đáp án sai (nếu chọn sai)
        if (!isCorrect && !selectedAnswer.equals("TIMEOUT")) {
            Button selectedButton = getButtonByAnswer(selectedAnswer);
            if (selectedButton != null) {
                selectedButton.setBackgroundColor(Color.RED);
            }
        }
    }

    private Button getButtonByAnswer(String answer) {
        switch (answer) {
            case "A": return btnAnswerA;
            case "B": return btnAnswerB;
            case "C": return btnAnswerC;
            case "D": return btnAnswerD;
            default: return null;
        }
    }

    private void resetAnswerButtons() {
        setAnswersEnabled(true);
        btnAnswerA.setBackgroundColor(ContextCompat.getColor(this, R.color.colorButtonNormal));
        btnAnswerB.setBackgroundColor(ContextCompat.getColor(this, R.color.colorButtonNormal));
        btnAnswerC.setBackgroundColor(ContextCompat.getColor(this, R.color.colorButtonNormal));
        btnAnswerD.setBackgroundColor(ContextCompat.getColor(this, R.color.colorButtonNormal));
    }

    private void setAnswersEnabled(boolean enabled) {
        btnAnswerA.setEnabled(enabled);
        btnAnswerB.setEnabled(enabled);
        btnAnswerC.setEnabled(enabled);
        btnAnswerD.setEnabled(enabled);
    }

    private void endGame() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        // TODO: Gửi sự kiện "FINISH_GAME" và điểm số
        Toast.makeText(this, "Kết thúc trận đấu!", Toast.LENGTH_SHORT).show();

        // Chuyển sang màn hình So Sánh Kết Quả
        Intent intent = new Intent(this, CompareResultActivity.class);
        intent.putExtra("PLAYER_1_SCORE", player1Score);
        intent.putExtra("PLAYER_2_SCORE", player2Score);
        // intent.putExtra("MATCH_ID", ...);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        // TODO: Ngắt kết nối WebSocket
    }
}