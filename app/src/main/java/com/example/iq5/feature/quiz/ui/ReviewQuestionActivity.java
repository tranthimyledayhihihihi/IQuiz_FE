package com.example.iq5.feature.quiz.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast; // Import mới

import com.example.iq5.R;
import com.example.iq5.feature.quiz.adapter.ReviewQuestionAdapter;
import com.example.iq5.feature.quiz.model.Question;

import java.util.List;

public class ReviewQuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_review_question);

        RecyclerView rv = findViewById(R.id.recyclerReview);

        // LƯU Ý: Phải ép kiểu (List<Question>) vì dữ liệu được truyền là Serializable
        List<Question> list = (List<Question>) getIntent().getSerializableExtra("questions");

        if (list != null) {
            rv.setAdapter(new ReviewQuestionAdapter(list));
        } else {
            // Xử lý trường hợp danh sách câu hỏi bị null (ví dụ: do lỗi tải hoặc chuyển màn hình quá sớm)
            Toast.makeText(this, "Không có dữ liệu câu hỏi để xem lại.", Toast.LENGTH_LONG).show();
            // Tùy chọn: Có thể đóng màn hình hoặc hiển thị một trạng thái rỗng khác
            // finish();
        }
    }
}