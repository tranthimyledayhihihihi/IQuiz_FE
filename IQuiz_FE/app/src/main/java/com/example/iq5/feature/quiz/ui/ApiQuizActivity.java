package com.example.iq5.feature.quiz.ui;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.iq5.data.model.AnswerSubmit;
import com.example.iq5.data.model.GameStartOptions;
import com.example.iq5.data.model.Question;
import com.example.iq5.data.repository.QuizApiRepository;
import com.example.iq5.data.repository.UserProfileApiRepository;
import com.example.iq5.feature.quiz.adapter.AnswerOptionAdapter;
import com.example.iq5.feature.quiz.model.Option;

import java.util.ArrayList;
import java.util.List;

/**
 * QuizActivity sử dụng API thật từ backend
 */
public class ApiQuizActivity extends AppCompatActivity {

    private static final String TAG = "ApiQuizActivity";
    
    // UI Components
    private TextView txtQuestion, txtQuestionNumber;
    private RecyclerView rvOptions;
    private ImageButton btnLifelineHint;
    private Button btnNext, btnFinish;
    private ProgressBar progressBar;
    
    // Data & Logic
    private QuizApiRepository quizRepository;
    private UserProfileApiRepository userProfileRepository;
    private Question currentQuestion;
    private List<Question> answeredQuestions = new ArrayList<>();
    private List<Question> preloadedQuestions = new ArrayList<>(); // Câu hỏi đã tải sẵn
    private int currentAttemptId = -1;
    private int currentQuestionIndex = 0;
    private boolean isQuizStarted = false;
    private boolean usingPreloadedQuestions = false; // Flag để biết đang dùng câu hỏi sẵn hay API
    
    // Adapter
    private AnswerOptionAdapter optionAdapter;
    
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
        // txtQuestionNumber = findViewById(R.id.txtQuestionNumber); // Comment out if not in layout
        rvOptions = findViewById(R.id.recyclerOptions);
        btnLifelineHint = findViewById(R.id.btnLifelineHint);
        // btnNext = findViewById(R.id.btnNext); // Comment out if not in layout
        btnFinish = findViewById(R.id.btnFinish);
        progressBar = findViewById(R.id.progressBar);
        
        // Setup RecyclerView
        if (rvOptions != null) {
            rvOptions.setLayoutManager(new GridLayoutManager(this, 2));
        }
        
