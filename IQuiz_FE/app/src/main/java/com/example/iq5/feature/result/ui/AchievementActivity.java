package com.example.iq5.feature.result.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.R;
import com.example.iq5.data.model.AchievementsResponse;
import com.example.iq5.data.repository.AchievementApiRepository;

import java.util.List;

/**
 * AchievementActivity – phiên bản ĐÃ FIX
 * - KHÔNG dùng RecyclerView cũ
 * - KHÔNG dùng milestone / stats cũ
 * - DÙNG AchievementApiRepository + BE thật
 */
public class AchievementActivity extends AppCompatActivity {

    private static final String TAG = "AchievementActivity";

    private LinearLayout layoutAchievements;
    private AchievementApiRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        layoutAchievements = findViewById(R.id.layoutAchievements);
        repository = new AchievementApiRepository(this);

        loadAchievements();
    }

    private void loadAchievements() {
        Log.d(TAG, "🏆 Loading achievements from API...");

        repository.getMyAchievements(new AchievementApiRepository.AchievementsCallback() {
            @Override
            public void onSuccess(List<AchievementsResponse.Achievement> list) {
                runOnUiThread(() -> showAchievements(list));
            }

            @Override
            public void onUnauthorized() {
                runOnUiThread(() -> {
                    Toast.makeText(
                            AchievementActivity.this,
                            "❌ Phiên đăng nhập hết hạn",
                            Toast.LENGTH_LONG
                    ).show();
                    finish();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(
                            AchievementActivity.this,
                            "❌ Lỗi: " + error,
                            Toast.LENGTH_LONG
                    ).show();
                });
            }
        });
    }

    private void showAchievements(List<AchievementsResponse.Achievement> list) {
        layoutAchievements.removeAllViews();

        if (list == null || list.isEmpty()) {
            TextView tv = new TextView(this);
            tv.setText("Bạn chưa có thành tựu nào");
            layoutAchievements.addView(tv);
            return;
        }

        for (AchievementsResponse.Achievement a : list) {
            TextView tv = new TextView(this);
            tv.setPadding(24, 24, 24, 24);

            tv.setText(
                    (a.icon != null ? a.icon : "🏆") + " " + a.tenThanhTuu + "\n" +
                            a.moTa + "\n" +
                            "🎁 +" + a.diemThuong + " điểm\n" +
                            "📅 " + a.ngayDatDuoc
            );

            layoutAchievements.addView(tv);
        }
    }
}
