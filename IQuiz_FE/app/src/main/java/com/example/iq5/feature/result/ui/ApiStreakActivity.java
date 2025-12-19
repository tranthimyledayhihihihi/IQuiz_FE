package com.example.iq5.feature.result.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.core.network.ApiClient;
import com.example.iq5.core.network.UserApiService;
import com.example.iq5.core.prefs.PrefsManager;
import android.view.View;
import android.view.ViewGroup;
import com.example.iq5.feature.result.model.StreakDay;
import com.example.iq5.data.model.UserStreakResponse;

import retrofit2.Retrofit;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * API Streak Activity - Lấy dữ liệu chuỗi ngày thật từ backend
 */
public class ApiStreakActivity extends AppCompatActivity {

    private static final String TAG = "ApiStreakActivity";
    
    private TextView tvCurrentDays;
    private TextView tvLastPlayed;
    private RecyclerView rvStreakHistory;
    
    private UserApiService userApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Tạo layout động thay vì dùng XML
        createDynamicLayout();
        initApiService();
        loadStreakData();
    }

    private void createDynamicLayout() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);
        layout.setBackgroundColor(0xFFF5F5F5);
        
        // Title
        TextView title = new TextView(this);
        title.setText("🔥 CHUỖI NGÀY CHƠI");
        title.setTextSize(24);
        title.setTextColor(0xFF333333);
        title.setPadding(0, 0, 0, 32);
        layout.addView(title);
        
        // Current Days
        tvCurrentDays = new TextView(this);
        tvCurrentDays.setText("0 NGÀY");
        tvCurrentDays.setTextSize(32);
        tvCurrentDays.setTextColor(0xFFFF5722);
        tvCurrentDays.setPadding(0, 0, 0, 16);
        layout.addView(tvCurrentDays);
        
        // Last Played
        tvLastPlayed = new TextView(this);
        tvLastPlayed.setText("Chưa có dữ liệu");
        tvLastPlayed.setTextSize(16);
        tvLastPlayed.setTextColor(0xFF666666);
        tvLastPlayed.setPadding(0, 0, 0, 32);
        layout.addView(tvLastPlayed);
        
        // RecyclerView
        rvStreakHistory = new RecyclerView(this);
        rvStreakHistory.setLayoutManager(new LinearLayoutManager(this));
        rvStreakHistory.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            0, 1.0f));
        layout.addView(rvStreakHistory);
        
        setContentView(layout);
    }

    private void initViews() {
        // Views đã được tạo trong createDynamicLayout()
    }

    private void initApiService() {
        // Sử dụng PrefsManager để tạo Retrofit client
        PrefsManager prefsManager = new PrefsManager(this);
        Retrofit retrofit = ApiClient.getClient(prefsManager);
        userApiService = retrofit.create(UserApiService.class);
    }

    private void loadStreakData() {
        Log.d(TAG, "🔄 Loading streak data from API...");
        
        // Call API để lấy streak data
        Call<UserStreakResponse> call = userApiService.getMyStreak();
        
        call.enqueue(new Callback<UserStreakResponse>() {
            @Override
            public void onResponse(Call<UserStreakResponse> call, Response<UserStreakResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Streak data loaded successfully");
                    displayStreakData(response.body());
                } else {
                    Log.e(TAG, "❌ Failed to load streak data: " + response.code());
                    showError("Không thể tải dữ liệu chuỗi ngày");
                }
            }

            @Override
            public void onFailure(Call<UserStreakResponse> call, Throwable t) {
                Log.e(TAG, "❌ Network error loading streak data", t);
                showError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    private void displayStreakData(UserStreakResponse streakData) {
        Log.d(TAG, "📊 Displaying streak data: " + streakData.getSoNgayLienTiep() + " days");
        
        // Hiển thị số ngày hiện tại
        if (tvCurrentDays != null) {
            tvCurrentDays.setText(streakData.getSoNgayLienTiep() + " NGÀY");
        }
        
        // Hiển thị ngày cập nhật cuối
        if (tvLastPlayed != null && streakData.getNgayCapNhatCuoi() != null) {
            tvLastPlayed.setText("Lần cuối: " + streakData.getNgayCapNhatCuoi());
        }
        
        // Tạo mock history data cho RecyclerView (vì API chưa có endpoint chi tiết)
        List<StreakDay> historyData = generateMockHistory(streakData.getSoNgayLienTiep());
        
        // Setup adapter đơn giản
        SimpleStreakAdapter adapter = new SimpleStreakAdapter(historyData);
        rvStreakHistory.setAdapter(adapter);
        
        Toast.makeText(this, "Chuỗi ngày: " + streakData.getSoNgayLienTiep() + " ngày", Toast.LENGTH_SHORT).show();
    }

    private List<StreakDay> generateMockHistory(int currentStreak) {
        List<StreakDay> history = new ArrayList<>();
        
        // Tạo 7 ngày gần nhất
        for (int i = 6; i >= 0; i--) {
            StreakDay day = new StreakDay();
            day.setDayNumber(7 - i);
            day.setDate("2025-12-" + String.format("%02d", 19 - i));
            day.setCompleted(i < currentStreak); // Những ngày trong streak sẽ completed
            history.add(day);
        }
        
        return history;
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        
        // Hiển thị dữ liệu mặc định
        if (tvCurrentDays != null) {
            tvCurrentDays.setText("0 NGÀY");
        }
        if (tvLastPlayed != null) {
            tvLastPlayed.setText("Chưa có dữ liệu");
        }
    }

    // Simple adapter class
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