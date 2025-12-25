package com.example.iq5.feature.multiplayer.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.iq5.R;
import com.example.iq5.feature.multiplayer.data.WebSocketManager;
import com.example.iq5.feature.multiplayer.data.models.GameResult;
import com.example.iq5.feature.multiplayer.data.models.Question;

import org.json.JSONObject;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MatchActivity extends AppCompatActivity {

    private WebSocketManager wsManager;
    private String matchCode;
    private int myUserId = -1;

    // Game state (Tương tự biến trên Web)
    private int yourScore = 0;
    private int opponentScore = 0;
    private List<Question> questions;
    private int currentQuestionIndex = 0;

    private TextView tvMatchCode, tvTimer, tvYourScore, tvOpponentScore;
    private TextView tvQuestionNumber, tvQuestionText;
    private LinearLayout llAnswers, llGameArea;
    private CardView cardLoading;
    private TextView tvLoading;

    private CountDownTimer countDownTimer;
    private Button selectedButton;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        matchCode = getIntent().getStringExtra("matchCode");
        initViews();
        extractUserIdFromToken();
        setupWebSocket();

        // Gửi lệnh JOIN_MATCH ngay khi vào (Tương đương ws.onopen trên Web)
        joinMatchWhenReady();
    }

    private void initViews() {
        tvMatchCode = findViewById(R.id.tvMatchCode);
        tvTimer = findViewById(R.id.tvTimer);
        tvYourScore = findViewById(R.id.tvYourScore);
        tvOpponentScore = findViewById(R.id.tvOpponentScore);
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvQuestionText = findViewById(R.id.tvQuestionText);
        llAnswers = findViewById(R.id.llAnswers);
        llGameArea = findViewById(R.id.llGameArea);
        cardLoading = findViewById(R.id.cardLoading);
        tvLoading = findViewById(R.id.tvLoading);

        tvMatchCode.setText(matchCode);
        showLoading("⏳ Đang tải câu hỏi...");
    }

    private void setupWebSocket() {
        wsManager = WebSocketManager.getInstance();

        // 1. Nhận danh sách câu hỏi (Tương đương case 'QUESTIONS' trên Web)
        wsManager.setOnQuestionsReceivedListener(list -> runOnUiThread(() -> {
            questions = list;
            if (questions != null && !questions.isEmpty()) {
                hideLoading();
                displayQuestion(0);
            }
        }));

        // 2. Cập nhật điểm thời gian thực (Tương đương case 'SCORE_UPDATE' trên Web)
        wsManager.setOnScoreUpdateListener((userId, questionId, correct) -> runOnUiThread(() -> {
            if (correct) {
                if (userId == myUserId) {
                    yourScore += 100;
                    if (selectedButton != null) selectedButton.setBackgroundResource(R.drawable.bg_answer_correct);
                } else {
                    opponentScore += 100;
                }
            } else if (userId == myUserId) {
                if (selectedButton != null) selectedButton.setBackgroundResource(R.drawable.bg_answer_wrong);
            }

            // Cập nhật UI điểm số
            tvYourScore.setText(String.valueOf(yourScore));
            tvOpponentScore.setText(String.valueOf(opponentScore));

            // Tự động chuyển câu sau 1.2s (Giống setTimeout trên Web)
            handler.postDelayed(() -> displayQuestion(currentQuestionIndex + 1), 1200);
        }));

        // 3. Kết thúc trận đấu (Tương đương case 'GAME_END' trên Web)
        wsManager.setOnGameEndListener(result -> runOnUiThread(() -> {
            if (countDownTimer != null) countDownTimer.cancel();
            openResultScreen(result);
        }));
    }

    private void displayQuestion(int index) {
        if (questions == null || index >= questions.size()) {
            showLoading("⏳ Đang chờ kết quả chung cuộc...");
            return;
        }

        currentQuestionIndex = index;
        selectedButton = null;
        Question q = questions.get(index);

        tvQuestionNumber.setText("Câu " + (index + 1) + "/" + questions.size());
        tvQuestionText.setText(q.getNoiDung());
        llAnswers.removeAllViews();

        String[] answers = {q.getDapAnA(), q.getDapAnB(), q.getDapAnC(), q.getDapAnD()};
        String[] letters = {"A", "B", "C", "D"};

        for (int i = 0; i < 4; i++) {
            if (answers[i] != null && !answers[i].isEmpty()) {
                llAnswers.addView(createAnswerButton(answers[i], letters[i], q.getCauHoiID()));
            }
        }
        startTimer();
    }

    private Button createAnswerButton(String answer, String letter, int questionId) {
        Button btn = new Button(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 20);
        btn.setLayoutParams(params);
        btn.setText(letter + ". " + answer);
        btn.setAllCaps(false);
        btn.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        btn.setPadding(40, 30, 40, 30);
        btn.setBackgroundResource(R.drawable.bg_answer_default);

        btn.setOnClickListener(v -> {
            if (selectedButton != null) return; // Chỉ cho chọn 1 lần
            selectedButton = btn;
            disableAllAnswers();
            btn.setBackgroundResource(R.drawable.bg_answer_selected);

            // Gửi đáp án lên Socket (Tương đương SUBMIT_ANSWER trên Web)
            wsManager.submitAnswer(matchCode, questionId, letter);
            if (countDownTimer != null) countDownTimer.cancel();
        });
        return btn;
    }

    private void startTimer() {
        if (countDownTimer != null) countDownTimer.cancel();
        tvTimer.setText("15s");
        countDownTimer = new CountDownTimer(15000, 1000) {
            public void onTick(long millisUntilFinished) {
                tvTimer.setText((millisUntilFinished / 1000) + "s");
            }
            public void onFinish() {
                // Hết giờ thì gửi đáp án trống (Tương đương timeLeft <= 0 trên Web)
                if (selectedButton == null) {
                    wsManager.submitAnswer(matchCode, questions.get(currentQuestionIndex).getCauHoiID(), "");
                    displayQuestion(currentQuestionIndex + 1);
                }
            }
        }.start();
    }

    private void joinMatchWhenReady() {
        handler.postDelayed(() -> {
            if (wsManager.isConnected()) {
                wsManager.joinMatch(matchCode);
            }
        }, 500);
    }

    private void extractUserIdFromToken() {
        try {
            SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
            String token = prefs.getString("auth_token", "");
            String[] parts = token.split("\\.");
            String payload = new String(Base64.decode(parts[1], Base64.URL_SAFE), StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(payload);
            myUserId = json.optInt("nameid", -1); // Lấy ID của mình từ JWT giống hàm parseJwt trên Web
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void disableAllAnswers() {
        for (int i = 0; i < llAnswers.getChildCount(); i++) llAnswers.getChildAt(i).setEnabled(false);
    }

    private void showLoading(String msg) {
        cardLoading.setVisibility(View.VISIBLE);
        llGameArea.setVisibility(View.GONE);
        tvLoading.setText(msg);
    }

    private void hideLoading() {
        cardLoading.setVisibility(View.GONE);
        llGameArea.setVisibility(View.VISIBLE);
    }

    private void openResultScreen(GameResult result) {
        Intent i = new Intent(this, MatchResultActivity.class);
        i.putExtra("matchCode", matchCode);
        i.putExtra("yourScore", yourScore);
        i.putExtra("opponentScore", opponentScore);
        i.putExtra("result", result.getKetQua());
        startActivity(i);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}