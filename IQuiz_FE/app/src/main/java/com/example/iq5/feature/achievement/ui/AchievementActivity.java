package com.example.iq5.feature.achievement.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

        // ⚠️ ID PHẢI TỒN TẠI TRONG XML
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
