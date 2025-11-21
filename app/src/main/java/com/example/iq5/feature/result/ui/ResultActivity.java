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
import com.example.iq5.feature.result.model.MatchResult;
import com.example.iq5.feature.result.data.ResultRepository;

public class ResultActivity extends AppCompatActivity {

    // View ánh xạ
    private TextView tvStatus;
    private ImageView ivEmoji;
    private TextView tvScore, tvCorrect, tvIncorrect;
    private LinearLayout layoutStars;
    private TextView tvStar1, tvStar2, tvStar3;
    private Button btnPlayAgain, btnRetry, btnShare;
    private TextView tvBadge1, tvBadge2;

    private ResultRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        mapViews();

        // Khởi tạo Repository
        repository = new ResultRepository(this);

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
            int goldColor = ContextCompat.getColor(this, R.color.color_gold);

            tvStatus.setText("🎉 XUẤT SẮC!");
            tvStatus.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_success));

            ivEmoji.setImageResource(R.drawable.ic_trophy);
            ivEmoji.setColorFilter(goldColor);

            tvScore.setTextColor(goldColor);

            btnRetry.setText("🏡 Trang chủ");
            btnRetry.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_primary));

            updateStars(correctCount, total);

        } else {
            int errorColor = ContextCompat.getColor(this, R.color.color_error);

            tvStatus.setText("😞 CHƯA ĐẠT");
            tvStatus.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_error));

            ivEmoji.setImageResource(R.drawable.ic_sad_face);
            ivEmoji.setColorFilter(errorColor);

            tvScore.setTextColor(errorColor);

            btnRetry.setText("🔄 Xem lại câu sai");
            btnRetry.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_warning));

            layoutStars.setVisibility(View.GONE);
        }

        // --- 2. Cập nhật Điểm số và Thống kê ---
        tvScore.setText(String.valueOf(score));
        tvCorrect.setText(correctCount + "/" + total);
        tvIncorrect.setText((total - correctCount) + "/" + total);

        // --- 3. Hiển thị Badge/Thành tựu nếu có ---
        displayAchievementBadges(score, correctCount, total);

        // TODO: Gọi API để lưu kết quả và cập nhật streak
        // saveMatchResult(result);
        // updateUserStreak();
    }

    private void updateStars(int correct, int total) {
        int stars = 0;
        if (correct == total) {
            stars = 3;
        } else if (correct >= total * 0.8) {
            stars = 2;
        } else if (correct >= total * 0.5) {
            stars = 1;
        }

        int gold = ContextCompat.getColor(this, R.color.color_gold);
        int gray = ContextCompat.getColor(this, R.color.color_border_light);

        tvStar1.setTextColor(stars >= 1 ? gold : gray);
        tvStar2.setTextColor(stars >= 2 ? gold : gray);
        tvStar3.setTextColor(stars >= 3 ? gold : gray);
    }

    /**
     * Hiển thị badge thành tựu nếu đạt được mốc đặc biệt.
     */
    private void displayAchievementBadges(int score, int correct, int total) {
        // Ẩn badge mặc định
        tvBadge1.setVisibility(View.GONE);
        tvBadge2.setVisibility(View.GONE);

        // Kiểm tra thành tựu "Hoàn hảo"
        if (correct == total) {
            tvBadge1.setText("🏆 Hoàn hảo!");
            tvBadge1.setVisibility(View.VISIBLE);
        }

        // Kiểm tra thành tựu "Điểm cao"
        if (score >= 1000) {
            tvBadge2.setText("⭐ Điểm cao!");
            tvBadge2.setVisibility(View.VISIBLE);
        }

        // TODO: Lấy thêm thành tựu từ Repository
        // checkAndDisplayNewAchievements();
    }

    private void shareResult() {
        String shareText = "Tôi vừa đạt " + tvScore.getText().toString() +
                " điểm trong Quiz App! Thử thách bản thân ngay!";
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, "Chia sẻ kết quả"));
    }
}