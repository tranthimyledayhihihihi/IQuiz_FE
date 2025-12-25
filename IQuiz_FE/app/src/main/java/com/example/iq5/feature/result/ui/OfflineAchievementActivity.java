package com.example.iq5.feature.result.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.R;
import com.example.iq5.feature.result.data.ResultRepository;
import com.example.iq5.feature.result.model.Achievement;

import java.util.List;

/**
 * OfflineAchievementActivity – FINAL
 * - Khớp 100% với model Achievement
 * - Dùng getIconResId()
 * - Không phụ thuộc UI cũ
 * - BUILD PASS
 */
public class OfflineAchievementActivity extends AppCompatActivity {

    private static final String TAG = "OfflineAchievement";

    private LinearLayout layoutAchievements;
    private ResultRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        layoutAchievements = findViewById(R.id.layoutAchievements);
        repository = new ResultRepository(this);

        loadOfflineAchievements();
    }

    private void loadOfflineAchievements() {
        Log.d(TAG, "🏆 Loading OFFLINE achievements...");

        List<Achievement> list = repository.getAchievements();

        if (list == null || list.isEmpty()) {
            Toast.makeText(
                    this,
                    "📱 Chưa có thành tựu offline",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        showAchievements(list);
    }

    private void showAchievements(List<Achievement> list) {
        layoutAchievements.removeAllViews();

        for (Achievement a : list) {
            TextView tv = new TextView(this);
            tv.setPadding(24, 24, 24, 24);

            // ✅ ĐÚNG THEO MODEL
            String icon = a.getIconResId() != null ? a.getIconResId() : "🏆";

            String status = a.isUnlocked() ? "✅ Đã mở" : "🔒 Chưa mở";

            tv.setText(
                    icon + " " + a.getTitle() + "\n" +
                            a.getDescription() + "\n" +
                            status + " (" +
                            a.getCurrentProgress() + "/" +
                            a.getTargetProgress() + ")"
            );

            layoutAchievements.addView(tv);
        }
    }
}
