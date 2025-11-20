package com.example.iq5.feature.multiplayer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.R;
import com.example.iq5.feature.multiplayer.data.QuestionRepository;
import com.example.iq5.feature.multiplayer.model.Question;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class PvPBattleActivity extends AppCompatActivity {

    // Khai báo các Views chính
    private MaterialButton btnSkip;
    private MaterialButton btnEndGame;
    private MaterialButton btnAnswerA, btnAnswerB, btnAnswerC, btnAnswerD;
    private TextView tvQuestion, tvQuestionNumber;

    // KHAI BÁO BỔ SUNG: Điểm số, Timer Views
    private TextView tvPlayerScore, tvOpponentScore;
    private TextView tvTimer;
    private ProgressBar progressTime;

    // Biến trạng thái trò chơi
    private int currentQuestionIndex = 0;
    private final int totalQuestions = 10; // ĐÃ SỬA: Đặt lại thành 10 (theo yêu cầu)
    private String roomCode;

    private List<Question> questionsList;
    private String currentCorrectAnswer;

    // BIẾN TRẠNG THÁI BỔ SUNG
    private int playerScore = 0;
    private int playerCorrectCount = 0; // ĐÃ BỔ SUNG: Theo dõi số câu đúng
    private final int scorePerQuestion = 100;
    private final int MAX_TIME_SECONDS = 15;
    private int currentTimeSeconds = MAX_TIME_SECONDS;

    // Timer logic
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (currentTimeSeconds > 0) {
                currentTimeSeconds--;
                progressTime.setProgress(currentTimeSeconds);

                if (tvTimer != null) {
                    tvTimer.setText(String.valueOf(currentTimeSeconds));
                }

                timerHandler.postDelayed(this, 1000);
            } else {
                Toast.makeText(PvPBattleActivity.this, "Hết giờ!", Toast.LENGTH_SHORT).show();
                handleAnswerSelected("TIMEOUT", null);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pvp_battle);

        roomCode = getIntent().getStringExtra("ROOM_CODE");

        // --- ÁNH XẠ VIEWS ---
        btnSkip = findViewById(R.id.btnSkip);
        btnEndGame = findViewById(R.id.btnEndGame);
        btnAnswerA = findViewById(R.id.btnAnswerA);
        btnAnswerB = findViewById(R.id.btnAnswerB);
        btnAnswerC = findViewById(R.id.btnAnswerC);
        btnAnswerD = findViewById(R.id.btnAnswerD);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);

        // Ánh xạ BỔ SUNG: Điểm số và Timer Views
        tvPlayerScore = findViewById(R.id.layoutPlayerScore).findViewById(R.id.tvPlayerScore);
        tvOpponentScore = findViewById(R.id.layoutOpponentScore).findViewById(R.id.tvPlayerScore);
        progressTime = findViewById(R.id.progressTime);
        tvTimer = findViewById(R.id.tvTimer);

        // Khởi tạo Progress Bar
        progressTime.setMax(MAX_TIME_SECONDS);
        progressTime.setProgress(MAX_TIME_SECONDS);
        tvPlayerScore.setText(String.valueOf(playerScore));
        tvOpponentScore.setText("0");

        // Load và xáo trộn danh sách câu hỏi
        questionsList = QuestionRepository.getAllQuestions();
        if (questionsList.size() < totalQuestions) { /* ... xử lý ... */ }

        // Thiết lập Listeners và Back Callback
        setupActionListeners();
        setupOnBackPressedCallback();

        // Bắt đầu tải câu hỏi đầu tiên
        loadQuestion(currentQuestionIndex);
    }

    // --- LOGIC TIMER CORE ---

    private void startTimer() {
        currentTimeSeconds = MAX_TIME_SECONDS;
        progressTime.setProgress(MAX_TIME_SECONDS);
        if (tvTimer != null) {
            tvTimer.setText(String.valueOf(currentTimeSeconds));
        }
        timerHandler.postDelayed(timerRunnable, 1000);
    }

    private void stopTimer() {
        timerHandler.removeCallbacks(timerRunnable);
    }

    // --- LOGIC XỬ LÝ ĐÁP ÁN ---

    /**
     * Xử lý khi người chơi chọn một đáp án hoặc hết giờ.
     */
    private void handleAnswerSelected(String selectedAnswer, MaterialButton selectedButton) {
        stopTimer();
        setAnswerButtonsEnabled(false);

        if (selectedAnswer.equals(currentCorrectAnswer)) {
            // ĐÚNG: Cộng điểm và tăng số câu đúng
            playerScore += scorePerQuestion;
            playerCorrectCount++; // ĐÃ FIX: Tăng số câu trả lời đúng
            tvPlayerScore.setText(String.valueOf(playerScore));
            Toast.makeText(this, "Chính xác! +" + scorePerQuestion, Toast.LENGTH_SHORT).show();
            if (selectedButton != null) {
                selectedButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorWin, getTheme()));
            }
        } else if (!selectedAnswer.equals("TIMEOUT")) {
            // SAI: Thay đổi màu nút
            Toast.makeText(this, "Sai rồi.", Toast.LENGTH_SHORT).show();
            if (selectedButton != null) {
                selectedButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorLose, getTheme()));
            }
            // Highlight đáp án đúng
            MaterialButton correctButton = findCorrectButton(currentCorrectAnswer);
            if (correctButton != null) {
                correctButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimaryLight, getTheme()));
            }
        } else {
            // Hết giờ
            Toast.makeText(this, "Hết giờ, không có điểm!", Toast.LENGTH_SHORT).show();
            MaterialButton correctButton = findCorrectButton(currentCorrectAnswer);
            if (correctButton != null) {
                correctButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimaryLight, getTheme()));
            }
        }

        new Handler().postDelayed(this::goToNextQuestion, 1500);
    }

    // --- CÁC PHƯƠNG THỨC KHÁC ---

    /**
     * Thiết lập OnBackPressedCallback để xử lý nút Back vật lý/cử chỉ.
     */
    private void setupOnBackPressedCallback() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                showEndGameConfirmationDialog();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    /**
     * Thiết lập Listeners cho các nút điều khiển (Bỏ qua, Kết thúc, Đáp án)
     */
    private void setupActionListeners() {

        btnSkip.setOnClickListener(v -> {
            if (currentQuestionIndex < totalQuestions - 1) {
                Toast.makeText(this, "Chuyển sang Câu hỏi tiếp theo!", Toast.LENGTH_SHORT).show();
                goToNextQuestion();
            } else {
                Toast.makeText(this, "Đã hết câu hỏi!", Toast.LENGTH_SHORT).show();
                finishMatch();
            }
        });

        btnEndGame.setOnClickListener(v -> {
            showEndGameConfirmationDialog();
        });

        // --- LOGIC CHO CÁC NÚT ĐÁP ÁN ---
        btnAnswerA.setOnClickListener(v -> handleAnswerSelected("A", btnAnswerA));
        btnAnswerB.setOnClickListener(v -> handleAnswerSelected("B", btnAnswerB));
        btnAnswerC.setOnClickListener(v -> handleAnswerSelected("C", btnAnswerC));
        btnAnswerD.setOnClickListener(v -> handleAnswerSelected("D", btnAnswerD));
    }

    /**
     * Kích hoạt hoặc vô hiệu hóa các nút đáp án và reset màu về trạng thái ban đầu.
     */
    private void setAnswerButtonsEnabled(boolean enabled) {
        int defaultColorId = R.color.white;

        btnAnswerA.setEnabled(enabled);
        btnAnswerB.setEnabled(enabled);
        btnAnswerC.setEnabled(enabled);
        btnAnswerD.setEnabled(enabled);

        // Đặt màu nền lại thành trắng khi kích hoạt
        btnAnswerA.setBackgroundTintList(getResources().getColorStateList(defaultColorId, getTheme()));
        btnAnswerB.setBackgroundTintList(getResources().getColorStateList(defaultColorId, getTheme()));
        btnAnswerC.setBackgroundTintList(getResources().getColorStateList(defaultColorId, getTheme()));
        btnAnswerD.setBackgroundTintList(getResources().getColorStateList(defaultColorId, getTheme()));
    }


    /**
     * Tải câu hỏi tiếp theo và cập nhật UI.
     */
    private void loadQuestion(int index) {
        setAnswerButtonsEnabled(true);
        startTimer(); // Bắt đầu lại đồng hồ

        if (index >= questionsList.size()) {
            finishMatch();
            return;
        }

        Question question = questionsList.get(index);

        // 1. Cập nhật đáp án đúng hiện tại
        currentCorrectAnswer = question.getCorrectAnswerKey();

        // 2. Cập nhật UI
        tvQuestionNumber.setText("Câu hỏi " + (index + 1) + "/" + totalQuestions);
        tvQuestion.setText(question.getContent());

        List<String> options = question.getOptions();
        btnAnswerA.setText(options.get(0));
        btnAnswerB.setText(options.get(1));
        btnAnswerC.setText(options.get(2));
        btnAnswerD.setText(options.get(3));
    }

    /**
     * Chuyển sang câu hỏi tiếp theo và cập nhật chỉ mục
     */
    private void goToNextQuestion() {
        if (currentQuestionIndex < totalQuestions - 1) {
            currentQuestionIndex++;
            loadQuestion(currentQuestionIndex);
        } else {
            finishMatch();
        }
    }

    /**
     * Hiển thị hộp thoại xác nhận kết thúc trận đấu sớm
     */
    private void showEndGameConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận Kết thúc")
                .setMessage("Bạn có chắc chắn muốn kết thúc trận đấu? Bạn có thể bị xử thua nếu thoát sớm.")
                .setPositiveButton("KẾT THÚC", (dialog, which) -> {
                    finishMatch();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    /**
     * Chuyển sang màn hình Kết quả và dọn dẹp Activity Stack
     */
    private void finishMatch() {
        stopTimer();

        // GIẢ LẬP: Điểm và số câu trả lời đúng của Đối thủ (Sẽ được nhận qua WebSocket/API trong thực tế)
        int opponentFinalScore = 600;
        int opponentCorrectCount = 6;

        Intent intent = new Intent(PvPBattleActivity.this, CompareResultActivity.class);

        // ĐÃ FIX: Đính kèm tất cả dữ liệu thống kê cần thiết
        intent.putExtra("PLAYER_SCORE", playerScore);
        intent.putExtra("PLAYER_CORRECT_COUNT", playerCorrectCount);
        intent.putExtra("OPPONENT_SCORE", opponentFinalScore);
        intent.putExtra("OPPONENT_CORRECT_COUNT", opponentCorrectCount);
        intent.putExtra("TOTAL_QUESTIONS", totalQuestions);

        // Đóng Activity hiện tại
        finish();

        // Chuyển sang màn hình kết quả
        startActivity(intent);
    }

    /**
     * Hàm hỗ trợ tìm nút đáp án đúng.
     */
    private MaterialButton findCorrectButton(String answer) {
        if ("A".equals(answer)) return btnAnswerA;
        if ("B".equals(answer)) return btnAnswerB;
        if ("C".equals(answer)) return btnAnswerC;
        if ("D".equals(answer)) return btnAnswerD;
        return null;
    }
}