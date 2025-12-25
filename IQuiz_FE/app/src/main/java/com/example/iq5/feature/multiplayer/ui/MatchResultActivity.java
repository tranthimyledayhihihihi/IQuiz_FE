package com.example.iq5.feature.multiplayer.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.iq5.R;
import com.example.iq5.feature.multiplayer.data.WebSocketManager;
import com.example.iq5.feature.multiplayer.data.models.CauHoiDisplayModel;
import com.example.iq5.feature.multiplayer.data.models.GameResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MatchResultActivity extends AppCompatActivity {

    private WebSocketManager wsManager;
    private String matchCode;
    private int myUserId;
    private int yourScore = 0;
    private int opponentScore = 0;

    private List<CauHoiDisplayModel> currentQuestions;
    private int currentQuestionIndex = 0;
    private CountDownTimer countDownTimer;
    private int timeLeft = 15;

    private TextView tvMatchCode, tvTimer, tvYourScore, tvOpponentScore;
    private LinearLayout llGameArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        matchCode = getIntent().getStringExtra("matchCode");
        if (matchCode == null || matchCode.trim().isEmpty()) {
            Toast.makeText(this, "❌ Mã trận đấu không hợp lệ!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        getUserIdFromToken();
        setupWebSocket();

        // Delay 500ms rồi connect (giống web)
        new Handler(Looper.getMainLooper()).postDelayed(this::connectToMatch, 500);
    }

    private void initViews() {
        tvMatchCode = findViewById(R.id.tvMatchCode);
        tvTimer = findViewById(R.id.tvTimer);
        tvYourScore = findViewById(R.id.tvYourScore);
        tvOpponentScore = findViewById(R.id.tvOpponentScore);
        llGameArea = findViewById(R.id.llGameArea);

        tvMatchCode.setText(matchCode);
        showLoading("Đang tải câu hỏi...");
    }

    // ✅ GIẢI MÃ TOKEN ĐỂ LẤY USER ID
    private void getUserIdFromToken() {
        try {
            String token = getSharedPreferences("app_prefs", MODE_PRIVATE)
                    .getString("auth_token", "");

            if (token.isEmpty()) {
                Toast.makeText(this, "❌ Chưa đăng nhập!", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            String[] parts = token.split("\\.");
            if (parts.length > 1) {
                String payload = new String(
                        Base64.decode(parts[1], Base64.URL_SAFE),
                        StandardCharsets.UTF_8
                );
                JSONObject json = new JSONObject(payload);
                myUserId = json.getInt("nameid");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupWebSocket() {
        wsManager = WebSocketManager.getInstance();

        // ✅ QUESTIONS RECEIVED
        wsManager.setOnQuestionsReceivedListener(questions -> {
            runOnUiThread(() -> {
                currentQuestions = questions;
                if (currentQuestions != null && !currentQuestions.isEmpty()) {
                    displayQuestion(0);
                }
            });
        });

        // ✅ SCORE UPDATE
        wsManager.setOnScoreUpdateListener((userId, questionId, correct) -> {
            runOnUiThread(() -> updateScores(userId, correct));
        });

        // ✅ GAME END
        wsManager.setOnGameEndListener(result -> {
            runOnUiThread(() -> {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                showGameResult(result);
            });
        });
    }

    private void connectToMatch() {
        // Gửi message JOIN_MATCH
        wsManager.joinMatch(matchCode);
    }
    // ✅ HIỂN THỊ CÂU HỎI (GIỐNG WEB)
    private void displayQuestion(int index) {
        if (currentQuestions == null || index >= currentQuestions.size()) {
            showLoading("⏳ Đang chờ kết quả...");
            return;
        }

        currentQuestionIndex = index;
        CauHoiDisplayModel q = currentQuestions.get(index);

        // Clear previous content
        llGameArea.removeAllViews();

        // Question number and text
        TextView tvQuestionNumber = new TextView(this);
        tvQuestionNumber.setText("Câu " + (index + 1) + "/" + currentQuestions.size());
        tvQuestionNumber.setTextSize(14);
        tvQuestionNumber.setTextColor(getResources().getColor(android.R.color.darker_gray));
        tvQuestionNumber.setPadding(0, 0, 0, 20);
        llGameArea.addView(tvQuestionNumber);

        TextView tvQuestionText = new TextView(this);
        tvQuestionText.setText(q.getNoiDung());
        tvQuestionText.setTextSize(20);
        tvQuestionText.setTextColor(getResources().getColor(android.R.color.black));
        tvQuestionText.setPadding(0, 0, 0, 40);
        llGameArea.addView(tvQuestionText);

        // ✅ PARSE CacLuaChon (JSON A/B/C/D) từ backend
        try {
            String rawChoices = q.getCacLuaChon();
            if (rawChoices == null || rawChoices.trim().isEmpty()) rawChoices = "{}";

            Gson gson = new Gson();
            JsonObject choices = gson.fromJson(rawChoices, JsonObject.class);

            String[] letters = {"A", "B", "C", "D"};
            for (String letter : letters) {
                if (choices != null && choices.has(letter) && !choices.get(letter).isJsonNull()) {
                    String answerText = choices.get(letter).getAsString();
                    if (answerText != null && !answerText.trim().isEmpty()) {
                        Button btnAnswer = createAnswerButton(letter, answerText, q.getCauHoiID());
                        llGameArea.addView(btnAnswer);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Nếu parse lỗi thì vẫn cho user thấy thông báo thay vì crash
            TextView tvErr = new TextView(this);
            tvErr.setText("⚠️ Lỗi đọc đáp án (CacLuaChon).");
            tvErr.setPadding(0, 20, 0, 0);
            llGameArea.addView(tvErr);
        }

        startTimer();
    }

    // ✅ TẠO NÚT ĐÁP ÁN
    private Button createAnswerButton(String letter, String text, int questionId) {
        Button btn = new Button(this);
        btn.setText(letter + ". " + text);
        btn.setTag(R.id.tag_answer, letter);
        btn.setTag(R.id.tag_question_id, questionId);
        btn.setBackgroundResource(R.drawable.answer_button_background);
        btn.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        btn.setPadding(30, 40, 30, 40);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 30);
        btn.setLayoutParams(params);

        btn.setOnClickListener(v -> {
            disableAllAnswers();
            btn.setSelected(true);
            btn.setBackgroundResource(R.drawable.answer_selected_background);

            String selectedAnswer = (String) btn.getTag(R.id.tag_answer);
            int qId = (int) btn.getTag(R.id.tag_question_id);

            submitAnswer(qId, selectedAnswer);
        });

        return btn;
    }

    // ✅ DISABLE TẤT CẢ NÚT ĐÁP ÁN
    private void disableAllAnswers() {
        for (int i = 0; i < llGameArea.getChildCount(); i++) {
            View child = llGameArea.getChildAt(i);
            if (child instanceof Button) {
                child.setEnabled(false);
            }
        }
    }

    // ✅ START TIMER (GIỐNG WEB)
    private void startTimer() {
        timeLeft = 15;
        tvTimer.setText(timeLeft + "s");

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = (int) (millisUntilFinished / 1000);
                tvTimer.setText(timeLeft + "s");
            }

            @Override
            public void onFinish() {
                if (currentQuestions != null && currentQuestionIndex < currentQuestions.size()) {
                    CauHoiDisplayModel q = currentQuestions.get(currentQuestionIndex);
                    submitAnswer(q.getCauHoiID(), "");
                }
            }
        }.start();
    }

    // ✅ SUBMIT ANSWER (GIỐNG WEB)
    private void submitAnswer(int questionId, String answer) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        int timeSpent = 15 - timeLeft;
        wsManager.submitAnswer(matchCode, questionId, answer);

        // Delay 2s rồi hiển thị câu tiếp theo
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            displayQuestion(currentQuestionIndex + 1);
        }, 2000);
    }

    // ✅ CẬP NHẬT ĐIỂM (TỰ TÍNH - GIỐNG WEB)
    private void updateScores(int userId, boolean correct) {
        if (correct) {
            if (userId == myUserId) {
                yourScore += 100;
                highlightCorrectAnswer();
            } else {
                opponentScore += 100;
            }
        } else {
            if (userId == myUserId) {
                highlightWrongAnswer();
            }
        }

        tvYourScore.setText(String.valueOf(yourScore));
        tvOpponentScore.setText(String.valueOf(opponentScore));
    }

    // ✅ TÔ MÀU XANH ĐÁP ÁN ĐÚNG
    private void highlightCorrectAnswer() {
        for (int i = 0; i < llGameArea.getChildCount(); i++) {
            View child = llGameArea.getChildAt(i);
            if (child instanceof Button && child.isSelected()) {
                child.setBackgroundResource(R.drawable.answer_correct_background);
            }
        }
    }

    // ✅ TÔ MÀU ĐỎ ĐÁP ÁN SAI
    private void highlightWrongAnswer() {
        for (int i = 0; i < llGameArea.getChildCount(); i++) {
            View child = llGameArea.getChildAt(i);
            if (child instanceof Button && child.isSelected()) {
                child.setBackgroundResource(R.drawable.answer_wrong_background);
            }
        }
    }

    // ✅ HIỂN THỊ KẾT QUẢ (GIỐNG WEB)
    private void showGameResult(GameResult result) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        tvTimer.setVisibility(View.GONE);

        String resultClass;
        String resultText;

        if (yourScore > opponentScore) {
            resultClass = "winner";
            resultText = "🎉 Bạn Chiến Thắng!";
        } else if (yourScore < opponentScore) {
            resultClass = "loser";
            resultText = "😢 Bạn Thua Cuộc!";
        } else {
            resultClass = "draw";
            resultText = "🤝 Hòa!";
        }

        new AlertDialog.Builder(this)
                .setTitle("🏁 Trận Đấu Kết Thúc!")
                .setMessage(resultText + "\n\n" +
                        "🎯 Điểm của bạn: " + yourScore + "\n" +
                        "👤 Điểm đối thủ: " + opponentScore)
                .setPositiveButton("🏠 Trở về Lobby", (dialog, which) -> {
                    returnToLobby();
                })
                .setCancelable(false)
                .show();
    }

    private void returnToLobby() {
        if (wsManager.isConnected()) {
            // Không cần disconnect vì còn activity khác dùng
        }
        finish();
    }

    private void showLoading(String message) {
        llGameArea.removeAllViews();

        TextView tvLoading = new TextView(this);
        tvLoading.setText("⏳\n\n" + message);
        tvLoading.setTextSize(20);
        tvLoading.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvLoading.setPadding(40, 100, 40, 100);

        llGameArea.addView(tvLoading);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}