package com.example.iq5.feature.result.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.iq5.R;
import com.example.iq5.feature.result.adapter.AchievementAdapter;
import com.example.iq5.feature.result.model.Achievement;
import com.example.iq5.feature.result.data.ResultRepository;
import java.util.ArrayList;
import java.util.List;

public class AchievementActivity extends AppCompatActivity {

    private RecyclerView rvUnlocked, rvLocked;
    private ResultRepository repository;

    // NOTE: Các View khác (tvUnlockedCount, CardView, v.v.) chưa được khai báo
    // trong phiên bản code này, nhưng chúng ta chỉ tập trung sửa lỗi Adapter hiện tại.
    // Nếu muốn chạy đầy đủ, bạn cần bổ sung các khai báo View bị thiếu như code trước.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        // 1. KHỞI TẠO REPOSITORY
        repository = new ResultRepository(this);

        // 2. GỌI PHƯƠNG THỨC LẤY DỮ LIỆU
        List<Achievement> mockAchievements = repository.getAchievements();

        // 3. Xử lý dữ liệu (Ánh xạ View và phân loại)
        // Lưu ý: Cần đảm bảo các ID này tồn tại trong activity_achievement.xml
        rvUnlocked = findViewById(R.id.rv_achievements_unlocked);
        rvLocked = findViewById(R.id.rv_achievements_locked);

        List<Achievement> unlockedList = new ArrayList<>();
        List<Achievement> lockedList = new ArrayList<>();

        for (Achievement ach : mockAchievements) {
            if (ach.isUnlocked()) {
                unlockedList.add(ach);
            } else {
                lockedList.add(ach);
            }
        }

        // 4. Cấu hình Adapter (ĐÃ SỬA LỖI)
        if (rvUnlocked != null) {
            rvUnlocked.setLayoutManager(new GridLayoutManager(this, 2));
            // TRUYỀN THIẾU ĐỐI SỐ: Cần truyền 'this' (Context) vào đây
            AchievementAdapter unlockedAdapter = new AchievementAdapter(unlockedList, this);
            rvUnlocked.setAdapter(unlockedAdapter);
        }

        if (rvLocked != null) {
            rvLocked.setLayoutManager(new GridLayoutManager(this, 2));
            // TRUYỀN THIẾU ĐỐI SỐ: Cần truyền 'this' (Context) vào đây
            AchievementAdapter lockedAdapter = new AchievementAdapter(lockedList, this);
            rvLocked.setAdapter(lockedAdapter);
        }

        // 5. Back button
        if (findViewById(R.id.btn_back_achieve) != null) {
            findViewById(R.id.btn_back_achieve).setOnClickListener(v -> finish());
        }
    }
}