package com.example.fe;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;

public class ResultActivity extends AppCompatActivity {

    private LinearLayout layoutHeaderResult;
    private ImageView imgResultIcon;
    private TextView tvResultStatus, tvFinalScore, tvCorrectAnswers;
    private Button btnPlayAgain, btnBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        layoutHeaderResult = findViewById(R.id.layout_header_result);
        imgResultIcon = findViewById(R.id.img_result_icon);
        tvResultStatus = findViewById(R.id.tv_result_status);
        tvFinalScore = findViewById(R.id.tv_final_score);
        tvCorrectAnswers = findViewById(R.id.tv_correct_answers);
        btnPlayAgain = findViewById(R.id.btn_play_again);
        btnBackHome = findViewById(R.id.btn_back_home);

        // Lấy dữ liệu (Giả lập)
        // Đổi giá trị này để kiểm tra trạng thái THUA:
        boolean isWin = true; // Thay bằng Intent.getBooleanExtra("IS_WIN", false);
        int finalScore = 1500;
        int correctCount = 8;
        int totalQuestions = 10;

        updateResultUI(isWin, finalScore, correctCount, totalQuestions);

        btnPlayAgain.setOnClickListener(v -> Toast.makeText(this, "Chuyển đến màn hình chơi mới", Toast.LENGTH_SHORT).show());
        btnBackHome.setOnClickListener(v -> Toast.makeText(this, "Trở về Trang chủ", Toast.LENGTH_SHORT).show());
    }

    private void updateResultUI(boolean isWin, int score, int correct, int total) {
        if (isWin) {
            // Cập nhật cho trạng thái THẮNG [cite: 120]

            // 1. Header Background: Vàng (#FFD700) [cite: 122]
            layoutHeaderResult.setBackgroundColor(ContextCompat.getColor(this, R.color.color_gold));

            // 2. Icon: Cúp, màu Trắng trên nền Vàng [cite: 123]
            imgResultIcon.setImageResource(R.drawable.ic_trophy);
            imgResultIcon.setColorFilter(ContextCompat.getColor(this, R.color.white)); // Đặt lại tint về Trắng (nếu có tint Đỏ từ Thua)

            // 3. Status Text & Score: Vàng (#FFD700) [cite: 125, 126]
            tvResultStatus.setText(getString(R.string.result_win));
            tvResultStatus.setTextColor(ContextCompat.getColor(this, R.color.white)); // Header text trắng
            tvFinalScore.setTextColor(ContextCompat.getColor(this, R.color.color_gold));

            // 4. Nút chính (Play Again): Tím Primary (#6C4FFF) [cite: 127]
            btnPlayAgain.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.purple_primary));

            // 5. Nút phụ (Back Home): Tím Nhạt (#E8E3FF) [cite: 134]
            btnBackHome.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.purple_light));
            btnBackHome.setTextColor(ContextCompat.getColor(this, R.color.purple_primary));

        } else {
            // Cập nhật cho trạng thái THUA [cite: 128]

            // 1. Header Background: Trắng (#FFFFFF) [cite: 129]
            layoutHeaderResult.setBackgroundColor(ContextCompat.getColor(this, R.color.white));

            // 2. Icon: Mặt buồn, set tint Đỏ (#FF3B30) [cite: 130]
            imgResultIcon.setImageResource(R.drawable.ic_sad_face);
            imgResultIcon.setColorFilter(ContextCompat.getColor(this, R.color.color_error)); // Tint icon thành Đỏ

            // 3. Status Text & Score: Đen/Đen xám [cite: 131, 132]
            tvResultStatus.setText(getString(R.string.result_lose));
            tvResultStatus.setTextColor(ContextCompat.getColor(this, R.color.text_primary_dark)); // Header text đen
            tvFinalScore.setTextColor(ContextCompat.getColor(this, R.color.text_secondary_dark_gray));

            // 4. Nút chính (Play Again/Thử lại): Tím Primary (#6C4FFF) [cite: 133]
            btnPlayAgain.setText(getString(R.string.play_again));
            btnPlayAgain.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.purple_primary));

            // 5. Nút phụ (Back Home): Tím Nhạt (#E8E3FF) [cite: 134]
            btnBackHome.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.purple_light));
            btnBackHome.setTextColor(ContextCompat.getColor(this, R.color.purple_primary)); // Text màu Tím Primary
        }

        // Set điểm và số câu đúng chung
        tvFinalScore.setText(String.valueOf(score));
        tvCorrectAnswers.setText(getString(R.string.correct_answers_format, correct, total));

        // TODO: Gọi API 1. Lưu kết quả trận đấu, 2. Tính chuỗi ngày, 4. Thành tựu
    }
}