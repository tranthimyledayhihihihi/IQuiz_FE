package com.example.iq5.feature.quiz.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.R;

// Cần thêm các imports sau để Intent hoạt động:
import com.example.iq5.feature.quiz.ui.SelectCategoryActivity;
import com.example.iq5.feature.quiz.ui.ReviewQuestionActivity;
// Giả định SpecialModeActivity nằm ở đây. Nếu không, cần sửa lại đường dẫn.
// Ví dụ: import com.example.iq5.feature.specialmode.ui.SpecialModeActivity;

public class Test extends AppCompatActivity {

    private Button btnStartQuiz;
    private Button btnReview;
    private Button btnSpecialMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Giả sử tên file XML bạn cung cấp là activity_main.xml
        setContentView(R.layout.quiz_test);

        // 1. Lấy View theo các ID từ XML
        btnStartQuiz = findViewById(R.id.btnStartQuiz);
        btnReview = findViewById(R.id.btnReview);
        btnSpecialMode = findViewById(R.id.btnSpecialMode);

        // 2. Thiết lập Listener

        // Mở màn hình Chọn Chủ đề/Độ khó
        btnStartQuiz.setOnClickListener(v -> {
            // Khởi động Activity để chọn danh mục/độ khó
            Intent intent = new Intent(this, SelectCategoryActivity.class);
            startActivity(intent);
        });

        // Mở màn hình Xem lại câu hỏi (Lịch sử làm bài)
        btnReview.setOnClickListener(v -> {
            // Khởi động Activity xem lại câu hỏi
            Intent intent = new Intent(this, ReviewQuestionActivity.class);
            startActivity(intent);
        });

        // Mở màn hình Chế độ Đặc biệt
        btnSpecialMode.setOnClickListener(v -> {
            // LỖI CŨ: Intent intent = new Intent(this, LifelineDialogFragment.class);
            // SỬA LẠI: Phải khởi động một Activity (ví dụ: SpecialModeActivity)

            // Do bạn chưa cung cấp tên class Special Mode, tôi sẽ dùng một Activity giả định
            // Bạn cần thay thế SpecialModeActivity.class bằng Activity chứa các nút Daily/Wrong/Challenge
            // Ví dụ: SpecialModeActivity.class hoặc TestModeActivity.class

            // Giả định bạn có class SpecialModeActivity:
            try {
                // SỬA LỖI Ở ĐÂY: Dùng Activity thay vì Fragment/Dialog
                Class<?> specialModeClass = Class.forName("com.example.iq5.feature.specialmode.ui.SpecialModeActivity");
                Intent intent = new Intent(this, specialModeClass);
                startActivity(intent);
            } catch (ClassNotFoundException e) {
                // Nếu không tìm thấy SpecialModeActivity, báo lỗi cho Dev
                Toast.makeText(this, "Lỗi: Không tìm thấy SpecialModeActivity.class", Toast.LENGTH_LONG).show();
            }
        });
    }
}