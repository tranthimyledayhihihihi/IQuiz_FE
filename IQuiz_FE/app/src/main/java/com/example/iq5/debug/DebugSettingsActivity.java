package com.example.iq5.debug;

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
import com.example.iq5.data.model.ApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Debug Settings Activity để kiểm tra tại sao không lưu được
 */
public class DebugSettingsActivity extends AppCompatActivity {

    private static final String TAG = "DebugSettingsActivity";

    private TextView tvDebugInfo;
    private UserApiService userApiService;
    private PrefsManager prefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createDebugLayout();
        initApiService();
    }

    private void createDebugLayout() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);

        // Title
        TextView title = new TextView(this);
        title.setText("🔍 DEBUG SETTINGS API");
        title.setTextSize(20);
        title.setPadding(0, 0, 0, 32);
        layout.addView(title);

        // Check Token Button
        Button btnCheckToken = new Button(this);
        btnCheckToken.setText("🔑 Check JWT Token");
        btnCheckToken.setOnClickListener(v -> checkToken());
        layout.addView(btnCheckToken);

        // Test Load Settings
        Button btnLoadSettings = new Button(this);
        btnLoadSettings.setText("📥 Test Load Settings");
        btnLoadSettings.setOnClickListener(v -> testLoadSettings());
        layout.addView(btnLoadSettings);

        // Test Save Settings
        Button btnSaveSettings = new Button(this);
        btnSaveSettings.setText("💾 Test Save Settings");
        btnSaveSettings.setOnClickListener(v -> testSaveSettings());
        layout.addView(btnSaveSettings);

        // Debug Info
        tvDebugInfo = new TextView(this);
        tvDebugInfo.setText("Nhấn nút để debug...");
        tvDebugInfo.setPadding(0, 32, 0, 0);
        tvDebugInfo.setTextSize(12);
        layout.addView(tvDebugInfo);

        setContentView(layout);
    }

    private void initApiService() {
        prefsManager = new PrefsManager(this);
        Retrofit retrofit = ApiClient.getClient(prefsManager);
        userApiService = retrofit.create(UserApiService.class);
    }

    private void checkToken() {
        String token = prefsManager.getAuthToken();
        String info = "🔑 JWT TOKEN CHECK:\n\n";

        if (token != null && !token.isEmpty()) {
            info += "✅ Token exists\n";
            info += "📏 Length: " + token.length() + "\n";
            info += "🔤 First 20 chars: " + token.substring(0, Math.min(20, token.length())) + "...\n";
            info += "🌐 Base URL: " + ApiClient.getBaseUrl() + "\n\n";
            info += "🎯 Status: READY TO CALL API";
        } else {
            info += "❌ No token found!\n";
            info += "🔧 Need to login first\n\n";
            info += "🎯 Status: NEED AUTHENTICATION";
        }

        tvDebugInfo.setText(info);
        Log.d(TAG, info);
    }

    private void testLoadSettings() {
        tvDebugInfo.setText("🔄 Testing load settings...");
        Log.d(TAG, "🧪 Testing load settings API");

        Call<UserProfileModel> call = userApiService.getMyProfile();

        call.enqueue(new Callback<UserProfileModel>() {
            @Override
            public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {
                String result = "📥 LOAD SETTINGS RESULT:\n\n";
                result += "📊 Response Code: " + response.code() + "\n";
                result += "📨 Message: " + response.message() + "\n\n";

                if (response.isSuccessful() && response.body() != null) {
                    UserProfileModel profile = response.body();
                    result += "✅ SUCCESS!\n";
                    result += "👤 User: " + profile.getHoTen() + "\n";
                    result += "📧 Email: " + profile.getEmail() + "\n";

                    if (profile.getCaiDat() != null) {
                        UserProfileModel.CaiDatModel settings = profile.getCaiDat();
                        result += "\n⚙️ CURRENT SETTINGS:\n";
                        result += "🔊 Sound: " + settings.isAmThanh() + "\n";
                        result += "🎵 Music: " + settings.isNhacNen() + "\n";
                        result += "🔔 Notifications: " + settings.isThongBao() + "\n";
                        result += "🌐 Language: " + settings.getNgonNgu() + "\n";
                    } else {
                        result += "\n⚠️ No settings found in profile";
                    }
                } else {
                    result += "❌ FAILED!\n";
                    result += "🔧 Check authentication or backend";
                }

                tvDebugInfo.setText(result);
                Log.d(TAG, result);
            }

            @Override
            public void onFailure(Call<UserProfileModel> call, Throwable t) {
                String error = "❌ LOAD SETTINGS FAILED:\n\n";
                error += "🚫 Error: " + t.getMessage() + "\n";
                error += "🔧 Check network or backend\n";
                error += "🌐 URL: " + ApiClient.getBaseUrl();

                tvDebugInfo.setText(error);
                Log.e(TAG, error, t);
            }
        });
    }

    private void testSaveSettings() {
        tvDebugInfo.setText("🔄 Testing save settings...");
        Log.d(TAG, "🧪 Testing save settings API");

        // Tạo test settings
        UserApiService.UserSettingsModel testSettings = new UserApiService.UserSettingsModel(
            true,   // amThanh
            false,  // nhacNen
            true,   // thongBao
            "vi"    // ngonNgu
        );

        Call<ApiResponse> call = userApiService.updateSettings(testSettings);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                String result = "💾 SAVE SETTINGS RESULT:\n\n";
                result += "📊 Response Code: " + response.code() + "\n";
                result += "📨 Message: " + response.message() + "\n\n";

                if (response.isSuccessful()) {
                    result += "✅ SUCCESS!\n";
                    result += "💾 Settings saved to database\n";
                    result += "🎯 Test data:\n";
                    result += "  - Sound: true\n";
                    result += "  - Music: false\n";
                    result += "  - Notifications: true\n";
                    result += "  - Language: vi\n\n";
                    result += "🔄 Try loading again to verify";
                } else {
                    result += "❌ FAILED!\n";
                    if (response.code() == 401) {
                        result += "🔐 Authentication required\n";
                        result += "🔧 Need to login first";
                    } else if (response.code() == 400) {
                        result += "📝 Bad request - check data format";
                    } else {
                        result += "🔧 Server error - check backend";
                    }
                }

                tvDebugInfo.setText(result);
                Log.d(TAG, result);
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                String error = "❌ SAVE SETTINGS FAILED:\n\n";
                error += "🚫 Error: " + t.getMessage() + "\n";
                error += "🔧 Possible causes:\n";
                error += "  - Backend not running\n";
                error += "  - Network connection issue\n";
                error += "  - Wrong API endpoint\n";
                error += "🌐 URL: " + ApiClient.getBaseUrl();

                tvDebugInfo.setText(error);
                Log.e(TAG, error, t);
            }
        });
    }
}