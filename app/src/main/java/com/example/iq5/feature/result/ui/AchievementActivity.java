package com.example.iq5.feature.result.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.iq5.R;
import com.example.iq5.feature.result.adapter.AchievementAdapter;
import com.example.iq5.feature.result.model.Achievement;
import java.util.ArrayList;
import java.util.List;

public class AchievementActivity extends AppCompatActivity {

    private RecyclerView rvUnlocked, rvLocked; // Khai báo hai RecyclerView mới

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        // KHẮC PHỤC LỖI ID: Ánh xạ hai RecyclerView mới
        rvUnlocked = findViewById(R.id.rv_achievements_unlocked);
        rvLocked = findViewById(R.id.rv_achievements_locked);

        // Chuẩn bị dữ liệu
        List<Achievement> mockAchievements = createMockAchievements();
        List<Achievement> unlockedList = new ArrayList<>();
        List<Achievement> lockedList = new ArrayList<>();

        for (Achievement ach : mockAchievements) {
            if (ach.isUnlocked()) {
                unlockedList.add(ach);
            } else {
                lockedList.add(ach);
            }
        }

        // Cấu hình Adapter cho Đã mở khóa (Unlocked)
        rvUnlocked.setLayoutManager(new GridLayoutManager(this, 2));
        AchievementAdapter unlockedAdapter = new AchievementAdapter(unlockedList);
        rvUnlocked.setAdapter(unlockedAdapter);

        // Cấu hình Adapter cho Chưa mở khóa (Locked)
        rvLocked.setLayoutManager(new GridLayoutManager(this, 2));
        AchievementAdapter lockedAdapter = new AchievementAdapter(lockedList);
        rvLocked.setAdapter(lockedAdapter);

        // Xử lý nút back/đóng nếu cần
        findViewById(R.id.btn_back_achieve).setOnClickListener(v -> finish());
    }

    private List<Achievement> createMockAchievements() {
        List<Achievement> achievements = new ArrayList<>();
        // KHẮC PHỤC LỖI CONSTRUCTOR: Cung cấp 5 tham số (bao gồm ID Icon Drawable)

        // Demo: Đã mở khóa (Sử dụng R.drawable.ic_trophy, R.drawable.ic_fire_streak...)
        achievements.add(new Achievement(1, "Vô địch tuần", "Top 1 bảng xếp hạng", true, R.drawable.ic_trophy));
        achievements.add(new Achievement(2, "Streak 30", "Chơi 30 ngày liên tiếp", true, R.drawable.ic_fire));

        // Demo: Chưa mở khóa
        achievements.add(new Achievement(3, "Học giả", "Đúng 100 câu khoa học", false, R.drawable.ic_books));
        achievements.add(new Achievement(4, "Huyền thoại", "Điểm tổng > 100.000", false, R.drawable.ic_crown));
        return achievements;
    }
}