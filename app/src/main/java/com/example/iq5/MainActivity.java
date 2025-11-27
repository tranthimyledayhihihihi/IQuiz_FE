package com.example.iq5;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;

// Import màn hình FindMatchActivity (Màn 1)
import com.example.iq5.feature.multiplayer.ui.FindMatchActivity;
import com.example.iq5.feature.multiplayer.ui.LeaderboardActivity;
// SỬA LỖI: Import đúng Activity chứa ViewPager (Màn 5)


// Import các Activity mục tiêu
import com.example.iq5.feature.quiz.ui.SelectCategoryActivity; // Màn hình bắt đầu Quiz
import com.example.iq5.feature.quiz.ui.ReviewQuestionActivity; // Màn hình xem lại

import com.example.iq5.feature.result.ui.*; // Import tất cả các Activity demo

public class MainActivity extends AppCompatActivity {

    private Button btnGoToOnline;
    private Button btnGoToSocial; // Nút mới

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // --- Xử lý Insets (Giữ nguyên từ code gốc) ---
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        setContentView(R.layout.activity_main); // Tải layout activity_main.xml

        // Ánh xạ nút 1 (Luồng Chơi Game)
        btnGoToOnline = findViewById(R.id.btnGoToOnline);
        // Ánh xạ nút 2 (Luồng Xã Hội)
        btnGoToSocial = findViewById(R.id.btnGoToSocial);

        // Đặt sự kiện click cho Luồng 1
        btnGoToOnline.setOnClickListener(v -> {
            // Mở Màn hình 1: FindMatchActivity
            Intent intent = new Intent(MainActivity.this, FindMatchActivity.class);
            startActivity(intent);
        });

        // Đặt sự kiện click cho Luồng 2
        btnGoToSocial.setOnClickListener(v -> {
            // SỬA LỖI: Mở Màn hình 5 (FriendsLeaderboardActivity)
            Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
            startActivity(intent);
        });
        // ---------------------------------------------

        // 1. Tìm các nút
        Button btnStartQuiz = findViewById(R.id.btnStartQuiz);
        Button btnReview = findViewById(R.id.btnReview);
        Button btnSpecialMode = findViewById(R.id.btnSpecialMode);

        // 2. Thiết lập Listener cho "Bắt đầu Quiz"
        btnStartQuiz.setOnClickListener(v -> {
            // Chuyển sang màn hình chọn Category/Difficulty
            Intent intent = new Intent(MainActivity.this, SelectCategoryActivity.class);
            startActivity(intent);
        });

        // 3. Thiết lập Listener cho "Xem lại Câu hỏi"
        btnReview.setOnClickListener(v -> {
            // TODO: Thông thường, ReviewActivity cần danh sách câu hỏi để xem lại.
            // Vì đây là màn hình chính, chúng ta sẽ mở nó mà không có dữ liệu ban đầu
            // Bạn cần điều chỉnh sau này để truyền dữ liệu thực tế.
            Intent intent = new Intent(MainActivity.this, ReviewQuestionActivity.class);
            startActivity(intent);
        });

        // 4. Thiết lập Listener cho "Chế độ Đặc biệt" (Nếu có)
        btnSpecialMode.setOnClickListener(v -> {
            // TODO: Thay thế YourSpecialModeActivity.class bằng Activity thực tế của bạn
            // Hiện tại, ta có thể tạm thời hiển thị thông báo.
            // Intent intent = new Intent(MainActivity.this, YourSpecialModeActivity.class);
            // startActivity(intent);
        });
    }
}