        // Setup buttons
        setupButtons();
    }
    
    private void initRepository() {
        quizRepository = new QuizApiRepository(this);
        userProfileRepository = new UserProfileApiRepository(this);
    }
    
    /**
     * Bắt đầu quiz từ Intent parameters
     */
    private void startQuizFromIntent() {
        Intent intent = getIntent();
        
        // Kiểm tra xem có câu hỏi đã tải sẵn không
        boolean hasQuestions = intent.getBooleanExtra("has_questions", false);
        String questionsJson = intent.getStringExtra("questions_json");
        String categoryName = intent.getStringExtra("category_name");
        
        if (hasQuestions && questionsJson != null) {
            // Sử dụng câu hỏi đã tải sẵn
            startQuizWithPreloadedQuestions(questionsJson, categoryName);
        } else {
            // Sử dụng API cũ (cần authentication)
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
    
    /**
     * Bắt đầu quiz với câu hỏi đã tải sẵn (không cần API)
     */
    private void startQuizWithPreloadedQuestions(String questionsJson, String categoryName) {
        try {
            // Parse JSON thành danh sách câu hỏi
            com.google.gson.Gson gson = new com.google.gson.Gson();
            com.google.gson.reflect.TypeToken<List<com.example.iq5.core.network.QuizApiService.TestQuestionModel>> typeToken = 
                new com.google.gson.reflect.TypeToken<List<com.example.iq5.core.network.QuizApiService.TestQuestionModel>>() {};
            List<com.example.iq5.core.network.QuizApiService.TestQuestionModel> testQuestions = gson.fromJson(questionsJson, typeToken.getType());
            
            if (testQuestions != null && !testQuestions.isEmpty()) {
                // Convert TestQuestionModel thành Question
                List<Question> questions = convertTestQuestionsToQuestions(testQuestions);
                
                // Bắt đầu quiz với câu hỏi đã có
                startQuizWithQuestions(questions, categoryName);
                
                Toast.makeText(this, "✅ Bắt đầu quiz: " + categoryName + " (" + questions.size() + " câu)", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "❌ Không có câu hỏi để hiển thị", Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (Exception e) {
            Toast.makeText(this, "❌ Lỗi xử lý câu hỏi: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }
    
    /**
     * Convert TestQuestionModel thành Question model
     */
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
            
            // Create options list from individual options
            q.createOptionsFromIndividual();
            
            questions.add(q);
        }
        
        return questions;
    }
    
    /**
     * Bắt đầu quiz với danh sách câu hỏi có sẵn
     */
    private void startQuizWithQuestions(List<Question> questions, String categoryName) {
        this.preloadedQuestions = questions;
        this.currentQuestionIndex = 0;
        this.isQuizStarted = true;
        this.usingPreloadedQuestions = true;
        
        // Hiển thị câu hỏi đầu tiên
        if (!questions.isEmpty()) {
            displayQuestion(questions.get(0));
            updateQuestionProgress();
        }
    }
    
    /**
     * Cập nhật progress của câu hỏi
     */
    private void updateQuestionProgress() {
        if (usingPreloadedQuestions && !preloadedQuestions.isEmpty()) {
            // Hiển thị progress cho câu hỏi sẵn
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
    
    /**
     * Chuyển sang câu hỏi tiếp theo
     */
    private void moveToNextQuestion() {
        if (usingPreloadedQuestions) {
            // Sử dụng câu hỏi đã tải sẵn
            currentQuestionIndex++;
            
            if (currentQuestionIndex < preloadedQuestions.size()) {
                displayQuestion(preloadedQuestions.get(currentQuestionIndex));
                updateQuestionProgress();
            } else {
                // Hết câu hỏi - hiển thị kết quả
                showQuizResult();
            }
        } else {
            // Sử dụng API để lấy câu hỏi tiếp theo (logic cũ)
            loadNextQuestionFromApi();
        }
    }
    
    /**
     * Hiển thị kết quả quiz
     */
    private void showQuizResult() {
        int correctAnswers = 0;
        int totalQuestions = answeredQuestions.size();
        
        Log.d(TAG, "🧮 CALCULATING QUIZ RESULTS:");
        Log.d(TAG, "📊 Total questions answered: " + totalQuestions);
        
        // Đếm số câu trả lời đúng với debug logging
        for (int i = 0; i < answeredQuestions.size(); i++) {
            Question q = answeredQuestions.get(i);
            boolean isCorrect = q.isAnsweredCorrectly();
            
            Log.d(TAG, "❓ Question " + (i+1) + ":");
            Log.d(TAG, "   📝 Question: " + q.getQuestion_text());
            Log.d(TAG, "   👤 User selected: '" + q.getUser_selected_answer_id() + "'");
            Log.d(TAG, "   ✅ Correct answer: '" + q.getCorrect_answer() + "'");
            Log.d(TAG, "   🎯 Is correct (stored): " + isCorrect);
            
            // Double check the comparison manually
            String userAnswer = q.getUser_selected_answer_id();
            String correctAnswer = q.getCorrect_answer();
            boolean manualCheck = userAnswer != null && userAnswer.equals(correctAnswer);
            Log.d(TAG, "   🔍 Manual check: " + manualCheck);
            Log.d(TAG, "   📊 answeredQuestions size: " + answeredQuestions.size());
            
            if (isCorrect) {
                correctAnswers++;
                Log.d(TAG, "   ✅ Counting as correct! Total so far: " + correctAnswers);
            } else {
                Log.d(TAG, "   ❌ Counting as wrong!");
            }
        }
        
        double score = totalQuestions > 0 ? (double) correctAnswers / totalQuestions * 100 : 0;
        
        Log.d(TAG, "🏆 FINAL RESULTS:");
        Log.d(TAG, "   ✅ Correct answers: " + correctAnswers);
        Log.d(TAG, "   📊 Total questions: " + totalQuestions);
        Log.d(TAG, "   💯 Score: " + score + "%");
        
        // Show debug toast with detailed info
        String debugInfo = String.format(
            "🧮 DEBUG RESULT:\n" +
            "📊 Total questions: %d\n" +
            "✅ Correct answers: %d\n" +
            "💯 Score: %d%%\n" +
            "📝 answeredQuestions.size(): %d",
            totalQuestions, correctAnswers, (int)score, answeredQuestions.size()
        );
        
        Toast.makeText(this, debugInfo, Toast.LENGTH_LONG).show();
        
        // Also show a simple debug toast
        Toast.makeText(this, 
            "🧮 SIMPLE: " + correctAnswers + "/" + totalQuestions + " = " + (int)score + "%", 
            Toast.LENGTH_SHORT).show();
        
        // Tạo Bundle kết quả
        Bundle resultData = new Bundle();
        resultData.putInt("correct_answers", correctAnswers);
        resultData.putInt("total_questions", totalQuestions);
        resultData.putDouble("score", score);
        resultData.putString("category", currentQuestion != null ? currentQuestion.getCategory() : "Unknown");
        
        // Cập nhật thống kê user trước khi chuyển màn hình
        updateUserStats(correctAnswers, totalQuestions, score, 
                       currentQuestion != null ? currentQuestion.getCategory() : "Unknown");
        
        // Chuyển sang màn hình kết quả
        NavigationHelper.navigateToResult(this, resultData);
        finish();
    }
    
    /**
     * Bắt đầu quiz với API
     */
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
                    
                    Toast.makeText(ApiQuizActivity.this, 
                        "✅ Bắt đầu quiz thành công!", Toast.LENGTH_SHORT).show();
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    showLoading(false);
                    Toast.makeText(ApiQuizActivity.this, 
                        "❌ " + error, Toast.LENGTH_LONG).show();
                    finish(); // Quay lại màn hình trước
                });
            }
        });
    }
    
    /**
     * Hiển thị câu hỏi lên UI
     */
    private void displayQuestion(Question question) {
        currentQuestion = question;
        
        // Update UI
        if (txtQuestion != null) {
            txtQuestion.setText(question.getQuestion_text());
        }
        
        // Setup options adapter
        if (question.getOptions() != null && rvOptions != null) {
            optionAdapter = new AnswerOptionAdapter(
                convertToOptions(question.getOptions()),
                option -> onUserSelectOption(option)
            );
            rvOptions.setAdapter(optionAdapter);
        }
        
        // Show/hide buttons
        updateButtonVisibility();
    }
    
    /**
     * Convert API options to local Option model
     */
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
    
    /**
     * Setup button listeners
     */
    private void setupButtons() {
        if (btnFinish != null) {
            btnFinish.setOnClickListener(v -> {
                if (usingPreloadedQuestions) {
                    showQuizResult();
                } else {
                    finishQuizWithApi();
                }
            });
        }
        
        if (btnNext != null) {
            btnNext.setOnClickListener(v -> moveToNextQuestion());
        }
        
        if (btnLifelineHint != null) {
            btnLifelineHint.setOnClickListener(v -> showHint());
        }
    }
    
    /**
     * Update button visibility based on quiz state
     */
    private void updateButtonVisibility() {
        // Implementation for button visibility
        if (btnNext != null) {
            btnNext.setVisibility(View.VISIBLE);
        }
        
        if (btnFinish != null) {
            btnFinish.setVisibility(View.VISIBLE);
        }
    }
    
    /**
     * Show/hide loading indicator
     */
    private void showLoading(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        
        // Disable buttons during loading
        if (btnNext != null) btnNext.setEnabled(!show);
        if (btnFinish != null) btnFinish.setEnabled(!show);
    }
    
    /**
     * Handle user option selection for preloaded questions
     */
    private void onUserSelectOption(com.example.iq5.feature.quiz.model.Option option) {
        if (currentQuestion == null) return;
        
        if (usingPreloadedQuestions) {
            // Handle selection for preloaded questions
            String selectedAnswer = option.getOption_id();
            currentQuestion.setUser_selected_answer_id(selectedAnswer);
            
            // Debug logging for answer comparison
            Log.d(TAG, "🎯 ANSWER SELECTION DEBUG:");
            Log.d(TAG, "   📝 Question: " + currentQuestion.getQuestion_text());
            Log.d(TAG, "   👤 User selected: '" + selectedAnswer + "'");
            Log.d(TAG, "   ✅ Correct answer: '" + currentQuestion.getCorrect_answer() + "'");
            Log.d(TAG, "   🔍 Comparison: '" + selectedAnswer + "'.equals('" + currentQuestion.getCorrect_answer() + "')");
            
            // Check if answer is correct
            boolean isCorrect = selectedAnswer.equals(currentQuestion.getCorrect_answer());
            Log.d(TAG, "   🎯 Result: " + isCorrect);
            
            currentQuestion.setAnsweredCorrectly(isCorrect);
            
            // Create a copy of current question to preserve answer state
            Question answeredQuestion = createQuestionCopy(currentQuestion);
            answeredQuestion.setUser_selected_answer_id(selectedAnswer);
            answeredQuestion.setAnsweredCorrectly(isCorrect);
            
            // Add to answered questions
            answeredQuestions.add(answeredQuestion);
            
            Log.d(TAG, "📝 ADDED TO ANSWERED QUESTIONS:");
            Log.d(TAG, "   📊 Total answered questions now: " + answeredQuestions.size());
            Log.d(TAG, "   🎯 This question marked as: " + (isCorrect ? "CORRECT" : "WRONG"));
            Log.d(TAG, "   💾 Stored user answer: '" + answeredQuestion.getUser_selected_answer_id() + "'");
            Log.d(TAG, "   ✅ Stored correct answer: '" + answeredQuestion.getCorrect_answer() + "'");
            Log.d(TAG, "   🏁 Stored isCorrect flag: " + answeredQuestion.isAnsweredCorrectly());
            
            // Show result with debug info
            String resultMsg = isCorrect ? "✅ ĐÚNG!" : "❌ SAI! Đáp án đúng: " + currentQuestion.getCorrect_answer();
            Toast.makeText(this, resultMsg, Toast.LENGTH_SHORT).show();
            
            // Show debug info about storage
            String debugMsg = String.format(
                "📊 DEBUG: Câu %d/%d - %s\n" +
                "👤 Chọn: %s | ✅ Đúng: %s\n" +
                "📝 Tổng đã trả lời: %d",
                currentQuestionIndex + 1, 
                preloadedQuestions.size(),
                isCorrect ? "ĐÚNG" : "SAI",
                selectedAnswer,
                currentQuestion.getCorrect_answer(),
                answeredQuestions.size()
            );
            Toast.makeText(this, debugMsg, Toast.LENGTH_LONG).show();
            
            // Auto move to next question after 1 second
            new android.os.Handler().postDelayed(() -> moveToNextQuestion(), 1000);
            
        } else {
            // Handle selection for API questions (original logic)
            currentQuestion.setUser_selected_answer_id(option.getOption_id());
            submitCurrentAnswer();
        }
        
        // Update adapter
        if (optionAdapter != null) {
            optionAdapter.notifyDataSetChanged();
        }
    }
    
    /**
     * Create a copy of question to preserve answer state
     */
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
    
    /**
     * Load next question from API (original method)
     */
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
                    Toast.makeText(ApiQuizActivity.this, 
                        "❌ Lỗi tải câu hỏi: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }
    
    /**
     * Finish quiz using API
     */
    private void finishQuizWithApi() {
        if (currentAttemptId == -1) return;
        
        showLoading(true);
        
        quizRepository.endQuiz(currentAttemptId, new QuizApiRepository.QuizEndCallback() {
            @Override
            public void onSuccess(com.example.iq5.core.network.QuizApiService.QuizResult result) {
                runOnUiThread(() -> {
                    showLoading(false);
                    
                    Bundle resultData = new Bundle();
                    resultData.putInt("correct_answers", result.getSoCauDung());
                    resultData.putInt("total_questions", result.getTongCauHoi());
                    resultData.putDouble("score", result.getDiem());
                    resultData.putString("category", currentQuestion != null ? currentQuestion.getCategory() : "Unknown");
                    
                    NavigationHelper.navigateToResult(ApiQuizActivity.this, resultData);
                    finish();
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    showLoading(false);
                    Toast.makeText(ApiQuizActivity.this, 
                        "❌ Lỗi kết thúc quiz: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }
    
    /**
     * Show hint for current question
     */
    private void showHint() {
        if (currentQuestion != null && currentQuestion.getExplanation() != null) {
            Toast.makeText(this, "💡 " + currentQuestion.getExplanation(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "💡 Không có gợi ý cho câu hỏi này", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Submit current answer (for API mode)
     */
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
                    
                    // Hiển thị kết quả
                    String resultMsg = isCorrect ? "✅ ĐÚNG!" : "❌ SAI!";
                    Toast.makeText(ApiQuizActivity.this, resultMsg, Toast.LENGTH_SHORT).show();
                    
                    // Lưu câu hỏi đã trả lời
                    currentQuestion.setAnsweredCorrectly(isCorrect);
                    answeredQuestions.add(currentQuestion);
                    
                    // Enable nút Next
                    if (btnNext != null) {
                        btnNext.setEnabled(true);
                    }
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    showLoading(false);
                    Toast.makeText(ApiQuizActivity.this, 
                        "❌ Lỗi nộp đáp án: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }
    
    /**
     * Cập nhật thống kê user sau khi hoàn thành quiz
     */
    private void updateUserStats(int correctAnswers, int totalQuestions, double score, String category) {
        Log.d(TAG, "📊 Updating user stats...");
        
        userProfileRepository.updateQuizStats(correctAnswers, totalQuestions, score, category, 
            new UserProfileApiRepository.UpdateCallback() {
                @Override
                public void onSuccess(String message) {
                    Log.d(TAG, "✅ User stats updated successfully: " + message);
                    // Không cần làm gì thêm, chỉ log thành công
                }
                
                @Override
                public void onUnauthorized() {
                    Log.e(TAG, "❌ Unauthorized when updating user stats");
                    // Có thể redirect về login, nhưng không bắt buộc
                }
                
                @Override
                public void onError(String error) {
                    Log.e(TAG, "❌ Error updating user stats: " + error);
                    // Không hiển thị lỗi cho user vì không ảnh hưởng đến flow chính
                }
            });
    }
}