package com.example.iq5.debug;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.core.network.ApiClient;
import com.example.iq5.core.network.UserApiService;
import com.example.iq5.core.prefs.PrefsManager;
import com.example.iq5.data.model.ApiResponse;
import com.example.iq5.data.model.UserProfileModel;
import com.example.iq5.feature.auth.ui.ApiSettingsActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Test activity for User Settings API
 */
public class TestSettingsActivity extends AppCompatActivity {

    private static final String TAG = "TestSettingsActivity";
    
    private TextView tvResult;
    private UserApiService userApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        createLayout();
        initApiService();
    }

    private void createLayout() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);
        layout.setBackgroundColor(0xFFF5F5F5);
        
        // Title
        TextView title = new TextView(this);
        title.setText("⚙️ TEST USER SETTINGS");
        title.setTextSize(20);
        title.setTextColor(0xFF333333);
        title.setPadding(0, 0, 0, 24);
        layout.addView(title);
        
        // Test buttons
        Button btnLoadSettings = new Button(this);
        btnLoadSettings.setText("📥 Load Current Settings");
        btnLoadSettings.setOnClickListener(v -> testLoadSettings());
        layout.addView(btnLoadSettings);
        
        Button btnSaveSettings = new Button(this);
        btnSaveSettings.setText("💾 Test Save Settings");
        btnSaveSettings.setOnClickListener(v -> testSaveSettings());
        layout.addView(btnSaveSettings);
        
        Button btnToggleSound = new Button(this);
        btnToggleSound.setText("🔊 Toggle Sound Setting");
        btnToggleSound.setOnClickListener(v -> testToggleSetting("sound"));
        layout.addView(btnToggleSound);
        
        Button btnToggleMusic = new Button(this);
        btnToggleMusic.setText("🎵 Toggle Music Setting");
        btnToggleMusic.setOnClickListener(v -> testToggleSetting("music"));
        layout.addView(btnToggleMusic);
        
        Button btnToggleNotifications = new Button(this);
        btnToggleNotifications.setText("🔔 Toggle Notifications");
        btnToggleNotifications.setOnClickListener(v -> testToggleSetting("notifications"));
        layout.addView(btnToggleNotifications);
        
        Button btnChangeLanguage = new Button(this);
        btnChangeLanguage.setText("🌐 Change Language");
        btnChangeLanguage.setOnClickListener(v -> testToggleSetting("language"));
        layout.addView(btnChangeLanguage);
        
        Button btnOpenSettingsActivity = new Button(this);
        btnOpenSettingsActivity.setText("🔗 Open Settings Activity");
        btnOpenSettingsActivity.setOnClickListener(v -> openSettingsActivity());
        layout.addView(btnOpenSettingsActivity);
        
        // Result
        tvResult = new TextView(this);
        tvResult.setText("Nhấn button để test...");
        tvResult.setTextSize(12);
        tvResult.setPadding(0, 24, 0, 0);
        tvResult.setTextColor(0xFF666666);
        layout.addView(tvResult);
        
        setContentView(layout);
    }

    private void initApiService() {
        try {
            PrefsManager prefsManager = new PrefsManager(this);
            Retrofit retrofit = ApiClient.getClient(prefsManager);
            userApiService = retrofit.create(UserApiService.class);
            
            updateResult("✅ API Service initialized");
            Log.d(TAG, "✅ API Service initialized");
        } catch (Exception e) {
            updateResult("❌ Failed to init API service: " + e.getMessage());
            Log.e(TAG, "❌ Failed to init API service", e);
        }
    }

    private void testLoadSettings() {
        updateResult("🔄 Loading current settings...");
        
        if (userApiService == null) {
            updateResult("❌ User API Service not initialized");
            return;
        }

        try {
            Call<UserProfileModel> call = userApiService.getMyProfile();
            
            call.enqueue(new Callback<UserProfileModel>() {
                @Override
                public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        UserProfileModel profile = response.body();
                        
                        String result = "✅ SETTINGS LOADED!\n\n";
                        
                        if (profile.getCaiDat() != null) {
                            UserProfileModel.CaiDatModel settings = profile.getCaiDat();
                            result += "🔊 Sound: " + (settings.isAmThanh() ? "ON" : "OFF") + "\n" +
                                    "🎵 Music: " + (settings.isNhacNen() ? "ON" : "OFF") + "\n" +
                                    "🔔 Notifications: " + (settings.isThongBao() ? "ON" : "OFF") + "\n" +
                                    "🌐 Language: " + settings.getNgonNgu() + "\n\n" +
                                    "Response code: " + response.code();
                        } else {
                            result += "⚠️ No settings found in profile.\n" +
                                    "Settings may not be created yet.\n\n" +
                                    "User: " + profile.getHoTen() + "\n" +
                                    "Email: " + profile.getEmail();
                        }
                        
                        updateResult(result);
                        Toast.makeText(TestSettingsActivity.this, 
                            "✅ Settings loaded", Toast.LENGTH_SHORT).show();
                        
                        Log.d(TAG, "✅ Settings loaded successfully");
                    } else {
                        handleApiError("Load Settings", response);
                    }
                }

                @Override
                public void onFailure(Call<UserProfileModel> call, Throwable t) {
                    handleNetworkError("Load Settings", t);
                }
            });
            
        } catch (Exception e) {
            updateResult("❌ Exception in load settings: " + e.getMessage());
            Log.e(TAG, "❌ Exception in load settings", e);
        }
    }

    private void testSaveSettings() {
        updateResult("🔄 Testing save settings...");
        
        if (userApiService == null) {
            updateResult("❌ User API Service not initialized");
            return;
        }

        try {
            // Test với settings mẫu
            UserApiService.UserSettingsModel request = new UserApiService.UserSettingsModel(
                true,   // AmThanh
                false,  // NhacNen  
                true,   // ThongBao
                "vi"    // NgonNgu
            );
            
            Call<ApiResponse> call = userApiService.updateSettings(request);
            
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse apiResponse = response.body();
                        String result = "✅ SAVE SETTINGS SUCCESS!\n\n" +
                                "Success: " + apiResponse.success + "\n" +
                                "Message: " + apiResponse.message + "\n" +
                                "Response code: " + response.code() + "\n\n" +
                                "Test settings applied:\n" +
                                "🔊 Sound: ON\n" +
                                "🎵 Music: OFF\n" +
                                "🔔 Notifications: ON\n" +
                                "🌐 Language: vi";
                        
                        updateResult(result);
                        Toast.makeText(TestSettingsActivity.this, 
                            "✅ " + apiResponse.message, Toast.LENGTH_SHORT).show();
                        
                        Log.d(TAG, "✅ Save settings success: " + apiResponse.message);
                    } else {
                        handleApiError("Save Settings", response);
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    handleNetworkError("Save Settings", t);
                }
            });
            
        } catch (Exception e) {
            updateResult("❌ Exception in save settings: " + e.getMessage());
            Log.e(TAG, "❌ Exception in save settings", e);
        }
    }

    private void testToggleSetting(String settingType) {
        updateResult("🔄 Testing toggle " + settingType + "...");
        
        UserApiService.UserSettingsModel request;
        
        switch (settingType) {
            case "sound":
                request = new UserApiService.UserSettingsModel(false, true, true, "vi");
                break;
            case "music":
                request = new UserApiService.UserSettingsModel(true, false, true, "vi");
                break;
            case "notifications":
                request = new UserApiService.UserSettingsModel(true, true, false, "vi");
                break;
            case "language":
                request = new UserApiService.UserSettingsModel(true, true, true, "en");
                break;
            default:
                request = new UserApiService.UserSettingsModel(true, true, true, "vi");
        }
        
        Call<ApiResponse> call = userApiService.updateSettings(request);
        
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    String result = "✅ TOGGLE " + settingType.toUpperCase() + " SUCCESS!\n\n" +
                            "Message: " + apiResponse.message + "\n" +
                            "Setting changed: " + settingType;
                    
                    updateResult(result);
                    Toast.makeText(TestSettingsActivity.this, 
                        "✅ " + settingType + " setting updated", Toast.LENGTH_SHORT).show();
                } else {
                    handleApiError("Toggle " + settingType, response);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                handleNetworkError("Toggle " + settingType, t);
            }
        });
    }

    private void openSettingsActivity() {
        try {
            Intent intent = new Intent(this, ApiSettingsActivity.class);
            startActivity(intent);
            Toast.makeText(this, "⚙️ Opening Settings Activity...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            updateResult("❌ Error opening Settings Activity: " + e.getMessage());
            Log.e(TAG, "❌ Error opening settings activity", e);
        }
    }

    private void handleApiError(String apiName, Response<?> response) {
        String result = "❌ " + apiName.toUpperCase() + " FAILED!\n\n" +
                "Response code: " + response.code() + "\n" +
                "Message: " + response.message() + "\n\n";
        
        if (response.code() == 401) {
            result += "❌ 401 UNAUTHORIZED\n" +
                    "Token không hợp lệ hoặc hết hạn.\n" +
                    "Hãy đăng nhập lại.";
        } else if (response.code() == 404) {
            result += "❌ 404 NOT FOUND\n" +
                    "Endpoint không tồn tại.\n" +
                    "Kiểm tra URL API.";
        }
        
        try {
            if (response.errorBody() != null) {
                result += "\nError body: " + response.errorBody().string();
            }
        } catch (Exception e) {
            result += "\nError reading error body: " + e.getMessage();
        }
        
        updateResult(result);
        Toast.makeText(this, "❌ " + apiName + " Error: " + response.code(), Toast.LENGTH_LONG).show();
        Log.e(TAG, "❌ " + apiName + " failed: " + response.code());
    }

    private void handleNetworkError(String apiName, Throwable t) {
        String result = "❌ " + apiName.toUpperCase() + " NETWORK ERROR!\n\n" +
                "Error: " + t.getMessage() + "\n" +
                "Type: " + t.getClass().getSimpleName() + "\n\n" +
                "Có thể:\n" +
                "- Backend chưa chạy (http://localhost:5048)\n" +
                "- Không có kết nối mạng\n" +
                "- URL sai (emulator cần 10.0.2.2)";
        
        updateResult(result);
        Toast.makeText(this, "❌ " + apiName + " Network Error", Toast.LENGTH_LONG).show();
        Log.e(TAG, "❌ " + apiName + " network error", t);
    }

    private void updateResult(String text) {
        runOnUiThread(() -> {
            if (tvResult != null) {
                tvResult.setText(text);
            }
        });
    }
}