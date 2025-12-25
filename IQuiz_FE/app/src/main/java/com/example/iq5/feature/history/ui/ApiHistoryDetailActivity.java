package com.example.iq5.feature.history.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.core.network.HistoryApiService;
import com.example.iq5.data.repository.HistoryApiRepository;
import com.example.iq5.utils.ApiHelper;

/**
 * History Detail Activity sử dụng API thật từ backend
 */
public class ApiHistoryDetailActivity extends AppCompatActivity {

    private static final String TAG = "ApiHistoryDetailActivity";
    
    // UI Components
    private TextView tvQuizDate, tvScore, tvCorrectCount, tvStatus;
    private RecyclerView rvQuestions;
    private ProgressBar progressBar;
    private View layoutNoData;
    
    // Repository
    private HistoryApiRepository historyRepository;
    
    // Data
    private int attemptId;
    private HistoryApiService.HistoryDetail historyDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Sử dụng layout đơn giản nếu layout chính không tồn tại
        try {
            setContentView(R.layout.activity_main); // Using existing layout as placeholder
        } catch (Exception e) {
            setContentView(android.R.layout.activity_list_item);
        }

        getIntentData();
        initViews();
        initRepository();
        setupButtons();
        loadHistoryDetail();
    }
    
    private void getIntentData() {
        attemptId = getIntent().getIntExtra("attemptId", -1);
        
        if (attemptId == -1) {
            Toast.makeText(this, "❌ Không tìm thấy ID lịch sử", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    
    private void initViews() {
        // Comment out missing views - these need to be added to the layout
        // tvQuizDate = findViewById(R.id.tvQuizDate);
        // tvScore = findViewById(R.id.tvScore);
        // tvCorrectCount = findViewById(R.id.tvCorrectCount);
        // tvStatus = findViewById(R.id.tvStatus);
        // rvQuestions = findViewById(R.id.rvQuestions);
        progressBar = findViewById(R.id.progressBar);
        // layoutNoData = findViewById(R.id.layoutNoData);
        
        // Setup RecyclerView
        if (rvQuestions != null) {
            rvQuestions.setLayoutManager(new LinearLayoutManager(this));
        }
    }
    
    private void initRepository() {
        historyRepository = new HistoryApiRepository(this);
    }
    
    /**
     * Load chi tiết lịch sử
     */
    private void loadHistoryDetail() {
        showLoading(true);
        
        historyRepository.getHistoryDetail(attemptId, 
            new HistoryApiRepository.HistoryDetailCallback() {
            @Override
            public void onSuccess(HistoryApiService.HistoryDetail detail) {
                runOnUiThread(() -> {
                    showLoading(false);
                    historyDetail = detail;
                    displayHistoryDetail(detail);
                    
                    Toast.makeText(ApiHistoryDetailActivity.this, 
                        "✅ Đã tải chi tiết lịch sử!", Toast.LENGTH_SHORT).show();
                });
            }
            
            @Override
            public void onNotFound() {
                runOnUiThread(() -> {
                    showLoading(false);
                    showNoDataMessage("Không tìm thấy lịch sử này");
                });
            }
            
            @Override
            public void onUnauthorized() {
                runOnUiThread(() -> {
                    showLoading(false);
                    Toast.makeText(ApiHistoryDetailActivity.this, 
                        "❌ Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show();
                    
                    // Clear token và quay về login
                    ApiHelper.clearToken(ApiHistoryDetailActivity.this);
                    finish();
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    showLoading(false);
                    Toast.makeText(ApiHistoryDetailActivity.this, 
                        "❌ Lỗi tải chi tiết: " + error, Toast.LENGTH_LONG).show();
                    showNoDataMessage("Lỗi: " + error);
                });
            }
        });
    }
    
    /**
     * Hiển thị chi tiết lịch sử
     */
    private void displayHistoryDetail(HistoryApiService.HistoryDetail detail) {
        // Basic info
        if (tvQuizDate != null) {
            tvQuizDate.setText("Ngày làm: " + formatDate(detail.getNgayLam()));
        }
        
        if (tvScore != null) {
            tvScore.setText(String.format("%.1f điểm", detail.getDiem()));
        }
        
        if (tvCorrectCount != null) {
            tvCorrectCount.setText(detail.getSoCauDung() + "/" + detail.getTongCauHoi() + " câu đúng");
        }
        
        if (tvStatus != null) {
            tvStatus.setText("Trạng thái: " + detail.getTrangThaiKetQua());
        }
        
        // Questions detail
        if (detail.getChiTietCauHoi() != null && !detail.getChiTietCauHoi().isEmpty()) {
            displayQuestions(detail.getChiTietCauHoi());
        } else {
            showNoDataMessage("Không có chi tiết câu hỏi");
        }
    }
    
    /**
     * Hiển thị danh sách câu hỏi
     */
    private void displayQuestions(java.util.List<HistoryApiService.QuestionResult> questions) {
        if (rvQuestions != null) {
            rvQuestions.setVisibility(View.VISIBLE);
        }
        
        if (layoutNoData != null) {
            layoutNoData.setVisibility(View.GONE);
        }
        
        // TODO: Setup adapter cho RecyclerView
        // QuestionResultAdapter adapter = new QuestionResultAdapter(questions);
        // rvQuestions.setAdapter(adapter);
        
        // Tạm thời hiển thị thông tin câu hỏi đầu tiên
        if (!questions.isEmpty()) {
            HistoryApiService.QuestionResult firstQuestion = questions.get(0);
            String status = firstQuestion.isCorrect() ? "✅ Đúng" : "❌ Sai";
            
            Toast.makeText(this, 
                "Câu 1: " + status + "\n" +
                "Đáp án chọn: " + firstQuestion.getDapAnChon() + "\n" +
                "Đáp án đúng: " + firstQuestion.getDapAnDung(), 
                Toast.LENGTH_LONG).show();
        }
    }
    
    /**
     * Hiển thị thông báo không có dữ liệu
     */
    private void showNoDataMessage(String message) {
        if (layoutNoData != null) {
            layoutNoData.setVisibility(View.VISIBLE);
        }
        
        if (rvQuestions != null) {
            rvQuestions.setVisibility(View.GONE);
        }
        
        // Tìm TextView trong layoutNoData để hiển thị message
        // TextView tvNoDataMessage = layoutNoData.findViewById(R.id.tvNoDataMessage);
        // if (tvNoDataMessage != null) {
        //     tvNoDataMessage.setText(message);
        // }
    }
    
    /**
     * Format date string
     */
    private String formatDate(String dateString) {
        // TODO: Implement proper date formatting
        return dateString != null ? dateString : "Không có thông tin";
    }
    
    /**
     * Show/hide loading indicator
     */
    private void showLoading(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
    
    /**
     * Setup button listeners
     */
    private void setupButtons() {
        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }
}