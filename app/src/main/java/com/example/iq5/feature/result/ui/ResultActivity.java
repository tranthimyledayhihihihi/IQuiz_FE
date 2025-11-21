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
import com.example.iq5.feature.quiz.model.Question;
import com.example.iq5.feature.quiz.ui.ReviewQuestionActivity;
import com.example.iq5.feature.quiz.ui.SelectCategoryActivity;
import com.example.iq5.feature.result.model.MatchResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    // View ánh xạ
    private TextView tvStatus;
    private ImageView ivEmoji;
    private TextView tvScore, tvCorrect, tvIncorrect;
    private LinearLayout layoutStars;
    private TextView tvStar1, tvStar2, tvStar3;
    private Button btnPlayAgain, btnRetry, btnShare;
    private TextView tvBadge1, tvBadge2;

    private int score;
    private int total;
    private int correctCount;
    private boolean isWin;

    private List<Question> questionList;   // nhận từ QuizActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        mapViews();
        getDataFromIntent();
        displayResult(score, correctCount, total, isWin);
        setupButtons();
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

    /**
     * Nhận dữ liệu từ QuizActivity
     */
    @SuppressWarnings("unchecked")
    private void getDataFromIntent() {

        questionList = (List<Question>) getIntent().getSerializableExtra("questions");
        score = getIntent().getIntExtra("score", 0);
        total = getIntent().getIntExtra("total", 1);

        // Đếm số câu đúng từ list câu hỏi
        int count = 0;
        if (questionList != null) {
            for (Question q : questionList) {
                if (q.isUserAnswerCorrect()) {
                    count++;
                }
            }
        }
        correctCount = count;

        // Thắng = >= 80%
        isWin = correctCount >= Math.ceil(total * 0.8);
    }

    private void displayResult(int score, int correct, int total, boolean isWin) {

        // --- 1. Cấu hình giao diện thắng/thua ---
        if (isWin) {
            int gold = ContextCompat.getColor(this, R.color.color_gold);
            tvStatus.setText("🎉 XUẤT SẮC!");
            tvStatus.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_success));
            ivEmoji.setImageResource(R.drawable.ic_trophy);
            ivEmoji.setColorFilter(gold);
            tvScore.setTextColor(gold);

            btnRetry.setText("🏡 Trang chủ");
            btnRetry.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_primary));

            updateStars(correct, total);

        } else {
            int error = ContextCompat.getColor(this, R.color.color_error);
            tvStatus.setText("😞 CHƯA ĐẠT");
            tvStatus.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_error));
            ivEmoji.setImageResource(R.drawable.ic_sad_face);
            ivEmoji.setColorFilter(error);
            tvScore.setTextColor(error);

            btnRetry.setText("🔄 Xem lại câu sai");
            btnRetry.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_warning));

            layoutStars.setVisibility(View.GONE);
        }

        // --- 2. Điểm và thống kê ---
        tvScore.setText(String.valueOf(score));
        tvCorrect.setText(correct + "/" + total);
        tvIncorrect.setText((total - correct) + "/" + total);

        displayAchievementBadges(score, correct, total);
    }

    private void updateStars(int correct, int total) {
        int stars = 0;
        if (correct == total) stars = 3;
        else if (correct >= total * 0.8) stars = 2;
        else if (correct >= total * 0.5) stars = 1;

        int gold = ContextCompat.getColor(this, R.color.color_gold);
        int gray = ContextCompat.getColor(this, R.color.color_border_light);

        tvStar1.setTextColor(stars >= 1 ? gold : gray);
        tvStar2.setTextColor(stars >= 2 ? gold : gray);
        tvStar3.setTextColor(stars >= 3 ? gold : gray);
    }

    private void displayAchievementBadges(int score, int correct, int total) {
        tvBadge1.setVisibility(View.GONE);
        tvBadge2.setVisibility(View.GONE);

        if (correct == total) {
            tvBadge1.setText("🏆 Hoàn hảo!");
            tvBadge1.setVisibility(View.VISIBLE);
        }

        if (score >= 1000) {
            tvBadge2.setText("⭐ Điểm cao!");
            tvBadge2.setVisibility(View.VISIBLE);
        }
    }

    private void setupButtons() {

        // CHƠI LẠI → quay về chọn loại quiz
        btnPlayAgain.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, SelectCategoryActivity.class);
            startActivity(intent);
            finish();
        });

        // RETRY (tuỳ theo thắng/thua)
        btnRetry.setOnClickListener(v -> {

            // Nếu thua → mở review câu sai
            if (!isWin) {
                openReviewIncorrect();
                return;
            }

            // Nếu thắng → về Home
            finish();
        });

        btnShare.setOnClickListener(v -> shareResult());
    }

    /**
     * Chỉ gửi những câu user trả lời sai hoặc bỏ qua
     */
    private void openReviewIncorrect() {

        List<Question> wrongList = new ArrayList<>();

        for (Question q : questionList) {
            if (!q.isUserAnswerCorrect()) {
                wrongList.add(q);
            }
        }

        Intent intent = new Intent(ResultActivity.this, ReviewQuestionActivity.class);
        intent.putExtra("questions", (Serializable) wrongList);
        startActivity(intent);
    }

    private void shareResult() {
        String shareText = "Tôi vừa đạt " + tvScore.getText().toString() +
                " điểm trong Quiz App! Bạn có dám thử không?";
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, "Chia sẻ kết quả"));
    }
}
