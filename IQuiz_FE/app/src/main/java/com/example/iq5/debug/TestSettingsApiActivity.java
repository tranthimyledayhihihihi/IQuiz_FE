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
import com.example.iq5.data.model.UserProfileModel;
import com.example.iq5.feature.auth.ui.ApiSettingsActivity;
import com.example.iq5.feature.auth.ui.SettingsActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Test Activity để kiểm tra API Settings
 */
public class TestSettingsApiActivity extends AppCompatActivity {

    private static final String TAG = "TestSettingsApiActivity";
    
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
        title.setText("🧪 TEST SETTINGS API");
        title.setTextSize(20);
        title.setPadding(0, 0, 0, 32);
        layout.addView(title);
        
        // Test API Button
        Button btnTestApi = new Button(this);
        btnTestApi.setText("⚙️ Test Settings API");
        btnTestApi.setOnClickListener(v -> testSettingsApi());
        layout.addView(btnTestApi);
        
        // Open Old Settings Activity
        Button btnOldSettings = new Button(this);
        btnOldSettings.setText("📂 Old Settings Activity (Mock Data)");
        btnOldSettings.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
        layout.addView(btnOldSettings);
        
        // Open New API Settings Activity
        Button btnNewSettings = new Button(this);
        btnNewSettings.setText("🔗 New API Settings Activity");
        btnNewSettings.setOnClickListener(v -> {
            Intent intent = new Intent(this, ApiSettingsActivity.class);
            startActivity(intent);
        });
        layout.addView(btnNewSettings);
        
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

    private void testSettingsApi() {
        Log.d(TAG, "🧪 Testing Settings API...");
        tvResult.setText("🔄 Đang test API...");
        
        Call<UserProfileModel> call = userApiService.getMyProfile();
        
        call.enqueue(new Callback<UserProfileModel>() {
            @Override
            public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserProfileModel profile = response.body();
                    
                    String result = "✅ API SUCCESS!\n\n" +
                            "📊 Thông tin người dùng:\n" +
                            "• Tên: " + profile.getHoTen() + "\n" +
                            "• Email: " + profile.getEmail() + "\n" +
                            "• Vai trò: " + profile.getVaiTro() + "\n\n";
                    
                    if (profile.getCaiDat() != null) {
                        UserProfileModel.CaiDatModel settings = profile.getCaiDat();
                        result += "⚙️ Cài đặt hiện tại:\n" +
                                "• Âm thanh: " + (settings.isAmThanh() ? "Bật" : "Tắt") + "\n" +
                                "• Nhạc nền: " + (settings.isNhacNen() ? "Bật" : "Tắt") + "\n" +
                                "• Thông báo: " + (settings.isThongBao() ? "Bật" : "Tắt") + "\n" +
                                "• Ngôn ngữ: " + settings.getNgonNgu() + "\n\n";
                    } else {
                        result += "⚠️ Chưa có cài đặt người dùng\n\n";
                    }
                    
                    result += "🎯 Kết luận: API hoạt động tốt!";
                    
                    tvResult.setText(result);
                    
                    Log.d(TAG, "✅ Settings API test successful");
                    Toast.makeText(TestSettingsApiActivity.this, 
                            "API Settings hoạt động tốt!", 
                            Toast.LENGTH_SHORT).show();
                    
                } else {
                    String error = "❌ API FAILED!\n\n" +
                            "• Response code: " + response.code() + "\n" +
                            "• Message: " + response.message() + "\n\n" +
                            "🔧 Có thể cần đăng nhập trước";
                    
                    tvResult.setText(error);
                    Log.e(TAG, "❌ Settings API test failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserProfileModel> call, Throwable t) {
                String error = "❌ NETWORK ERROR!\n\n" +
                        "• Error: " + t.getMessage() + "\n\n" +
                        "🔧 Kiểm tra:\n" +
                        "- Backend có chạy không?\n" +
                        "- URL có đúng không?\n" +
                        "- Kết nối mạng?";
                
                tvResult.setText(error);
                Log.e(TAG, "❌ Network error testing Settings API", t);
                Toast.makeText(TestSettingsApiActivity.this, 
                        "Lỗi kết nối: " + t.getMessage(), 
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}