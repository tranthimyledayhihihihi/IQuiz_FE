package com.example.iq5.debug;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.R;
import com.example.iq5.core.network.ApiClient;
import com.example.iq5.core.network.UserApiService;
import com.example.iq5.core.prefs.PrefsManager;
import com.example.iq5.data.model.UserStreakResponse;
import com.example.iq5.feature.result.ui.ApiStreakActivity;
import com.example.iq5.feature.result.ui.StreakActivity;

import retrofit2.Retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Test Activity để kiểm tra API Streak
 */
public class TestStreakApiActivity extends AppCompatActivity {

    private static final String TAG = "TestStreakApiActivity";
    
    private TextView tvResult;
    private UserApiService userApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Tạo layout động
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);
        
        // Title
        TextView title = new TextView(this);
        title.setText("🧪 TEST STREAK API");
        title.setTextSize(20);
        title.setPadding(0, 0, 0, 32);
        layout.addView(title);
        
        // Test API Button
        Button btnTestApi = new Button(this);
        btnTestApi.setText("🔥 Test Streak API");
        btnTestApi.setOnClickListener(v -> testStreakApi());
        layout.addView(btnTestApi);
        
        // Open Old Streak Activity
        Button btnOldStreak = new Button(this);
        btnOldStreak.setText("📂 Old Streak Activity (Mock Data)");
        btnOldStreak.setOnClickListener(v -> {
            Intent intent = new Intent(this, StreakActivity.class);
            startActivity(intent);
        });
        layout.addView(btnOldStreak);
        
        // Open New API Streak Activity
        Button btnNewStreak = new Button(this);
        btnNewStreak.setText("🔗 New API Streak Activity");
        btnNewStreak.setOnClickListener(v -> {
            Intent intent = new Intent(this, ApiStreakActivity.class);
            startActivity(intent);
        });
        layout.addView(btnNewStreak);
        
        // Result TextView
        tvResult = new TextView(this);
        tvResult.setText("Nhấn nút để test API...");
        tvResult.setPadding(0, 32, 0, 0);
        layout.addView(tvResult);
        
        setContentView(layout);
        
        // Init API service
        PrefsManager prefsManager = new PrefsManager(this);
        Retrofit retrofit = ApiClient.getClient(prefsManager);
        userApiService = retrofit.create(UserApiService.class);
    }

    private void testStreakApi() {
        Log.d(TAG, "🧪 Testing Streak API...");
        tvResult.setText("🔄 Đang test API...");
        
        Call<UserStreakResponse> call = userApiService.getMyStreak();
        
        call.enqueue(new Callback<UserStreakResponse>() {
            @Override
            public void onResponse(Call<UserStreakResponse> call, Response<UserStreakResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserStreakResponse streak = response.body();
                    
                    String result = "✅ API SUCCESS!\n\n" +
                            "📊 Dữ liệu nhận được:\n" +
                            "• Số ngày liên tiếp: " + streak.getSoNgayLienTiep() + "\n" +
                            "• Ngày cập nhật cuối: " + streak.getNgayCapNhatCuoi() + "\n\n" +
                            "🎯 Kết luận: API hoạt động tốt!";
                    
                    tvResult.setText(result);
                    
                    Log.d(TAG, "✅ Streak API test successful: " + streak.getSoNgayLienTiep() + " days");
                    Toast.makeText(TestStreakApiActivity.this, 
                            "Chuỗi ngày: " + streak.getSoNgayLienTiep() + " ngày", 
                            Toast.LENGTH_SHORT).show();
                    
                } else {
                    String error = "❌ API FAILED!\n\n" +
                            "• Response code: " + response.code() + "\n" +
                            "• Message: " + response.message() + "\n\n" +
                            "🔧 Có thể cần đăng nhập trước";
                    
                    tvResult.setText(error);
                    Log.e(TAG, "❌ Streak API test failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserStreakResponse> call, Throwable t) {
                String error = "❌ NETWORK ERROR!\n\n" +
                        "• Error: " + t.getMessage() + "\n\n" +
                        "🔧 Kiểm tra:\n" +
                        "- Backend có chạy không?\n" +
                        "- URL có đúng không?\n" +
                        "- Kết nối mạng?";
                
                tvResult.setText(error);
                Log.e(TAG, "❌ Network error testing Streak API", t);
                Toast.makeText(TestStreakApiActivity.this, 
                        "Lỗi kết nối: " + t.getMessage(), 
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}