package com.example.iq5.feature.quiz.ui;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.core.navigation.NavigationHelper;
import com.example.iq5.core.network.QuizResultApiService;
import com.example.iq5.data.model.AnswerSubmit;
import com.example.iq5.data.model.GameStartOptions;
import com.example.iq5.data.model.Question;
import com.example.iq5.data.repository.QuizApiRepository;
import com.example.iq5.data.repository.UserProfileApiRepository;
import com.example.iq5.feature.quiz.adapter.AnswerOptionAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * QuizActivity sử dụng API thật từ backend
 */
public class ApiQuizActivity extends AppCompatActivity {

    private static final String TAG = "ApiQuizActivity";

    // ===== SCORE CONFIG =====
    private static final int POINTS_PER_CORRECT = 100;

    // ===== TIMER CONFIG =====
    private static final long QUESTION_TIME_MS = 15_000; // 15s / câu
    private CountDownTimer countDownTimer;

    // UI Components
    private TextView txtQuestion, txtQuestionNumber, txtScore, txtTimer;
    private RecyclerView rvOptions;
    private ImageButton btnLifelineHint;
    private Button btnNext, btnFinish;
    private ProgressBar progressBar;

    // Data & Logic
    private QuizApiRepository quizRepository;
    private UserProfileApiRepository userProfileRepository;
    private Question currentQuestion;

    // Lưu những câu đã trả lời của lượt chơi hiện tại (dùng để review ngay sau khi finish)
    private final List<Question> answeredQuestions = new ArrayList<>();

    private List<Question> preloadedQuestions = new ArrayList<>();
    private int currentAttemptId = -1;
    private int currentQuestionIndex = 0;
    private boolean isQuizStarted = false;
    private boolean usingPreloadedQuestions = false;

    // Adapter
    private AnswerOptionAdapter optionAdapter;

