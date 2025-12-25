package com.example.iq5.feature.result.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.iq5.feature.quiz.model.Question;
import com.example.iq5.feature.quiz.ui.ReviewQuestionActivity;
import com.example.iq5.feature.quiz.ui.SelectCategoryActivity;
import com.example.iq5.feature.result.model.MatchResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private static final String TAG = "ResultActivity";
    
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

    private List<Question> questionList;

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

    @SuppressWarnings("unchecked")
    private void getDataFromIntent() {
        questionList = (List<Question>) getIntent().getSerializableExtra("questions");
        
        // Try to get data from API quiz first (new format)
        int apiCorrectAnswers = getIntent().getIntExtra("correct_answers", -1);
        int apiTotalQuestions = getIntent().getIntExtra("total_questions", -1);
        double apiScore = getIntent().getDoubleExtra("score", -1);
        
        Log.d(TAG, "🎯 RESULT ACTIVITY DEBUG:");
        Log.d(TAG, "   📊 API correct_answers: " + apiCorrectAnswers);
        Log.d(TAG, "   📊 API total_questions: " + apiTotalQuestions);
        Log.d(TAG, "   📊 API score: " + apiScore);
        
        if (apiCorrectAnswers != -1 && apiTotalQuestions != -1) {
            // Use API quiz data
            correctCount = apiCorrectAnswers;
            total = apiTotalQuestions;
            score = (int) apiScore;
            
            Log.d(TAG, "   ✅ Using API data:");
            Log.d(TAG, "      correctCount = " + correctCount);
            Log.d(TAG, "      total = " + total);
            Log.d(TAG, "      score = " + score);
            
            // Show debug toast
            Toast.makeText(this, 
                "🎯 API DATA: " + correctCount + "/" + total + " = " + score + "%", 
                Toast.LENGTH_LONG).show();
                
        } else {
            // Fallback to old format
            score = getIntent().getIntExtra("score", 0);
            total = getIntent().getIntExtra("total", 1);

            int count = 0;
            if (questionList != null) {
                for (Question q : questionList) {
                    if (q.isUserAnswerCorrect()) {
                        count++;
                    }
                }
            }
            correctCount = count;
            
            Log.d(TAG, "   ⚠️ Using fallback data:");
            Log.d(TAG, "      correctCount = " + correctCount);
            Log.d(TAG, "      total = " + total);
            Log.d(TAG, "      score = " + score);
            
            // Show debug toast
            Toast.makeText(this, 
                "⚠️ FALLBACK DATA: " + correctCount + "/" + total + " = " + score + "%", 
                Toast.LENGTH_LONG).show();
        }
        
        isWin = correctCount >= Math.ceil(total * 0.8);
    }

    private void displayResult(int score, int correct, int total, boolean isWin) {
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
        btnPlayAgain.setOnClickListener(v -> {
            NavigationHelper.navigateToSelectCategory(this);
            finish();
        });

        btnRetry.setOnClickListener(v -> {
            if (!isWin) {
                openReviewIncorrect();
                return;
            }

            NavigationHelper.navigateToHome(this, false);
        });

        btnShare.setOnClickListener(v -> shareResult());
    }

    private void openReviewIncorrect() {
        // Open new Wrong Question Review Activity
        Intent intent = new Intent(this, com.example.iq5.feature.specialmode.ui.WrongQuestionReviewActivity.class);
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
