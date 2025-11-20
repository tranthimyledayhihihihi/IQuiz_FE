package com.example.iq5.feature.result.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.iq5.R;
import com.example.iq5.feature.result.model.MatchResult; // Giả định class MatchResult tồn tại

public class ResultActivity extends AppCompatActivity {

    // View ánh xạ
    private TextView tvStatus;
    private ImageView ivEmoji;
    private TextView tvScore, tvCorrect, tvIncorrect;
    private LinearLayout layoutStars;
    private TextView tvStar1, tvStar2, tvStar3;
    private Button btnPlayAgain, btnRetry, btnShare;
    private TextView tvBadge1, tvBadge2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        mapViews();

        // Dữ liệu giả: Thắng (100 điểm, 10/10, 3 sao)
        MatchResult mockResult = new MatchResult(100, 10, 10, true);

        // Dữ liệu giả: Thua (20 điểm, 2/10, 0 sao)
        // MatchResult mockResult = new MatchResult(20, 2, 10, false);

        displayResult(mockResult);

        btnPlayAgain.setOnClickListener(v -> {
            // Logic chơi lại (Intent đến GameActivity)
            // startActivity(new Intent(this, GameActivity.class));
            finish();
        });

        btnRetry.setOnClickListener(v -> {
            // Logic Trượt/Về Trang chủ
            finish();
        });

        btnShare.setOnClickListener(v -> {
            // Logic chia sẻ (Intent.ACTION_SEND)
            shareResult();
        });
    }

    private void mapViews() {
        tvStatus = findViewById(R.id.tv_result_status);
        ivEmoji = findViewById(R.id.iv_emoji);
        layoutStars = findViewById(R.id.layout_stars);
        tvStar1 = findViewById(R.id.tv_star1);
        tvStar2 = findViewById(R.id.tv_star2);
        tvStar3 = findViewById(R.id.tv_star3);
        tvScore = findViewById(R.id.tv_final_score);
        tvCorrect = findViewById(R.id.tv_correct_count);
        tvIncorrect = findViewById(R.id.tv_incorrect_count);
        tvBadge1 = findViewById(R.id.tv_badge_1);
        tvBadge2 = findViewById(R.id.tv_badge_2);
        btnPlayAgain = findViewById(R.id.btn_play_again);
        btnRetry = findViewById(R.id.btn_retry);
        btnShare = findViewById(R.id.btn_share);
    }

    private void displayResult(MatchResult result) {
        int correctCount = result.getCorrectAnswers();
        int total = result.getTotalQuestions();
        boolean isWin = result.isWin();
        int score = result.getScore();

        // --- 1. Cấu hình Màu sắc, Icon và Nút ---
        if (isWin) {
            // Trạng thái THẮNG: Vàng/Xanh lá
            int goldColor = ContextCompat.getColor(this, R.color.color_gold);

            tvStatus.setText("🎉 XUẤT SẮC!");
            tvStatus.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_success));

            ivEmoji.setImageResource(R.drawable.ic_trophy);
            ivEmoji.setColorFilter(goldColor); // Đặt màu vàng cho Trophy

            tvScore.setTextColor(goldColor); // Điểm màu Vàng

            // Nút Retry thành 'Trang chủ' (Dùng Tím Primary)
            btnRetry.setText("🏡 Trang chủ");
            // SỬA LỖI: purple_primary -> color_primary
            btnRetry.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_primary));

            updateStars(correctCount, total);

        } else {
            // Trạng thái THUA: Đỏ
            int errorColor = ContextCompat.getColor(this, R.color.color_error);

            tvStatus.setText("😞 CHƯA ĐẠT");
            tvStatus.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_error));

            ivEmoji.setImageResource(R.drawable.ic_sad_face);
            ivEmoji.setColorFilter(errorColor); // Đặt màu đỏ cho Icon

            tvScore.setTextColor(errorColor); // Điểm màu Đỏ

            // Nút chính: Chơi lại (Giữ nguyên text/màu từ XML)
            // btnPlayAgain.setText("Thử lại");

            // Nút phụ: Xem lại câu sai (Dùng màu Cam Warning)
            btnRetry.setText("🔄 Xem lại câu sai");
            btnRetry.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_warning)); // Màu Cam Warning

            layoutStars.setVisibility(View.GONE); // Ẩn sao nếu thua
        }

        // --- 2. Cập nhật Điểm số và Thống kê ---
        tvScore.setText(String.valueOf(score));

        // Format lại text thống kê đúng/sai
        tvCorrect.setText(correctCount + "/" + total);
        tvIncorrect.setText((total - correctCount) + "/" + total);

        // TODO: Gọi API 1. Lưu kết quả trận đấu và 2. Tính chuỗi ngày
    }

    // --- 3. Logic Cập nhật Sao ---
    private void updateStars(int correct, int total) {
        int stars = 0;
        if (correct == total) {
            stars = 3; // Hoàn hảo
        } else if (correct >= total * 0.8) {
            stars = 2; // Rất tốt
        } else if (correct >= total * 0.5) {
            stars = 1; // Đạt
        }

        // Đổi màu vàng cho số sao đạt được
        int gold = ContextCompat.getColor(this, R.color.color_gold);
        // SỬA LỖI: border_light -> color_border_light
        int gray = ContextCompat.getColor(this, R.color.color_border_light);

        tvStar1.setTextColor(stars >= 1 ? gold : gray);
        tvStar2.setTextColor(stars >= 2 ? gold : gray);
        tvStar3.setTextColor(stars >= 3 ? gold : gray);
    }

    // --- 4. Hàm Chia sẻ ---
    private void shareResult() {
        String shareText = "Tôi vừa đạt " + tvScore.getText().toString() + " điểm trong Quiz App! Thử thách bản thân ngay!";
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, "Chia sẻ kết quả"));
    }
}