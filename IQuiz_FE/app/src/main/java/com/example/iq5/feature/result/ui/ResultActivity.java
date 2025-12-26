package com.example.iq5.feature.result.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.iq5.R;
import com.example.iq5.core.navigation.NavigationHelper;
import com.example.iq5.feature.quiz.ui.ReviewQuestionActivity;
import com.example.iq5.feature.quiz.model.Question;

import java.io.Serializable;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private TextView tvStatus;
    private ImageView ivEmoji;
    private TextView tvScore, tvCorrect, tvIncorrect;
    private LinearLayout layoutStars;
    private TextView tvStar1, tvStar2, tvStar3;
    private Button btnPlayAgain, btnRetry, btnShare;
    private TextView tvBadge1, tvBadge2;

    private int points = 0;
    private int total = 0;
    private int correctCount = 0;
    private double percent = 0;
    private boolean isWin = false;
    private Button btnHome;



    // ✅ danh sách câu sai của lượt vừa chơi
    private List<Question> wrongQuestionsThisRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        mapViews();
        getDataFromIntent();
        displayResult(points, percent, correctCount, total, isWin);
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
        btnHome = findViewById(R.id.btn_home);

    }


    @SuppressWarnings("unchecked")
    private void getDataFromIntent() {
        correctCount = getIntent().getIntExtra("correct_answers", 0);
        total = getIntent().getIntExtra("total_questions", 0);
        percent = getIntent().getDoubleExtra("score", 0);
        points = getIntent().getIntExtra("points", (int) Math.round(percent));

        Serializable wrongData = getIntent().getSerializableExtra("wrong_questions");
        if (wrongData instanceof List) {
            wrongQuestionsThisRun = (List<Question>) wrongData;
        }

        isWin = total > 0 && percent >= 80.0;
    }

    private void displayResult(int points, double percent, int correct, int total, boolean isWin) {
        if (isWin) {
            int gold = ContextCompat.getColor(this, R.color.color_gold);
            tvStatus.setText("XUẤT SẮC!");
            tvStatus.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_success));
            ivEmoji.setImageResource(R.drawable.ic_trophy);
            ivEmoji.setColorFilter(gold);
            tvScore.setTextColor(gold);

            btnRetry.setText("Trang chủ");
            btnRetry.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_primary));
            updateStars(correct, total);

        } else {
            int error = ContextCompat.getColor(this, R.color.color_error);
            tvStatus.setText("CHƯA ĐẠT");
            tvStatus.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_error));
            ivEmoji.setImageResource(R.drawable.ic_sad_face);
            ivEmoji.setColorFilter(error);
            tvScore.setTextColor(error);

            btnRetry.setText("Xem lại câu sai");
            btnRetry.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_warning));
            layoutStars.setVisibility(View.VISIBLE);
            updateStars(correct, total);

        }

        // ✅ hiển thị điểm theo POINTS
        tvScore.setText(String.valueOf(points));
        tvCorrect.setText(correct + "/" + total);
        tvIncorrect.setText((total - correct) + "/" + total);

        displayAchievementBadges(points, correct, total);
    }

    private void updateStars(int correct, int total) {
        int stars = 0;

        if (total <= 0) {
            stars = 0;
        } else if (correct == total) {
            stars = 3;
        } else if (correct >= Math.ceil(total * 0.8)) {
            stars = 2;
        } else if (correct >= Math.ceil(total * 0.5)) {
            stars = 1;
        }

        int gold = ContextCompat.getColor(this, R.color.color_gold);
        int gray = ContextCompat.getColor(this, R.color.color_border_light);

        tvStar1.setTextColor(stars >= 1 ? gold : gray);
        tvStar2.setTextColor(stars >= 2 ? gold : gray);
        tvStar3.setTextColor(stars >= 3 ? gold : gray);

        // Nếu muốn “ẩn hẳn” khi 0 sao thì dùng:
        // layoutStars.setVisibility(stars == 0 ? View.GONE : View.VISIBLE);
    }


    private void displayAchievementBadges(int points, int correct, int total) {
        tvBadge1.setVisibility(View.GONE);
        tvBadge2.setVisibility(View.GONE);

        if (total > 0 && correct == total) {
            tvBadge1.setText("Hoàn hảo!");
            tvBadge1.setVisibility(View.VISIBLE);
        }

        // ví dụ badge theo POINTS
        if (points >= 1000) {
            tvBadge2.setText("Điểm cao!");
            tvBadge2.setVisibility(View.VISIBLE);
        }
    }

    private void setupButtons() {
        btnPlayAgain.setOnClickListener(v -> {
            NavigationHelper.navigateToSelectCategory(this);
            finish();
        });
        btnHome.setOnClickListener(v -> {
            NavigationHelper.navigateToHome(this, false);
            finish();
        });


        btnRetry.setOnClickListener(v -> {
            if (!isWin) {
                openReviewWrongThisRun();
                return;
            }
            NavigationHelper.navigateToHome(this, false);
        });

        btnShare.setOnClickListener(v -> shareResult());
    }

    private void openReviewWrongThisRun() {
        if (wrongQuestionsThisRun == null || wrongQuestionsThisRun.isEmpty()) {
            Toast.makeText(this, "Không có câu sai để xem lại", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent i = new Intent(this, ReviewQuestionActivity.class);
        i.putExtra("questions", (Serializable) wrongQuestionsThisRun);
        startActivity(i);
    }

    private void shareResult() {
        String shareText = "Tôi vừa đạt " + tvScore.getText().toString() + " điểm trong Quiz App!";
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, "Chia sẻ kết quả"));
    }
}
