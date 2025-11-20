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

    private static final String TAG = "ReviewActivity";
    private List<Question> reviewedQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_question);

        Serializable serializable = getIntent().getSerializableExtra("questions");
        if (serializable instanceof List) {
            reviewedQuestions = (List<Question>) serializable;
        } else {
            Log.e(TAG, "Dữ liệu nhận không hợp lệ");
            Toast.makeText(this, "Không thể tải dữ liệu xem lại", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (reviewedQuestions == null || reviewedQuestions.isEmpty()) {
            Toast.makeText(this, "Không có câu hỏi nào để xem lại.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        RecyclerView rvReview = findViewById(R.id.recyclerReview);
        rvReview.setLayoutManager(new LinearLayoutManager(this));
        rvReview.setAdapter(new ReviewQuestionAdapter(reviewedQuestions));
    }
}
