package com.example.iq5.feature.multiplayer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MatchResultActivity extends AppCompatActivity {

    private static final String TAG = "MatchActivity"; // Đổi TAG để dễ lọc Logcat

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

        // ✅ GỬI LỆNH JOIN NGAY LẬP TỨC (KHÔNG DELAY)
        connectToMatch();
    }

    private void initViews() {
        tvMatchCode = findViewById(R.id.tvMatchCode);
        tvTimer = findViewById(R.id.tvTimer);
        tvYourScore = findViewById(R.id.tvYourScore);
        tvOpponentScore = findViewById(R.id.tvOpponentScore);
        llGameArea = findViewById(R.id.llGameArea);

        tvMatchCode.setText(matchCode);
        tvYourScore.setText("0");
        tvOpponentScore.setText("0");
        showLoading("⏳ Đang đợi đối thủ và tải câu hỏi...");
    }

    private void getUserIdFromToken() {
        try {
            String token = getSharedPreferences("app_prefs", MODE_PRIVATE).getString("auth_token", "");
            if (token.isEmpty()) return;

            String[] parts = token.split("\\.");
            if (parts.length > 1) {
                String payload = new String(Base64.decode(parts[1], Base64.URL_SAFE), StandardCharsets.UTF_8);
                JSONObject json = new JSONObject(payload);
                myUserId = json.getInt("nameid"); // Key nameid từ Backend JWT
                Log.d(TAG, "✅ My User ID: " + myUserId);
            }
        } catch (Exception e) {
            Log.e(TAG, "❌ Parse token error", e);
        }
    }

    private void setupWebSocket() {
        wsManager = WebSocketManager.getInstance();

        // 1. NHẬN CÂU HỎI TỪ SERVER
        wsManager.setOnQuestionsReceivedListener(questions -> runOnUiThread(() -> {
            Log.d(TAG, "📩 Nhận được " + (questions == null ? 0 : questions.size()) + " câu hỏi");
            currentQuestions = questions;

            if (currentQuestions != null && !currentQuestions.isEmpty()) {
                displayQuestion(0);
            } else {
                showLoading("⚠️ Server trả về 0 câu hỏi!");
            }
        }));

        // 2. CẬP NHẬT ĐIỂM REALTIME (GIỐNG WEB)
        wsManager.setOnScoreUpdateListener((userId, questionId, correct) -> runOnUiThread(() -> {
            Log.d(TAG, "📊 Score update: user=" + userId + ", correct=" + correct);
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
        }));

        // 3. KẾT THÚC GAME
        wsManager.setOnGameEndListener(result -> runOnUiThread(() -> {
            if (countDownTimer != null) countDownTimer.cancel();
            showGameResult(result);
        }));
    }

    private void connectToMatch() {
        if (wsManager.isConnected()) {
            Log.d(TAG, "📤 Sending JOIN_MATCH for: " + matchCode);
            wsManager.joinMatch(matchCode);
        }
    }

    private void displayQuestion(int index) {
        if (currentQuestions == null || index >= currentQuestions.size()) {
            showLoading("⏳ Đang chờ đối thủ hoàn thành...");
            return;
        }

        currentQuestionIndex = index;
        CauHoiDisplayModel q = currentQuestions.get(index);
        llGameArea.removeAllViews();

        // Hiển thị số thứ tự câu hỏi
        TextView tvNum = new TextView(this);
        tvNum.setText("Câu " + (index + 1) + "/" + currentQuestions.size());
        tvNum.setPadding(0, 0, 0, 20);
        llGameArea.addView(tvNum);

        // Hiển thị nội dung câu hỏi
        TextView tvQText = new TextView(this);
        tvQText.setText(q.getNoiDung());
        tvQText.setTextSize(20);
        tvQText.setTextColor(getResources().getColor(android.R.color.black));
        tvQText.setPadding(0, 0, 0, 40);
        llGameArea.addView(tvQText);

        // ✅ PARSE ĐÁP ÁN LINH HOẠT (THỬ CẢ HOA VÀ THƯỜNG)
        try {
            JsonObject choices = new Gson().fromJson(q.getCacLuaChon(), JsonObject.class);
            String[] letters = {"A", "B", "C", "D"};
            int count = 0;

            for (String letter : letters) {
                // Thử lấy key "A", nếu null thử lấy key "a"
                JsonElement element = choices.get(letter);
                if (element == null || element.isJsonNull()) {
                    element = choices.get(letter.toLowerCase());
                }

                if (element != null && !element.isJsonNull()) {
                    String text = element.getAsString();
                    if (!text.isEmpty()) {
                        llGameArea.addView(createAnswerButton(letter, text, q.getCauHoiID()));
                        count++;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "❌ Error parsing answers", e);
        }

        startTimer();
    }

    private Button createAnswerButton(String letter, String text, int qId) {
        Button btn = new Button(this);
        btn.setText(letter + ". " + text);
        btn.setTag(R.id.tag_answer, letter);
        btn.setTag(R.id.tag_question_id, qId);
        btn.setBackgroundResource(R.drawable.answer_button_background);
        btn.setOnClickListener(v -> {
            disableAllAnswers();
            v.setSelected(true);
            v.setBackgroundResource(R.drawable.answer_selected_background);
            submitAnswer(qId, letter);
        });

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.setMargins(0, 0, 0, 30);
        btn.setLayoutParams(p);
        return btn;
    }

    private void startTimer() {
        timeLeft = 15;
        if (countDownTimer != null) countDownTimer.cancel();
        countDownTimer = new CountDownTimer(15000, 1000) {
            public void onTick(long ms) {
                timeLeft = (int) (ms / 1000);
                tvTimer.setText(timeLeft + "s");
            }
            public void onFinish() {
                submitAnswer(currentQuestions.get(currentQuestionIndex).getCauHoiID(), "");
            }
        }.start();
    }

    private void submitAnswer(int qId, String ans) {
        if (countDownTimer != null) countDownTimer.cancel();
        wsManager.submitAnswer(matchCode, qId, ans);
        new Handler(Looper.getMainLooper()).postDelayed(() -> displayQuestion(currentQuestionIndex + 1), 2000);
    }

    private void highlightCorrectAnswer() {
        for (int i = 0; i < llGameArea.getChildCount(); i++) {
            View v = llGameArea.getChildAt(i);
            if (v instanceof Button && v.isSelected()) v.setBackgroundResource(R.drawable.answer_correct_background);
        }
    }

    private void highlightWrongAnswer() {
        for (int i = 0; i < llGameArea.getChildCount(); i++) {
            View v = llGameArea.getChildAt(i);
            if (v instanceof Button && v.isSelected()) v.setBackgroundResource(R.drawable.answer_wrong_background);
        }
    }

    private void disableAllAnswers() {
        for (int i = 0; i < llGameArea.getChildCount(); i++) {
            View v = llGameArea.getChildAt(i);
            if (v instanceof Button) v.setEnabled(false);
        }
    }

    private void showGameResult(GameResult res) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // ✅ CHUYỂN SANG ACTIVITY KẾT QUẢ
        Intent intent = new Intent(this, GameResultActivity.class);
        intent.putExtra("yourScore", yourScore);
        intent.putExtra("opponentScore", opponentScore);
        intent.putExtra("matchCode", matchCode);

        // ✅ CLEAR STACK ĐỂ KHÔNG THỂ BACK VỀ MÀN HÌNH CHƠI
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
        finish();
    }

    private void showLoading(String msg) {
        llGameArea.removeAllViews();
        TextView tv = new TextView(this);
        tv.setText(msg);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setPadding(0, 100, 0, 0);
        llGameArea.addView(tv);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}