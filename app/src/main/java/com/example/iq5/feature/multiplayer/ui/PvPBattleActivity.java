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

    private MaterialButton btnSkip;
    private MaterialButton btnEndGame;
    private MaterialButton btnAnswerA, btnAnswerB, btnAnswerC, btnAnswerD;

    private TextView tvQuestion, tvQuestionNumber;
    private TextView tvPlayerScore, tvOpponentScore;
    private TextView tvTimer;
    private ProgressBar progressTime;

    private int currentQuestionIndex = 0;
    private final int totalQuestions = 10;
    private String roomCode;

    private List<Question> questionsList;
    private String currentCorrectAnswer;

    private int playerScore = 0;
    private int playerCorrectCount = 0;
    private final int scorePerQuestion = 100;

    private final int MAX_TIME_SECONDS = 15;
    private int currentTimeSeconds = MAX_TIME_SECONDS;

    private Handler timerHandler;

    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (currentTimeSeconds > 0) {
                currentTimeSeconds--;
                progressTime.setProgress(currentTimeSeconds);
                tvTimer.setText(String.valueOf(currentTimeSeconds));

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

        timerHandler = new Handler(getMainLooper());

        roomCode = getIntent().getStringExtra("ROOM_CODE");

        mapViews();
        initScoreAndTimerUi();

        questionsList = QuestionRepository.getAllQuestions();
        if (questionsList == null || questionsList.isEmpty()) {
            Toast.makeText(this, "Không có câu hỏi nào.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (questionsList.size() < totalQuestions) {
            Toast.makeText(this,
                    "Chỉ có " + questionsList.size() + " câu hỏi. Sẽ dùng số này.",
                    Toast.LENGTH_SHORT).show();
        }

        setupActionListeners();
        setupOnBackPressedCallback();

        loadQuestion(currentQuestionIndex);
    }

    private void mapViews() {
        btnSkip = findViewById(R.id.btnSkip);
        btnEndGame = findViewById(R.id.btnEndGame);
        btnAnswerA = findViewById(R.id.btnAnswerA);
        btnAnswerB = findViewById(R.id.btnAnswerB);
        btnAnswerC = findViewById(R.id.btnAnswerC);
        btnAnswerD = findViewById(R.id.btnAnswerD);

        tvQuestion = findViewById(R.id.tvQuestion);
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);

        tvPlayerScore = findViewById(R.id.layoutPlayerScore).findViewById(R.id.tvPlayerScore);
        tvOpponentScore = findViewById(R.id.layoutOpponentScore).findViewById(R.id.tvPlayerScore);

        progressTime = findViewById(R.id.progressTime);
        tvTimer = findViewById(R.id.tvTimer);
    }

    private void initScoreAndTimerUi() {
        progressTime.setMax(MAX_TIME_SECONDS);
        progressTime.setProgress(MAX_TIME_SECONDS);
        tvTimer.setText(String.valueOf(MAX_TIME_SECONDS));

        tvPlayerScore.setText(String.valueOf(playerScore));
        tvOpponentScore.setText("0");
    }

    private void startTimer() {
        currentTimeSeconds = MAX_TIME_SECONDS;
        progressTime.setProgress(MAX_TIME_SECONDS);
        tvTimer.setText(String.valueOf(currentTimeSeconds));

        timerHandler.removeCallbacks(timerRunnable);
        timerHandler.postDelayed(timerRunnable, 1000);
    }

    private void stopTimer() {
        timerHandler.removeCallbacks(timerRunnable);
    }

    private void setupOnBackPressedCallback() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showEndGameConfirmationDialog();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void setupActionListeners() {
        btnSkip.setOnClickListener(v -> {
            if (currentQuestionIndex < totalQuestions - 1 &&
                    currentQuestionIndex < questionsList.size() - 1) {
                Toast.makeText(this, "Chuyển sang câu hỏi tiếp theo!", Toast.LENGTH_SHORT).show();
                goToNextQuestion();
            } else {
                Toast.makeText(this, "Đã hết câu hỏi!", Toast.LENGTH_SHORT).show();
                finishMatch();
            }
        });

        btnEndGame.setOnClickListener(v -> showEndGameConfirmationDialog());

        btnAnswerA.setOnClickListener(v -> handleAnswerSelected("A", btnAnswerA));
        btnAnswerB.setOnClickListener(v -> handleAnswerSelected("B", btnAnswerB));
        btnAnswerC.setOnClickListener(v -> handleAnswerSelected("C", btnAnswerC));
        btnAnswerD.setOnClickListener(v -> handleAnswerSelected("D", btnAnswerD));
    }

    private void handleAnswerSelected(String selectedAnswer, MaterialButton selectedButton) {
        stopTimer();
        setAnswerButtonsEnabled(false);

        if (selectedAnswer.equals(currentCorrectAnswer)) {
            playerScore += scorePerQuestion;
            playerCorrectCount++;
            tvPlayerScore.setText(String.valueOf(playerScore));
            Toast.makeText(this, "Chính xác! +" + scorePerQuestion, Toast.LENGTH_SHORT).show();

            if (selectedButton != null) {
                selectedButton.setBackgroundTintList(
                        getResources().getColorStateList(R.color.colorWin, getTheme())
                );
            }

        } else if (!selectedAnswer.equals("TIMEOUT")) {
            Toast.makeText(this, "Sai rồi.", Toast.LENGTH_SHORT).show();
            if (selectedButton != null) {
                selectedButton.setBackgroundTintList(
                        getResources().getColorStateList(R.color.colorLose, getTheme())
                );
            }
            MaterialButton correctButton = findCorrectButton(currentCorrectAnswer);
            if (correctButton != null) {
                correctButton.setBackgroundTintList(
                        getResources().getColorStateList(R.color.colorPrimaryLight, getTheme())
                );
            }

        } else {
            Toast.makeText(this, "Hết giờ, không có điểm!", Toast.LENGTH_SHORT).show();
            MaterialButton correctButton = findCorrectButton(currentCorrectAnswer);
            if (correctButton != null) {
                correctButton.setBackgroundTintList(
                        getResources().getColorStateList(R.color.colorPrimaryLight, getTheme())
                );
            }
        }

        new Handler(getMainLooper()).postDelayed(this::goToNextQuestion, 1500);
    }

    private void setAnswerButtonsEnabled(boolean enabled) {
        int defaultColorId = R.color.white;

        btnAnswerA.setEnabled(enabled);
        btnAnswerB.setEnabled(enabled);
        btnAnswerC.setEnabled(enabled);
        btnAnswerD.setEnabled(enabled);

        btnAnswerA.setBackgroundTintList(
                getResources().getColorStateList(defaultColorId, getTheme()));
        btnAnswerB.setBackgroundTintList(
                getResources().getColorStateList(defaultColorId, getTheme()));
        btnAnswerC.setBackgroundTintList(
                getResources().getColorStateList(defaultColorId, getTheme()));
        btnAnswerD.setBackgroundTintList(
                getResources().getColorStateList(defaultColorId, getTheme()));
    }

    private void loadQuestion(int index) {
        if (index >= questionsList.size() || index >= totalQuestions) {
            finishMatch();
            return;
        }

        setAnswerButtonsEnabled(true);
        startTimer();

        Question question = questionsList.get(index);
        currentCorrectAnswer = question.getCorrectAnswerKey();

        tvQuestionNumber.setText("Câu hỏi " + (index + 1) + "/" +
                Math.min(totalQuestions, questionsList.size()));
        tvQuestion.setText(question.getContent());

        List<String> options = question.getOptions();
        if (options != null && options.size() >= 4) {
            btnAnswerA.setText(options.get(0));
            btnAnswerB.setText(options.get(1));
            btnAnswerC.setText(options.get(2));
            btnAnswerD.setText(options.get(3));
        }
    }

    private void goToNextQuestion() {
        if (currentQuestionIndex < totalQuestions - 1 &&
                currentQuestionIndex < questionsList.size() - 1) {
            currentQuestionIndex++;
            loadQuestion(currentQuestionIndex);
        } else {
            finishMatch();
        }
    }

    private void showEndGameConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận kết thúc")
                .setMessage("Bạn có chắc muốn kết thúc trận đấu? Bạn có thể bị xử thua nếu thoát sớm.")
                .setPositiveButton("KẾT THÚC", (dialog, which) -> finishMatch())
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void finishMatch() {
        stopTimer();

        int opponentFinalScore = 600;
        int opponentCorrectCount = 6;

        Intent intent = new Intent(PvPBattleActivity.this, CompareResultActivity.class);
        intent.putExtra("PLAYER_SCORE", playerScore);
        intent.putExtra("PLAYER_CORRECT_COUNT", playerCorrectCount);
        intent.putExtra("OPPONENT_SCORE", opponentFinalScore);
        intent.putExtra("OPPONENT_CORRECT_COUNT", opponentCorrectCount);
        intent.putExtra("TOTAL_QUESTIONS", totalQuestions);

        startActivity(intent);
        finish();
    }

    private MaterialButton findCorrectButton(String answer) {
        if ("A".equals(answer)) return btnAnswerA;
        if ("B".equals(answer)) return btnAnswerB;
        if ("C".equals(answer)) return btnAnswerC;
        if ("D".equals(answer)) return btnAnswerD;
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }
}
