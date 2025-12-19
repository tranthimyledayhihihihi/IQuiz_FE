package com.example.iq5.feature.specialmode.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.core.network.DailyQuizApiService;
import com.example.iq5.data.model.AnswerSubmit;
import com.example.iq5.data.model.Question;
import com.example.iq5.data.repository.DailyQuizApiRepository;
import com.example.iq5.feature.quiz.adapter.AnswerOptionAdapter;
import com.example.iq5.feature.quiz.model.Option;

import java.util.ArrayList;
import java.util.List;

/**
 * Daily Quiz Fragment sử dụng API thật từ backend
 */
public class ApiDailyQuizFragment extends Fragment {

    private static final String TAG = "ApiDailyQuizFragment";
    
    // UI Components
    private TextView tvQuizTitle, tvQuizDescription, tvQuestion;
    private RecyclerView rvOptions;
    private Button btnStart, btnSubmit, btnViewResult;
    private ProgressBar progressBar;
    
    // Data & Logic
    private DailyQuizApiRepository dailyQuizRepository;
    private DailyQuizApiService.DailyQuizDetails currentQuizDetails;
    private Question currentQuestion;
    private int currentAttemptId = -1;
    private boolean isQuizStarted = false;
    private boolean isAnswerSubmitted = false;
    
    // Adapter
    private AnswerOptionAdapter optionAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daily_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        initRepository();
        loadTodayQuiz();
    }
    
    private void initViews(View view) {
        tvQuizTitle = view.findViewById(R.id.tvQuizTitle);
        // Comment out missing views - these need to be added to the layout
        // tvQuizDescription = view.findViewById(R.id.tvQuizDescription);
        tvQuestion = view.findViewById(R.id.tvQuestion);
        // rvOptions = view.findViewById(R.id.rvOptions);
        // btnStart = view.findViewById(R.id.btnStart);
        // btnSubmit = view.findViewById(R.id.btnSubmit);
        // btnViewResult = view.findViewById(R.id.btnViewResult);
        progressBar = view.findViewById(R.id.progressBar);
        
        // Setup RecyclerView
        if (rvOptions != null) {
            rvOptions.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        
        setupButtons();
    }
    
    private void initRepository() {
        if (getContext() != null) {
            dailyQuizRepository = new DailyQuizApiRepository(getContext());
        }
    }
    
    /**
     * Load quiz hàng ngày từ API
     */
    private void loadTodayQuiz() {
        showLoading(true);
        
        dailyQuizRepository.getTodayQuiz(new DailyQuizApiRepository.TodayQuizCallback() {
            @Override
            public void onSuccess(DailyQuizApiService.DailyQuizDetails quizDetails) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showLoading(false);
                        currentQuizDetails = quizDetails;
                        displayQuizInfo(quizDetails);
                        
                        Toast.makeText(getContext(), 
                            "✅ Đã tải quiz hôm nay từ API!", Toast.LENGTH_SHORT).show();
                    });
                }
            }
            
            @Override
            public void onNoQuizToday() {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showLoading(false);
                        showNoQuizMessage();
                    });
                }
            }
            
            @Override
            public void onError(String error) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showLoading(false);
                        Toast.makeText(getContext(), 
                            "❌ Lỗi tải quiz: " + error, Toast.LENGTH_LONG).show();
                        showErrorMessage(error);
                    });
                }
            }
        });
    }
    
    /**
     * Hiển thị thông tin quiz
     */
    private void displayQuizInfo(DailyQuizApiService.DailyQuizDetails quizDetails) {
        if (tvQuizTitle != null) {
            tvQuizTitle.setText(quizDetails.getTieuDe() != null ? 
                quizDetails.getTieuDe() : "Quiz Hàng Ngày");
        }
        
        if (tvQuizDescription != null) {
            tvQuizDescription.setText(quizDetails.getMoTa() != null ? 
                quizDetails.getMoTa() : "Thử thách bản thân với câu hỏi hôm nay!");
        }
        
        // Hiển thị nút Start
        updateButtonVisibility(false, false, false);
    }
    
    /**
     * Bắt đầu quiz hàng ngày
     */
    private void startTodayQuiz() {
        showLoading(true);
        
        dailyQuizRepository.startTodayQuiz(new DailyQuizApiRepository.DailyQuizStartCallback() {
            @Override
            public void onSuccess(int attemptId, Question question, String message) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showLoading(false);
                        currentAttemptId = attemptId;
                        currentQuestion = question;
                        isQuizStarted = true;
                        
                        displayQuestion(question);
                        updateButtonVisibility(false, true, false);
                        
                        Toast.makeText(getContext(), 
                            "✅ " + message, Toast.LENGTH_SHORT).show();
                    });
                }
            }
            
            @Override
            public void onError(String error) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showLoading(false);
                        Toast.makeText(getContext(), 
                            "❌ Lỗi bắt đầu quiz: " + error, Toast.LENGTH_LONG).show();
                    });
                }
            }
        });
    }
    
    /**
     * Hiển thị câu hỏi
     */
    private void displayQuestion(Question question) {
        if (tvQuestion != null) {
            tvQuestion.setText(question.getQuestion_text());
        }
        
        // Setup options
        if (question.getOptions() != null && rvOptions != null) {
            optionAdapter = new AnswerOptionAdapter(
                convertToOptions(question.getOptions()),
                option -> onUserSelectOption(option)
            );
            rvOptions.setAdapter(optionAdapter);
        }
    }
    
    /**
     * Convert API options to local Option model
     */
    private List<Option> convertToOptions(List<Question.Option> apiOptions) {
        List<Option> options = new ArrayList<>();
        
        for (Question.Option apiOption : apiOptions) {
            Option option = new Option();
            option.setOption_id(apiOption.getOptionId());
            option.setOption_text(apiOption.getOptionText());
            options.add(option);
        }
        
        return options;
    }
    
    /**
     * Xử lý khi user chọn đáp án
     */
    private void onUserSelectOption(Option option) {
        if (currentQuestion == null || isAnswerSubmitted) return;
        
        // Lưu đáp án đã chọn
        currentQuestion.setUser_selected_answer_id(option.getOption_id());
        
        // Update adapter
        if (optionAdapter != null) {
            optionAdapter.notifyDataSetChanged();
        }
        
        // Enable nút Submit
        if (btnSubmit != null) {
            btnSubmit.setEnabled(true);
        }
    }
    
    /**
     * Nộp đáp án
     */
    private void submitAnswer() {
        if (currentQuestion == null || currentAttemptId == -1 || isAnswerSubmitted) return;
        
        showLoading(true);
        
        AnswerSubmit answerSubmit = new AnswerSubmit();
        answerSubmit.setAttemptId(currentAttemptId);
        answerSubmit.setQuestionId(currentQuestion.getQuestion_id());
        answerSubmit.setSelectedAnswerId(currentQuestion.getUser_selected_answer_id());
        
        dailyQuizRepository.submitDailyAnswer(answerSubmit, 
            new DailyQuizApiRepository.DailyAnswerSubmitCallback() {
            @Override
            public void onSuccess(boolean isCorrect, String message) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showLoading(false);
                        isAnswerSubmitted = true;
                        
                        // Hiển thị kết quả
                        String resultMsg = isCorrect ? "✅ ĐÚNG!" : "❌ SAI!";
                        Toast.makeText(getContext(), resultMsg, Toast.LENGTH_LONG).show();
                        
                        // Update UI
                        updateButtonVisibility(false, false, true);
                        
                        // Disable options
                        if (optionAdapter != null) {
                            // TODO: Disable option selection
                        }
                    });
                }
            }
            
            @Override
            public void onError(String error) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showLoading(false);
                        Toast.makeText(getContext(), 
                            "❌ Lỗi nộp đáp án: " + error, Toast.LENGTH_LONG).show();
                    });
                }
            }
        });
    }
    
    /**
     * Xem kết quả chi tiết
     */
    private void viewResult() {
        if (currentAttemptId == -1) return;
        
        showLoading(true);
        
        dailyQuizRepository.endTodayQuiz(currentAttemptId, 
            new DailyQuizApiRepository.DailyQuizEndCallback() {
            @Override
            public void onSuccess(DailyQuizApiService.DailyQuizResult result) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showLoading(false);
                        
                        // Hiển thị kết quả chi tiết
                        showDetailedResult(result);
                    });
                }
            }
            
            @Override
            public void onError(String error) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showLoading(false);
                        Toast.makeText(getContext(), 
                            "❌ Lỗi lấy kết quả: " + error, Toast.LENGTH_LONG).show();
                    });
                }
            }
        });
    }
    
    /**
     * Setup button listeners
     */
    private void setupButtons() {
        if (btnStart != null) {
            btnStart.setOnClickListener(v -> startTodayQuiz());
        }
        
        if (btnSubmit != null) {
            btnSubmit.setOnClickListener(v -> submitAnswer());
            btnSubmit.setEnabled(false);
        }
        
        if (btnViewResult != null) {
            btnViewResult.setOnClickListener(v -> viewResult());
        }
    }
    
    /**
     * Update button visibility
     */
    private void updateButtonVisibility(boolean showStart, boolean showSubmit, boolean showResult) {
        if (btnStart != null) {
            btnStart.setVisibility(showStart ? View.VISIBLE : View.GONE);
        }
        if (btnSubmit != null) {
            btnSubmit.setVisibility(showSubmit ? View.VISIBLE : View.GONE);
        }
        if (btnViewResult != null) {
            btnViewResult.setVisibility(showResult ? View.VISIBLE : View.GONE);
        }
    }
    
    /**
     * Show no quiz message
     */
    private void showNoQuizMessage() {
        if (tvQuizTitle != null) {
            tvQuizTitle.setText("Chưa có Quiz hôm nay");
        }
        if (tvQuizDescription != null) {
            tvQuizDescription.setText("Quiz hàng ngày sẽ được cập nhật sớm. Hãy quay lại sau!");
        }
        updateButtonVisibility(false, false, false);
    }
    
    /**
     * Show error message
     */
    private void showErrorMessage(String error) {
        if (tvQuizTitle != null) {
            tvQuizTitle.setText("Lỗi tải Quiz");
        }
        if (tvQuizDescription != null) {
            tvQuizDescription.setText("Không thể tải quiz hôm nay: " + error);
        }
        updateButtonVisibility(false, false, false);
    }
    
    /**
     * Show detailed result
     */
    private void showDetailedResult(DailyQuizApiService.DailyQuizResult result) {
        String resultText = String.format(
            "🎯 Kết quả Quiz Hàng Ngày\n\n" +
            "Điểm: %.1f\n" +
            "Số câu đúng: %d/%d\n" +
            "Trạng thái: %s",
            result.getDiem(),
            result.getSoCauDung(),
            result.getTongCauHoi(),
            result.getTrangThaiKetQua()
        );
        
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Kết Quả Quiz")
            .setMessage(resultText)
            .setPositiveButton("OK", null)
            .show();
    }
    
    /**
     * Show/hide loading indicator
     */
    private void showLoading(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}