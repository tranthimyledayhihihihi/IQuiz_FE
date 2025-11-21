package com.example.iq5.feature.quiz.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.iq5.R;
import com.example.iq5.feature.quiz.adapter.ReviewQuestionAdapter;
import com.example.iq5.feature.quiz.model.Question;

import java.io.Serializable;
import java.util.List;

public class ReviewQuestionActivity extends AppCompatActivity {

    private static final String TAG = "ReviewQuestionActivity";

    private List<Question> reviewedQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_question);

        if (!extractDataFromIntent()) {
            finish();
            return;
        }

        setupRecyclerView();
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
        rvReview.setAdapter(new ReviewQuestionAdapter(reviewedQuestions));
    }
}
