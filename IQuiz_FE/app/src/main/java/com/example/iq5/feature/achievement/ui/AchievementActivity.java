package com.example.iq5.feature.achievement.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.iq5.R;
import com.example.iq5.data.model.AchievementsResponse;
import com.example.iq5.data.repository.AchievementApiRepository;

import java.util.List;

public class AchievementActivity extends AppCompatActivity {

    private LinearLayout layoutAchievements;
    private AchievementApiRepository repository;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        layoutAchievements = findViewById(R.id.layoutAchievements);
        repository = new AchievementApiRepository(this);

        loadAchievements();
    }

    private void loadAchievements() {
        repository.getMyAchievements(new AchievementApiRepository.AchievementsCallback() {

            @Override
            public void onSuccess(List<AchievementsResponse.Achievement> list) {
                runOnUiThread(() -> showAchievements(list));
            }

            @Override
            public void onUnauthorized() {
                Toast.makeText(
                        AchievementActivity.this,
                        "Phiên đăng nhập hết hạn",
                        Toast.LENGTH_SHORT
                ).show();
                finish();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(
                        AchievementActivity.this,
                        "Lỗi: " + error,
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void showAchievements(List<AchievementsResponse.Achievement> list) {
        layoutAchievements.removeAllViews();

        if (list == null || list.isEmpty()) {
            showEmptyState();
            return;
        }

        for (AchievementsResponse.Achievement a : list) {
            layoutAchievements.addView(createAchievementCard(a));
        }
    }

    private CardView createAchievementCard(AchievementsResponse.Achievement achievement) {
        // CardView chính
        CardView card = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, dpToPx(16));
        card.setLayoutParams(cardParams);
        card.setRadius(dpToPx(16));
        card.setCardElevation(dpToPx(3));
        card.setUseCompatPadding(true);

        // Container layout bên trong card
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setPadding(dpToPx(20), dpToPx(20), dpToPx(20), dpToPx(20));
        container.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        // Icon achievement
        TextView iconView = new TextView(this);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
                dpToPx(64), dpToPx(64)
        );
        iconParams.setMarginEnd(dpToPx(16));
        iconView.setLayoutParams(iconParams);
        iconView.setText(achievement.icon != null ? achievement.icon : "🏆");
        iconView.setTextSize(36);
        iconView.setGravity(Gravity.CENTER);
        iconView.setBackgroundResource(R.drawable.bg_icon_circle_inline);

        // Content layout (bên phải icon)
        LinearLayout contentLayout = new LinearLayout(this);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        contentLayout.setLayoutParams(new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
        ));

        // Title
        TextView title = new TextView(this);
        title.setText(achievement.tenThanhTuu);
        title.setTextSize(18);
        title.setTextColor(Color.parseColor("#1A1A1A"));
        title.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        titleParams.setMargins(0, 0, 0, dpToPx(6));
        title.setLayoutParams(titleParams);

        // Description
        TextView description = new TextView(this);
        description.setText(achievement.moTa);
        description.setTextSize(14);
        description.setTextColor(Color.parseColor("#666666"));
        LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        descParams.setMargins(0, 0, 0, dpToPx(12));
        description.setLayoutParams(descParams);

        // Bottom info layout
        LinearLayout bottomLayout = new LinearLayout(this);
        bottomLayout.setOrientation(LinearLayout.HORIZONTAL);
        bottomLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        // Points info
        LinearLayout pointsLayout = new LinearLayout(this);
        pointsLayout.setOrientation(LinearLayout.HORIZONTAL);
        pointsLayout.setGravity(Gravity.CENTER_VERTICAL);
        pointsLayout.setLayoutParams(new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
        ));

        TextView pointsIcon = new TextView(this);
        pointsIcon.setText("🎁 +");
        pointsIcon.setTextSize(13);

        TextView points = new TextView(this);
        points.setText(String.valueOf(achievement.diemThuong));
        points.setTextSize(13);
        points.setTextColor(Color.parseColor("#FF6B35"));
        points.setTypeface(null, Typeface.BOLD);

        TextView pointsLabel = new TextView(this);
        pointsLabel.setText(" điểm");
        pointsLabel.setTextSize(13);
        pointsLabel.setTextColor(Color.parseColor("#FF6B35"));

        pointsLayout.addView(pointsIcon);
        pointsLayout.addView(points);
        pointsLayout.addView(pointsLabel);

        // Date
        TextView date = new TextView(this);
        date.setText("📅 " + achievement.ngayDatDuoc);
        date.setTextSize(12);
        date.setTextColor(Color.parseColor("#999999"));

        bottomLayout.addView(pointsLayout);
        bottomLayout.addView(date);

        // Add all views
        contentLayout.addView(title);
        contentLayout.addView(description);
        contentLayout.addView(bottomLayout);

        container.addView(iconView);
        container.addView(contentLayout);

        card.addView(container);

        return card;
    }

    private void showEmptyState() {
        // Card cho empty state
        CardView emptyCard = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, dpToPx(20), 0, 0);
        emptyCard.setLayoutParams(cardParams);
        emptyCard.setRadius(dpToPx(20));
        emptyCard.setCardElevation(dpToPx(2));
        emptyCard.setUseCompatPadding(true);

        LinearLayout emptyLayout = new LinearLayout(this);
        emptyLayout.setOrientation(LinearLayout.VERTICAL);
        emptyLayout.setGravity(Gravity.CENTER);
        emptyLayout.setPadding(dpToPx(40), dpToPx(50), dpToPx(40), dpToPx(50));

        TextView emptyIcon = new TextView(this);
        emptyIcon.setText("🎯");
        emptyIcon.setTextSize(72);
        emptyIcon.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        iconParams.setMargins(0, 0, 0, dpToPx(20));
        emptyIcon.setLayoutParams(iconParams);

        TextView emptyTitle = new TextView(this);
        emptyTitle.setText("Chưa có thành tựu");
        emptyTitle.setTextSize(22);
        emptyTitle.setTextColor(Color.parseColor("#1A1A1A"));
        emptyTitle.setTypeface(null, Typeface.BOLD);
        emptyTitle.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        titleParams.setMargins(0, 0, 0, dpToPx(10));
        emptyTitle.setLayoutParams(titleParams);

        TextView emptyDesc = new TextView(this);
        emptyDesc.setText("Hãy hoàn thành các thử thách\nđể nhận thành tựu đầu tiên của bạn! 🌟");
        emptyDesc.setTextSize(15);
        emptyDesc.setTextColor(Color.parseColor("#666666"));
        emptyDesc.setGravity(Gravity.CENTER);
        emptyDesc.setLineSpacing(dpToPx(4), 1.0f);

        emptyLayout.addView(emptyIcon);
        emptyLayout.addView(emptyTitle);
        emptyLayout.addView(emptyDesc);

        emptyCard.addView(emptyLayout);
        layoutAchievements.addView(emptyCard);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}