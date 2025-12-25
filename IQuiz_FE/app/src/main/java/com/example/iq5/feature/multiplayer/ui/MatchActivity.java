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

    private static final String TAG = "MatchActivity";

    // ===== Core =====
    private WebSocketManager wsManager;
    private String matchCode;
    private int myUserId = -1;

    // ===== Game state =====
    private int yourScore = 0;
    private int opponentScore = 0;
    private List<Question> questions;
    private int currentQuestionIndex = 0;

    // ===== UI =====
    private TextView tvMatchCode, tvTimer, tvYourScore, tvOpponentScore;
    private TextView tvQuestionNumber, tvQuestionText;
    private LinearLayout llAnswers, llGameArea;
    private CardView cardLoading;
    private TextView tvLoading;

    // ===== Helpers =====
    private CountDownTimer countDownTimer;
    private Button selectedButton;
    private final Handler handler = new Handler(Looper.getMainLooper());

    // ======================================================
    // LIFECYCLE
    // ======================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        matchCode = getIntent().getStringExtra("matchCode");
        if (matchCode == null || matchCode.trim().isEmpty()) {
            Toast.makeText(this, "❌ Mã trận đấu không hợp lệ", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        extractUserIdFromToken();
        setupWebSocket();
        joinMatchWhenReady();
    }

    // ======================================================
    // INIT
    // ======================================================
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
        tvYourScore.setText("0");
        tvOpponentScore.setText("0");

        showLoading("⏳ Đang tải câu hỏi...");
    }

    private void extractUserIdFromToken() {
        try {
            SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
            String token = prefs.getString("auth_token", "");
            if (token == null || token.isEmpty()) return;

            String[] parts = token.split("\\.");
            if (parts.length < 2) return;

            String payload = new String(
                    Base64.decode(parts[1], Base64.URL_SAFE),
                    StandardCharsets.UTF_8
            );
            JSONObject json = new JSONObject(payload);
            myUserId = json.optInt("nameid", -1);

        } catch (Exception e) {
            Log.e(TAG, "❌ Parse token failed", e);
        }
    }

    // ======================================================
    // WEBSOCKET
    // ======================================================
    private void setupWebSocket() {
        wsManager = WebSocketManager.getInstance();

        wsManager.setOnQuestionsReceivedListener(list ->
                runOnUiThread(() -> {
                    questions = list;
                    if (questions == null || questions.isEmpty()) {
                        showLoading("❌ Không có câu hỏi");
                        return;
                    }
                    hideLoading();
                    displayQuestion(0);
                })
        );

        wsManager.setOnScoreUpdateListener((userId, questionId, correct) ->
                runOnUiThread(() -> {
                    if (correct) {
                        if (userId == myUserId) {
                            yourScore += 100;
                            highlightCorrectAnswer();
                        } else {
                            opponentScore += 100;
                        }
                    } else if (userId == myUserId) {
                        highlightWrongAnswer();
                    }

                    tvYourScore.setText(String.valueOf(yourScore));
                    tvOpponentScore.setText(String.valueOf(opponentScore));

                    handler.postDelayed(
                            () -> displayQuestion(currentQuestionIndex + 1),
                            1200
                    );
                })
        );

        wsManager.setOnGameEndListener(result ->
                runOnUiThread(() -> openResultScreen(result))
        );
    }

    private void joinMatchWhenReady() {
        handler.postDelayed(() -> {
            if (wsManager.isConnected()) {
                wsManager.joinMatch(matchCode);
            } else {
                Toast.makeText(this, "❌ Chưa kết nối WebSocket", Toast.LENGTH_SHORT).show();
            }
        }, 400);
    }

    // ======================================================
    // GAME LOGIC
    // ======================================================
    private void displayQuestion(int index) {
        if (questions == null || index >= questions.size()) {
            showLoading("⏳ Đang chờ kết quả...");
            return;
        }

        currentQuestionIndex = index;
        selectedButton = null;

        Question q = questions.get(index);

        tvQuestionNumber.setText(
                "Câu " + (index + 1) + "/" + questions.size()
        );
        tvQuestionText.setText(q.getNoiDung());
        llAnswers.removeAllViews();

        String[] answers = {
                q.getDapAnA(),
                q.getDapAnB(),
                q.getDapAnC(),
                q.getDapAnD()
        };
        String[] letters = {"A", "B", "C", "D"};

        for (int i = 0; i < 4; i++) {
            if (answers[i] != null && !answers[i].trim().isEmpty()) {
                llAnswers.addView(
                        createAnswerButton(answers[i], letters[i], q.getCauHoiID())
                );
            }
        }

        startTimer();
    }

    private Button createAnswerButton(String answer, String letter, int questionId) {
        Button btn = new Button(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 20);
        btn.setLayoutParams(params);

        btn.setText(letter + ". " + answer);
        btn.setAllCaps(false);
        btn.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        btn.setPadding(40, 30, 40, 30);
        btn.setBackgroundResource(R.drawable.bg_answer_default);
        btn.setTextColor(Color.parseColor("#2c3e50"));

        btn.setOnClickListener(v -> {
            if (selectedButton != null) return;

            selectedButton = btn;
            disableAllAnswers();
            btn.setBackgroundResource(R.drawable.bg_answer_selected);
            submitAnswer(questionId, letter);
        });

        return btn;
    }

    private void startTimer() {
        if (countDownTimer != null) countDownTimer.cancel();

        tvTimer.setText("15s");
        tvTimer.setTextColor(Color.parseColor("#27ae60"));

        countDownTimer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int sec = (int) (millisUntilFinished / 1000);
                tvTimer.setText(sec + "s");
                if (sec <= 5) tvTimer.setTextColor(Color.RED);
            }

            @Override
            public void onFinish() {
                submitAnswer(
                        questions.get(currentQuestionIndex).getCauHoiID(),
                        "TIMEOUT"
                );
            }
        }.start();
    }

    private void submitAnswer(int questionId, String answer) {
        if (countDownTimer != null) countDownTimer.cancel();
        wsManager.submitAnswer(matchCode, questionId, answer);
    }

    private void disableAllAnswers() {
        for (int i = 0; i < llAnswers.getChildCount(); i++) {
            llAnswers.getChildAt(i).setEnabled(false);
        }
    }

    // ======================================================
    // UI HELPERS
    // ======================================================
    private void highlightCorrectAnswer() {
        if (selectedButton != null) {
            selectedButton.setBackgroundResource(R.drawable.bg_answer_correct);
        }
    }

    private void highlightWrongAnswer() {
        if (selectedButton != null) {
            selectedButton.setBackgroundResource(R.drawable.bg_answer_wrong);
        }
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

    // ======================================================
    // RESULT
    // ======================================================
    private void openResultScreen(GameResult result) {
        if (countDownTimer != null) countDownTimer.cancel();

        Intent i = new Intent(this, MatchResultActivity.class);
        i.putExtra("matchCode", matchCode);
        i.putExtra("yourScore", yourScore);
        i.putExtra("opponentScore", opponentScore);
        i.putExtra("result", result.getKetQua());
        i.putExtra(
                "winnerUserId",
                result.getWinnerUserId() != null ? result.getWinnerUserId() : -1
        );

        startActivity(i);
        finish();
    }

    // ======================================================
    // CLEANUP
    // ======================================================
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        if (countDownTimer != null) countDownTimer.cancel();
    }
}
