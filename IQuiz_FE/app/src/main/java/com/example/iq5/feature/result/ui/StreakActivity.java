package com.example.iq5.feature.result.ui;

import android.os.Bundle;
import android.view.View; // Cần thiết cho findViewById
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.iq5.R;
import com.example.iq5.core.navigation.NavigationHelper;
import android.view.ViewGroup;
import com.example.iq5.feature.result.data.ResultRepository;
import com.example.iq5.feature.result.model.StreakDay;
import java.util.List;

public class StreakActivity extends AppCompatActivity {

    private TextView tvCurrentDays;
    private RecyclerView rvStreakHistory;
    private ResultRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streak);

        // 1. Ánh xạ View
        tvCurrentDays = findViewById(R.id.tv_current_days);
        rvStreakHistory = findViewById(R.id.rv_streak_history);

        // 2. Khởi tạo Repository và lấy dữ liệu từ JSON
        repository = new ResultRepository(this);
        List<StreakDay> historyData = repository.getStreakHistory();

        // 3. Cập nhật số ngày Streak hiện tại từ Repository
        int currentStreak = repository.getCurrentStreakDays();
        if (tvCurrentDays != null) {
            tvCurrentDays.setText(currentStreak + " NGÀY");
        }

        // 4. Cấu hình RecyclerView cho lịch sử Streak với adapter đơn giản
        if (rvStreakHistory != null) {
            rvStreakHistory.setLayoutManager(new LinearLayoutManager(this));
            SimpleStreakAdapter adapter = new SimpleStreakAdapter(historyData);
            rvStreakHistory.setAdapter(adapter);
        }

        // 5. Xử lý nút Back - SỬ DỤNG finish() ĐỂ ĐẢM BẢO QUAY LẠI MÀN HÌNH TRƯỚC
        View btnBack = findViewById(R.id.btn_back_streak);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                // Đóng Activity hiện tại và quay lại Activity trước đó
                finish();
            });
        }
    }

    // Simple adapter class for mock data
    private static class SimpleStreakAdapter extends RecyclerView.Adapter<SimpleStreakAdapter.ViewHolder> {
        private List<StreakDay> data;

        public SimpleStreakAdapter(List<StreakDay> data) {
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new TextView(parent.getContext());
            textView.setPadding(16, 16, 16, 16);
            textView.setTextSize(16);
            return new ViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            StreakDay day = data.get(position);
            String status = day.isCompleted() ? "✅" : "❌";
            holder.textView.setText(status + " Ngày " + day.getDayNumber() + " - " + day.getDate());
            holder.textView.setTextColor(day.isCompleted() ? 0xFF4CAF50 : 0xFF757575);
        }

        @Override
        public int getItemCount() {
            return data != null ? data.size() : 0;
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView;
            }
        }
    }
}