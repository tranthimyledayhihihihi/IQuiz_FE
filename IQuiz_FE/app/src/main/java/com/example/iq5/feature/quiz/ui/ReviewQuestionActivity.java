package com.example.iq5.feature.quiz.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iq5.R;
import com.example.iq5.feature.quiz.adapter.ReviewQuestionAdapter;
import com.example.iq5.feature.quiz.model.Question;

import java.io.Serializable;
import java.util.List;

public class ReviewQuestionActivity extends AppCompatActivity {

    private static final String TAG = "ReviewQuestionActivity";

    private List<Question> reviewedQuestions;

    // UI (mới theo layout redesign)
    private ImageButton btnBack;
    private TextView txtTitle;
    private TextView txtSubtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_question);

        mapViews();

        if (!extractDataFromIntent()) {
            finish();
            return;
        }

        bindHeader();
        setupRecyclerView();
        setupActions();
    }

    private void mapViews() {
        // Các view này chỉ có nếu bạn đã thay layout theo bản redesign
        btnBack = findViewById(R.id.btnBack);
        txtTitle = findViewById(R.id.txtTitle);
        txtSubtitle = findViewById(R.id.txtSubtitle);
    }

    private void setupActions() {
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }

    private void bindHeader() {
        int total = reviewedQuestions != null ? reviewedQuestions.size() : 0;

        if (txtTitle != null) {
            txtTitle.setText("Xem lại câu hỏi");
        }
        if (txtSubtitle != null) {
            txtSubtitle.setText("Tổng " + total + " câu");
        }
    }

    /**
     * Nhận danh sách câu hỏi từ Intent một cách an toàn
     */
    @SuppressWarnings("unchecked")
    private boolean extractDataFromIntent() {
        Serializable data = getIntent().getSerializableExtra("questions");

        if (!(data instanceof List)) {
            Log.e(TAG, "Dữ liệu truyền vào không phải List<Question>");
            Toast.makeText(this, "Không thể tải dữ liệu xem lại", Toast.LENGTH_LONG).show();
            return false;
        }

        try {
            reviewedQuestions = (List<Question>) data;
        } catch (Exception e) {
            Log.e(TAG, "Lỗi ép kiểu List<Question>: ", e);
            Toast.makeText(this, "Lỗi dữ liệu câu hỏi", Toast.LENGTH_LONG).show();
            return false;
        }

        if (reviewedQuestions == null || reviewedQuestions.isEmpty()) {
            Toast.makeText(this, "Không có câu hỏi nào để xem lại", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /**
     * Setup RecyclerView hiển thị danh sách câu hỏi review
     */
    private void setupRecyclerView() {
        RecyclerView rvReview = findViewById(R.id.recyclerReview);

        rvReview.setLayoutManager(new LinearLayoutManager(this));
        rvReview.setHasFixedSize(true);
        rvReview.setAdapter(new ReviewQuestionAdapter(reviewedQuestions));
    }
}
