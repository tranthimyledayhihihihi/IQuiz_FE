package com.example.iq5.feature.history.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.core.network.HistoryApiService;
import com.example.iq5.data.repository.HistoryApiRepository;
import com.example.iq5.utils.ApiHelper;

/**
 * Simple History Detail Activity sử dụng API thật từ backend
 */
public class SimpleApiHistoryDetailActivity extends AppCompatActivity {

    private static final String TAG = "SimpleApiHistoryDetailActivity";
    
    // Repository
    private HistoryApiRepository historyRepository;
    
    // Data
    private int attemptId;
    private HistoryApiService.HistoryDetail historyDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.R.layout.activity_list_item);

        getIntentData();
        initRepository();
        loadHistoryDetail();
        
        Toast.makeText(this, "📖 History Detail - Đang tải từ API...", 
            Toast.LENGTH_LONG).show();
    }
    
    private void getIntentData() {
        attemptId = getIntent().getIntExtra("attemptId", -1);
        
        if (attemptId == -1) {
            Toast.makeText(this, "❌ Không tìm thấy ID lịch sử", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    
    private void initRepository() {
        historyRepository = new HistoryApiRepository(this);
    }
    
    /**
     * Load chi tiết lịch sử
     */
    private void loadHistoryDetail() {
        historyRepository.getHistoryDetail(attemptId, 
            new HistoryApiRepository.HistoryDetailCallback() {
            @Override
            public void onSuccess(HistoryApiService.HistoryDetail detail) {
                runOnUiThread(() -> {
                    historyDetail = detail;
                    displayHistoryDetail(detail);
                    
                    Toast.makeText(SimpleApiHistoryDetailActivity.this, 
                        "✅ Đã tải chi tiết lịch sử từ API!", Toast.LENGTH_SHORT).show();
                });
            }
            
            @Override
            public void onNotFound() {
                runOnUiThread(() -> {
                    Toast.makeText(SimpleApiHistoryDetailActivity.this, 
                        "❌ Không tìm thấy lịch sử này", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
            
            @Override
            public void onUnauthorized() {
                runOnUiThread(() -> {
                    Toast.makeText(SimpleApiHistoryDetailActivity.this, 
                        "❌ Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show();
                    
                    // Clear token và quay về login
                    ApiHelper.clearToken(SimpleApiHistoryDetailActivity.this);
                    finish();
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(SimpleApiHistoryDetailActivity.this, 
                        "❌ Lỗi tải chi tiết: " + error, Toast.LENGTH_LONG).show();
                    finish();
                });
            }
        });
    }
    
    /**
     * Hiển thị chi tiết lịch sử
     */
    private void displayHistoryDetail(HistoryApiService.HistoryDetail detail) {
        // Hiển thị thông tin cơ bản
        String basicInfo = String.format(
            "📊 Kết Quả Quiz\n" +
            "Ngày: %s\n" +
            "Điểm: %.1f\n" +
            "Đúng: %d/%d câu\n" +
            "Trạng thái: %s",
            formatDate(detail.getNgayLam()),
            detail.getDiem(),
            detail.getSoCauDung(),
            detail.getTongCauHoi(),
            detail.getTrangThaiKetQua()
        );
        
        Toast.makeText(this, basicInfo, Toast.LENGTH_LONG).show();
        
        // Hiển thị chi tiết câu hỏi nếu có
        if (detail.getChiTietCauHoi() != null && !detail.getChiTietCauHoi().isEmpty()) {
            displayQuestionDetails(detail.getChiTietCauHoi());
        }
    }
    
    /**
     * Hiển thị chi tiết câu hỏi
     */
    private void displayQuestionDetails(java.util.List<HistoryApiService.QuestionResult> questions) {
        // Hiển thị câu hỏi đầu tiên làm ví dụ
        if (!questions.isEmpty()) {
            HistoryApiService.QuestionResult firstQuestion = questions.get(0);
            String status = firstQuestion.isCorrect() ? "✅ Đúng" : "❌ Sai";
            
            String questionDetail = String.format(
                "📝 Câu hỏi đầu tiên:\n" +
                "%s\n" +
                "Bạn chọn: %s\n" +
                "Đáp án đúng: %s\n" +
                "Kết quả: %s",
                firstQuestion.getCauHoi(),
                firstQuestion.getDapAnChon(),
                firstQuestion.getDapAnDung(),
                status
            );
            
            Toast.makeText(this, questionDetail, Toast.LENGTH_LONG).show();
        }
        
        // Thống kê tổng quan
        int correctCount = 0;
        for (HistoryApiService.QuestionResult q : questions) {
            if (q.isCorrect()) correctCount++;
        }
        
        String summary = String.format(
            "📈 Tổng quan: %d/%d câu đúng (%.1f%%)",
            correctCount,
            questions.size(),
            (correctCount * 100.0 / questions.size())
        );
        
        Toast.makeText(this, summary, Toast.LENGTH_SHORT).show();
    }
    
    /**
     * Format date string
     */
    private String formatDate(String dateString) {
        return dateString != null ? dateString : "Không có thông tin";
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "👋 Thoát History Detail", Toast.LENGTH_SHORT).show();
    }
}