    // ===== SCORE STATE =====
    private int totalScore = 0;
    private int correctCount = 0;
    private boolean answeredThisQuestion = false; // chống chấm nhiều lần/câu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        initViews();
        initRepository();
        startQuizFromIntent();
    }

    private void initViews() {
        txtQuestion = findViewById(R.id.txtQuestion);
        // txtQuestionNumber = findViewById(R.id.txtQuestionNumber); // nếu layout không có thì để null
        txtScore = findViewById(R.id.txtScore);
        txtTimer = findViewById(R.id.txtTimer);

        rvOptions = findViewById(R.id.recyclerOptions);
        btnLifelineHint = findViewById(R.id.btnLifelineHint);
        // btnNext = findViewById(R.id.btnNext); // nếu layout không có thì để null
        btnFinish = findViewById(R.id.btnFinish);
        progressBar = findViewById(R.id.progressBar);

        if (rvOptions != null) {
            rvOptions.setLayoutManager(new GridLayoutManager(this, 2));
        }

        if (txtScore != null) txtScore.setText(String.valueOf(totalScore));
        if (txtTimer != null) txtTimer.setText((QUESTION_TIME_MS / 1000) + "s");

        setupButtons();
    }

    private void initRepository() {
        quizRepository = new QuizApiRepository(this);
        userProfileRepository = new UserProfileApiRepository(this);
    }

    private void startQuizFromIntent() {
        Intent intent = getIntent();

        boolean hasQuestions = intent.getBooleanExtra("has_questions", false);
        String questionsJson = intent.getStringExtra("questions_json");
        String categoryName = intent.getStringExtra("category_name");

        if (hasQuestions && questionsJson != null) {
            startQuizWithPreloadedQuestions(questionsJson, categoryName);
        } else {
            String difficulty = intent.getStringExtra("difficulty");
            String category = intent.getStringExtra("category");
            int questionCount = intent.getIntExtra("questionCount", 10);

            GameStartOptions options = new GameStartOptions();
            options.setDifficulty(difficulty != null ? difficulty : "easy");
            options.setCategory(category != null ? category : "general");
            options.setQuestionCount(questionCount);

            startQuiz(options);
        }
    }

    private void startQuizWithPreloadedQuestions(String questionsJson, String categoryName) {
        try {
            com.google.gson.Gson gson = new com.google.gson.Gson();
            com.google.gson.reflect.TypeToken<List<com.example.iq5.core.network.QuizApiService.TestQuestionModel>> typeToken =
                    new com.google.gson.reflect.TypeToken<List<com.example.iq5.core.network.QuizApiService.TestQuestionModel>>() {};
            List<com.example.iq5.core.network.QuizApiService.TestQuestionModel> testQuestions =
                    gson.fromJson(questionsJson, typeToken.getType());

            if (testQuestions != null && !testQuestions.isEmpty()) {
                List<Question> questions = convertTestQuestionsToQuestions(testQuestions);
                startQuizWithQuestions(questions, categoryName);
                Toast.makeText(this, "Bắt đầu quiz: " + categoryName + " (" + questions.size() + " câu)", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Không có câu hỏi để hiển thị", Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi xử lý câu hỏi: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private List<Question> convertTestQuestionsToQuestions(List<com.example.iq5.core.network.QuizApiService.TestQuestionModel> testQuestions) {
        List<Question> questions = new ArrayList<>();

        for (com.example.iq5.core.network.QuizApiService.TestQuestionModel testQ : testQuestions) {
            Question q = new Question();
            q.setId(testQ.getId());
            q.setQuestion_text(testQ.getQuestion());
            q.setOption_a(testQ.getOptionA());
            q.setOption_b(testQ.getOptionB());
            q.setOption_c(testQ.getOptionC());
            q.setOption_d(testQ.getOptionD());
            q.setCorrect_answer(testQ.getCorrectAnswer());
            q.setDifficulty(testQ.getDifficulty());
            q.setCategory(testQ.getCategoryName());

            q.createOptionsFromIndividual();
            questions.add(q);
        }

        return questions;
    }

    private void startQuizWithQuestions(List<Question> questions, String categoryName) {
        this.preloadedQuestions = questions;
        this.currentQuestionIndex = 0;
        this.isQuizStarted = true;
        this.usingPreloadedQuestions = true;

        if (!questions.isEmpty()) {
            displayQuestion(questions.get(0));
            updateQuestionProgress();
        }
    }

    private void updateQuestionProgress() {
        if (usingPreloadedQuestions && !preloadedQuestions.isEmpty()) {
            int total = preloadedQuestions.size();
            int current = currentQuestionIndex + 1;

            if (txtQuestionNumber != null) {
                txtQuestionNumber.setText("Câu " + current + "/" + total);
            }

            if (progressBar != null) {
                int progress = (int) ((float) current / total * 100);
                progressBar.setProgress(progress);
            }
        }
    }

    private void moveToNextQuestion() {
        stopQuestionTimer();

        if (usingPreloadedQuestions) {
            currentQuestionIndex++;

            if (currentQuestionIndex < preloadedQuestions.size()) {
                displayQuestion(preloadedQuestions.get(currentQuestionIndex));
                updateQuestionProgress();
            } else {
                showQuizResult();
            }
        } else {
            loadNextQuestionFromApi();
        }
    }

    private void showQuizResult() {
        stopQuestionTimer();

        int totalQuestions = usingPreloadedQuestions ? preloadedQuestions.size() : answeredQuestions.size();
        int correctAnswers = correctCount;
        double scorePercent = totalQuestions > 0 ? (double) correctAnswers / totalQuestions * 100 : 0;

        Log.d(TAG, "FINAL: correct=" + correctAnswers + " total=" + totalQuestions + " percent=" + scorePercent + " points=" + totalScore);

        Bundle resultData = new Bundle();
        resultData.putInt("correct_answers", correctAnswers);
        resultData.putInt("total_questions", totalQuestions);
        resultData.putDouble("score", scorePercent);
        resultData.putInt("points", totalScore);
        resultData.putString("category", currentQuestion != null ? currentQuestion.getCategory() : "Unknown");

        // ✅ GỬI CÂU SAI CỦA LƯỢT VỪA CHƠI
        resultData.putSerializable("wrong_questions", buildWrongQuestionsForReview());

        updateUserStats(correctAnswers, totalQuestions, scorePercent,
                currentQuestion != null ? currentQuestion.getCategory() : "Unknown");

        NavigationHelper.navigateToResult(this, resultData);
        finish();
    }

    private void startQuiz(GameStartOptions options) {
        showLoading(true);

        quizRepository.startQuiz(options, new QuizApiRepository.QuizStartCallback() {
            @Override
            public void onSuccess(int attemptId, Question firstQuestion) {
                runOnUiThread(() -> {
                    showLoading(false);
                    currentAttemptId = attemptId;
                    isQuizStarted = true;

                    displayQuestion(firstQuestion);
                    Toast.makeText(ApiQuizActivity.this, "Bắt đầu quiz thành công!", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    showLoading(false);
                    Toast.makeText(ApiQuizActivity.this, error, Toast.LENGTH_LONG).show();
                    finish();
                });
            }
        });
    }

    private void displayQuestion(Question question) {
        currentQuestion = question;
        answeredThisQuestion = false;

        startQuestionTimer();

        if (txtQuestion != null) {
            txtQuestion.setText(question.getQuestion_text());
        }

        if (question.getOptions() != null && rvOptions != null) {
            optionAdapter = new AnswerOptionAdapter(
                    convertToOptions(question.getOptions()),
                    option -> onUserSelectOption(option)
            );
            rvOptions.setAdapter(optionAdapter);
        }

        updateButtonVisibility();
    }

    private List<com.example.iq5.feature.quiz.model.Option> convertToOptions(List<Question.Option> apiOptions) {
        List<com.example.iq5.feature.quiz.model.Option> options = new ArrayList<>();

        for (Question.Option apiOption : apiOptions) {
            com.example.iq5.feature.quiz.model.Option option = new com.example.iq5.feature.quiz.model.Option();
            option.setOption_id(apiOption.getOptionId());
            option.setOption_text(apiOption.getOptionText());
            options.add(option);
        }

        return options;
    }

    private void setupButtons() {
        if (btnFinish != null) {
            btnFinish.setOnClickListener(v -> {
                stopQuestionTimer();
                if (usingPreloadedQuestions) showQuizResult();
                else finishQuizWithApi();
            });
        }

        if (btnNext != null) {
            btnNext.setOnClickListener(v -> moveToNextQuestion());
        }

        if (btnLifelineHint != null) {
            btnLifelineHint.setOnClickListener(v -> showHint());
        }
    }

    private void updateButtonVisibility() {
        if (btnNext != null) btnNext.setVisibility(View.VISIBLE);
        if (btnFinish != null) btnFinish.setVisibility(View.VISIBLE);
    }

    private void showLoading(boolean show) {
        if (progressBar != null) progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        if (btnNext != null) btnNext.setEnabled(!show);
        if (btnFinish != null) btnFinish.setEnabled(!show);
    }

    private void onUserSelectOption(com.example.iq5.feature.quiz.model.Option option) {
        if (currentQuestion == null) return;
        if (answeredThisQuestion) return;
        answeredThisQuestion = true;

        stopQuestionTimer();

        // nếu adapter bạn có lockSelection thì giữ, không có thì bỏ
        if (optionAdapter != null) {
            try { optionAdapter.lockSelection(); } catch (Exception ignored) {}
        }

        if (usingPreloadedQuestions) {
            String selectedAnswer = option.getOption_id();
            currentQuestion.setUser_selected_answer_id(selectedAnswer);

            boolean isCorrect = selectedAnswer != null && selectedAnswer.equals(currentQuestion.getCorrect_answer());
            currentQuestion.setAnsweredCorrectly(isCorrect);

            Question answeredQuestion = createQuestionCopy(currentQuestion);
            answeredQuestion.setUser_selected_answer_id(selectedAnswer);
            answeredQuestion.setAnsweredCorrectly(isCorrect);
            answeredQuestions.add(answeredQuestion);

            if (isCorrect) {
                int old = totalScore;
                totalScore += POINTS_PER_CORRECT;
                correctCount++;
                animateScore(old, totalScore);
                Toast.makeText(this, "ĐÚNG! +" + POINTS_PER_CORRECT, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "SAI! Đáp án đúng: " + currentQuestion.getCorrect_answer(), Toast.LENGTH_SHORT).show();
            }

            new android.os.Handler().postDelayed(this::moveToNextQuestion, 700);

        } else {
            currentQuestion.setUser_selected_answer_id(option.getOption_id());
            submitCurrentAnswer();
        }

        if (optionAdapter != null) optionAdapter.notifyDataSetChanged();
    }

    private Question createQuestionCopy(Question original) {
        Question copy = new Question();
        copy.setId(original.getId());
        copy.setQuestion_text(original.getQuestion_text());
        copy.setOption_a(original.getOption_a());
        copy.setOption_b(original.getOption_b());
        copy.setOption_c(original.getOption_c());
        copy.setOption_d(original.getOption_d());
        copy.setCorrect_answer(original.getCorrect_answer());
        copy.setCategory(original.getCategory());
        copy.setDifficulty(original.getDifficulty());
        copy.createOptionsFromIndividual();
        return copy;
    }

    private void loadNextQuestionFromApi() {
        if (currentAttemptId == -1) return;

        showLoading(true);

        quizRepository.getNextQuestion(currentAttemptId, new QuizApiRepository.NextQuestionCallback() {
            @Override
            public void onSuccess(Question question) {
                runOnUiThread(() -> {
                    showLoading(false);
                    displayQuestion(question);
                });
            }

            @Override
            public void onNoMoreQuestions() {
                runOnUiThread(() -> {
                    showLoading(false);
                    finishQuizWithApi();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    showLoading(false);
                    Toast.makeText(ApiQuizActivity.this, "Lỗi tải câu hỏi: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void finishQuizWithApi() {
        if (currentAttemptId == -1) return;

        stopQuestionTimer();
        showLoading(true);

        quizRepository.endQuiz(currentAttemptId, new QuizApiRepository.QuizEndCallback() {
            @Override
            public void onSuccess(com.example.iq5.core.network.QuizApiService.QuizResult result) {
                runOnUiThread(() -> {
                    showLoading(false);

                    // percent theo backend nếu có
                    double scorePercent = result.getDiem();

                    Bundle resultData = new Bundle();
                    resultData.putInt("correct_answers", result.getSoCauDung());
                    resultData.putInt("total_questions", result.getTongCauHoi());
                    resultData.putDouble("score", scorePercent);
                    resultData.putInt("points", totalScore);
                    resultData.putString("category", currentQuestion != null ? currentQuestion.getCategory() : "Unknown");

                    // ✅ GỬI CÂU SAI CỦA LƯỢT VỪA CHƠI
                    resultData.putSerializable("wrong_questions", buildWrongQuestionsForReview());

                    NavigationHelper.navigateToResult(ApiQuizActivity.this, resultData);
                    finish();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    showLoading(false);
                    Toast.makeText(ApiQuizActivity.this, "Lỗi kết thúc quiz: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void showHint() {
        if (currentQuestion != null && currentQuestion.getExplanation() != null) {
            Toast.makeText(this, currentQuestion.getExplanation(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Không có gợi ý cho câu hỏi này", Toast.LENGTH_SHORT).show();
        }
    }

    private void submitCurrentAnswer() {
        if (currentQuestion == null || currentAttemptId == -1) return;

        showLoading(true);

        AnswerSubmit answerSubmit = new AnswerSubmit();
        answerSubmit.setAttemptId(currentAttemptId);
        answerSubmit.setQuestionId(currentQuestion.getQuestion_id());
        answerSubmit.setSelectedAnswerId(currentQuestion.getUser_selected_answer_id());

        quizRepository.submitAnswer(answerSubmit, new QuizApiRepository.AnswerSubmitCallback() {
            @Override
            public void onSuccess(boolean isCorrect, String message) {
                runOnUiThread(() -> {
                    showLoading(false);

                    // ✅ cộng điểm
                    if (isCorrect) {
                        int old = totalScore;
                        totalScore += POINTS_PER_CORRECT;
                        correctCount++;
                        animateScore(old, totalScore);
                    }

                    Toast.makeText(ApiQuizActivity.this, isCorrect ? ("ĐÚNG! +" + POINTS_PER_CORRECT) : "SAI!", Toast.LENGTH_SHORT).show();

                    currentQuestion.setAnsweredCorrectly(isCorrect);
                    answeredQuestions.add(currentQuestion);

                    if (btnNext != null) btnNext.setEnabled(true);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    showLoading(false);
                    Toast.makeText(ApiQuizActivity.this, "Lỗi nộp đáp án: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    // ===== SCORE ANIMATION =====
    private void animateScore(int from, int to) {
        if (txtScore == null) return;

        ValueAnimator animator = ValueAnimator.ofInt(from, to);
        animator.setDuration(250);
        animator.addUpdateListener(a -> txtScore.setText(String.valueOf((int) a.getAnimatedValue())));
        animator.start();
    }

    // =========================
    // TIMER IMPLEMENTATION
    // =========================
    private void startQuestionTimer() {
        stopQuestionTimer();

        if (txtTimer != null) txtTimer.setText((QUESTION_TIME_MS / 1000) + "s");

        countDownTimer = new CountDownTimer(QUESTION_TIME_MS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (txtTimer != null) {
                    long sec = millisUntilFinished / 1000;
                    txtTimer.setText(sec + "s");
                }
            }

            @Override
            public void onFinish() {
                if (txtTimer != null) txtTimer.setText("0s");
                onTimeUp();
            }
        }.start();
    }

    private void stopQuestionTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    private void onTimeUp() {
        if (answeredThisQuestion) return;
        answeredThisQuestion = true;

        if (optionAdapter != null) {
            try { optionAdapter.lockSelection(); } catch (Exception ignored) {}
        }

        Toast.makeText(this, "Hết giờ!", Toast.LENGTH_SHORT).show();

        // Lưu là sai để review/đếm không lệch
        if (currentQuestion != null) {
            currentQuestion.setUser_selected_answer_id(null);
            currentQuestion.setAnsweredCorrectly(false);

            Question answered = createQuestionCopy(currentQuestion);
            answered.setUser_selected_answer_id(null);
            answered.setAnsweredCorrectly(false);
            answeredQuestions.add(answered);
        }

        new android.os.Handler().postDelayed(this::moveToNextQuestion, 500);
    }

    @Override
    protected void onDestroy() {
        stopQuestionTimer();
        super.onDestroy();
    }

    // =========================
    // BUILD WRONG QUESTIONS FOR REVIEW
    // (convert data.model.Question -> feature.quiz.model.Question)
    // =========================
    private com.example.iq5.feature.quiz.model.Question toReviewQuestion(Question q) {
        com.example.iq5.feature.quiz.model.Question rq = new com.example.iq5.feature.quiz.model.Question();
        rq.setQuestion_text(q.getQuestion_text());

        // correct_answer ở data.model.Question là "A/B/C/D"
        rq.setCorrect_answer_id(q.getCorrect_answer());
        rq.setUser_selected_answer_id(q.getUser_selected_answer_id());

        ArrayList<com.example.iq5.feature.quiz.model.Option> ops = new ArrayList<>();

        com.example.iq5.feature.quiz.model.Option oa = new com.example.iq5.feature.quiz.model.Option();
        oa.setOption_id("A");
        oa.setOption_text(q.getOption_a());
        ops.add(oa);

        com.example.iq5.feature.quiz.model.Option ob = new com.example.iq5.feature.quiz.model.Option();
        ob.setOption_id("B");
        ob.setOption_text(q.getOption_b());
        ops.add(ob);

        com.example.iq5.feature.quiz.model.Option oc = new com.example.iq5.feature.quiz.model.Option();
        oc.setOption_id("C");
        oc.setOption_text(q.getOption_c());
        ops.add(oc);

        com.example.iq5.feature.quiz.model.Option od = new com.example.iq5.feature.quiz.model.Option();
        od.setOption_id("D");
        od.setOption_text(q.getOption_d());
        ops.add(od);

        rq.setOptions(ops);
        return rq;
    }

    private ArrayList<com.example.iq5.feature.quiz.model.Question> buildWrongQuestionsForReview() {
        ArrayList<com.example.iq5.feature.quiz.model.Question> wrong = new ArrayList<>();

        for (Question q : answeredQuestions) {
            if (q == null) continue;

            String user = q.getUser_selected_answer_id(); // "A/B/C/D" hoặc null
            String correct = q.getCorrect_answer();       // "A/B/C/D"

            boolean isCorrect = (user != null && user.equals(correct));
            if (!isCorrect) wrong.add(toReviewQuestion(q));
        }
        return wrong;
    }

    // =========================
    // USER STATS (giữ nguyên phần bạn đang có)
    // =========================
    private void updateUserStats(int correctAnswers, int totalQuestions, double score, String category) {
        Log.d(TAG, "Updating user stats...");
        updateLocalStats(correctAnswers, totalQuestions, (int) score);
        submitQuizResult(correctAnswers, totalQuestions, category);

        userProfileRepository.updateQuizStats(correctAnswers, totalQuestions, score, category,
                new UserProfileApiRepository.UpdateCallback() {
                    @Override
                    public void onSuccess(String message) { Log.d(TAG, "Stats updated: " + message); }
                    @Override
                    public void onUnauthorized() { Log.e(TAG, "Unauthorized"); }
                    @Override
                    public void onError(String error) { Log.e(TAG, "Update error: " + error); }
                });
    }

    private void updateLocalStats(int correctAnswers, int totalQuestions, int score) {
        android.content.SharedPreferences prefs = getSharedPreferences("quiz_stats", MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();

        int currentQuizzes = prefs.getInt("total_quizzes", 0);
        int currentCorrect = prefs.getInt("total_correct", 0);
        int currentTotalScore = prefs.getInt("total_score", 0);
        int currentPerfectScores = prefs.getInt("perfect_scores", 0);

        editor.putInt("total_quizzes", currentQuizzes + 1);
        editor.putInt("total_correct", currentCorrect + correctAnswers);
        editor.putInt("total_score", currentTotalScore + score);
        editor.putLong("last_play_date", System.currentTimeMillis());

        if (score >= 100) editor.putInt("perfect_scores", currentPerfectScores + 1);

        saveWrongQuestions();
        editor.apply();
    }

    private void saveWrongQuestions() {
        try {
            android.content.SharedPreferences wrongPrefs = getSharedPreferences("wrong_questions", MODE_PRIVATE);
            android.content.SharedPreferences.Editor editor = wrongPrefs.edit();

            com.google.gson.Gson gson = new com.google.gson.Gson();
            List<WrongQuestionData> wrongQuestions = new ArrayList<>();

            for (Question question : answeredQuestions) {
                if (!question.isAnsweredCorrectly()) {
                    WrongQuestionData wrongData = new WrongQuestionData();
                    wrongData.questionText = question.getQuestion_text();
                    wrongData.userAnswer = question.getUser_selected_answer_id();
                    wrongData.correctAnswer = question.getCorrect_answer();
                    wrongData.optionA = question.getOption_a();
                    wrongData.optionB = question.getOption_b();
                    wrongData.optionC = question.getOption_c();
                    wrongData.optionD = question.getOption_d();
                    wrongData.category = question.getCategory();
                    wrongData.timestamp = System.currentTimeMillis();
                    wrongQuestions.add(wrongData);
                }
            }

            if (!wrongQuestions.isEmpty()) {
                String existingJson = wrongPrefs.getString("wrong_questions_list", "[]");
                java.lang.reflect.Type listType = new com.google.gson.reflect.TypeToken<List<WrongQuestionData>>() {}.getType();
                List<WrongQuestionData> existingQuestions = gson.fromJson(existingJson, listType);

                if (existingQuestions == null) existingQuestions = new ArrayList<>();
                existingQuestions.addAll(wrongQuestions);

                if (existingQuestions.size() > 50) {
                    existingQuestions = existingQuestions.subList(existingQuestions.size() - 50, existingQuestions.size());
                }

                editor.putString("wrong_questions_list", gson.toJson(existingQuestions));
                editor.putInt("wrong_questions_count", wrongQuestions.size());
                editor.apply();
            }
        } catch (Exception e) {
            Log.e(TAG, "saveWrongQuestions error: " + e.getMessage());
        }
    }

    public static class WrongQuestionData {
        public String questionText;
        public String userAnswer;
        public String correctAnswer;
        public String optionA;
        public String optionB;
        public String optionC;
        public String optionD;
        public String category;
        public long timestamp;
    }

    private void submitQuizResult(int correctAnswers, int totalQuestions, String category) {
        Log.d(TAG, "Submitting quiz result...");
        try {
            QuizResultApiService.SubmitQuizResultRequest request =
                    new QuizResultApiService.SubmitQuizResultRequest(totalQuestions, correctAnswers, 1, 1);

            com.example.iq5.core.prefs.PrefsManager prefsManager = new com.example.iq5.core.prefs.PrefsManager(this);
            retrofit2.Retrofit retrofit = com.example.iq5.core.network.ApiClient.getClient(prefsManager);
            QuizResultApiService service = retrofit.create(QuizResultApiService.class);

            retrofit2.Call<QuizResultApiService.QuizResultResponse> call = service.submitResult(request);
            call.enqueue(new retrofit2.Callback<QuizResultApiService.QuizResultResponse>() {
                @Override
                public void onResponse(retrofit2.Call<QuizResultApiService.QuizResultResponse> call,
                                       retrofit2.Response<QuizResultApiService.QuizResultResponse> response) { }
                @Override
                public void onFailure(retrofit2.Call<QuizResultApiService.QuizResultResponse> call, Throwable t) { }
            });
        } catch (Exception e) {
            Log.e(TAG, "submitQuizResult error: " + e.getMessage());
        }
    }
}
