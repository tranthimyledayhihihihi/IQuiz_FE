package com.example.iq5.feature.auth.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

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
 * API Settings Activity - Lấy và cập nhật cài đặt thật từ backend
 */
public class ApiSettingsActivity extends AppCompatActivity {

    private static final String TAG = "ApiSettingsActivity";
    
    private SwitchCompat switchSound;
    private SwitchCompat switchBackgroundMusic;
    private SwitchCompat switchNotifications;
    private Spinner spinnerLanguage;
    private SeekBar seekVolume;
    private TextView tvVolumePercent;
    private Button btnSave;
    private Button btnBack;
    
    private UserApiService userApiService;
    private UserProfileModel.CaiDatModel currentSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        createDynamicLayout();
        initApiService();
        loadUserSettings();
    }

    private void createDynamicLayout() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);
        layout.setBackgroundColor(0xFFF5F5F5);
        
        // Title
        TextView title = new TextView(this);
        title.setText("⚙️ CÀI ĐẶT");
        title.setTextSize(24);
        title.setTextColor(0xFF333333);
        title.setPadding(0, 0, 0, 32);
        layout.addView(title);
        
        // Sound Switch
        switchSound = new SwitchCompat(this);
        switchSound.setText("🔊 Âm thanh");
        switchSound.setTextSize(16);
        switchSound.setPadding(0, 16, 0, 16);
        layout.addView(switchSound);
        
        // Background Music Switch
        switchBackgroundMusic = new SwitchCompat(this);
        switchBackgroundMusic.setText("🎵 Nhạc nền");
        switchBackgroundMusic.setTextSize(16);
        switchBackgroundMusic.setPadding(0, 16, 0, 16);
        layout.addView(switchBackgroundMusic);
        
        // Notifications Switch
        switchNotifications = new SwitchCompat(this);
        switchNotifications.setText("🔔 Thông báo");
        switchNotifications.setTextSize(16);
        switchNotifications.setPadding(0, 16, 0, 32);
        layout.addView(switchNotifications);
        
        // Language Label
        TextView langLabel = new TextView(this);
        langLabel.setText("🌐 Ngôn ngữ:");
        langLabel.setTextSize(16);
        langLabel.setTextColor(0xFF333333);
        langLabel.setPadding(0, 0, 0, 8);
        layout.addView(langLabel);
        
        // Language Spinner
        spinnerLanguage = new Spinner(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, 
            new String[]{"Tiếng Việt", "English"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(adapter);
        spinnerLanguage.setPadding(0, 0, 0, 32);
        layout.addView(spinnerLanguage);
        
        // Volume Label
        TextView volumeLabel = new TextView(this);
        volumeLabel.setText("🔊 Âm lượng:");
        volumeLabel.setTextSize(16);
        volumeLabel.setTextColor(0xFF333333);
        volumeLabel.setPadding(0, 0, 0, 8);
        layout.addView(volumeLabel);
        
        // Volume SeekBar
        seekVolume = new SeekBar(this);
        seekVolume.setMax(100);
        seekVolume.setProgress(70);
        seekVolume.setPadding(0, 0, 0, 8);
        layout.addView(seekVolume);
        
        // Volume Percent
        tvVolumePercent = new TextView(this);
        tvVolumePercent.setText("70%");
        tvVolumePercent.setTextSize(14);
        tvVolumePercent.setTextColor(0xFF666666);
        tvVolumePercent.setPadding(0, 0, 0, 32);
        layout.addView(tvVolumePercent);
        
        // Save Button
        btnSave = new Button(this);
        btnSave.setText("💾 Lưu cài đặt");
        btnSave.setTextSize(16);
        btnSave.setBackgroundColor(0xFF4CAF50);
        btnSave.setTextColor(0xFFFFFFFF);
        btnSave.setPadding(0, 16, 0, 16);
        btnSave.setOnClickListener(v -> saveSettings());
        layout.addView(btnSave);
        
        // Back Button
        btnBack = new Button(this);
        btnBack.setText("🔙 Quay lại");
        btnBack.setTextSize(16);
        btnBack.setBackgroundColor(0xFF2196F3);
        btnBack.setTextColor(0xFFFFFFFF);
        btnBack.setPadding(0, 16, 0, 16);
        LinearLayout.LayoutParams backParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT);
        backParams.topMargin = 16;
        btnBack.setLayoutParams(backParams);
        btnBack.setOnClickListener(v -> finish());
        layout.addView(btnBack);
        
        // Setup SeekBar listener
        seekVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvVolumePercent.setText(progress + "%");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        
        setContentView(layout);
    }

    private void initApiService() {
        PrefsManager prefsManager = new PrefsManager(this);
        Retrofit retrofit = ApiClient.getClient(prefsManager);
        userApiService = retrofit.create(UserApiService.class);
    }

    private void loadUserSettings() {
        Log.d(TAG, "🔄 Loading user settings from API...");
        
        Call<UserProfileModel> call = userApiService.getMyProfile();
        
        call.enqueue(new Callback<UserProfileModel>() {
            @Override
            public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Settings loaded successfully");
                    displaySettings(response.body());
                } else {
                    Log.e(TAG, "❌ Failed to load settings: " + response.code());
                    showError("Không thể tải cài đặt người dùng");
                }
            }

            @Override
            public void onFailure(Call<UserProfileModel> call, Throwable t) {
                Log.e(TAG, "❌ Network error loading settings", t);
                showError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    private void displaySettings(UserProfileModel profile) {
        Log.d(TAG, "📊 Displaying user settings");
        
        if (profile.getCaiDat() != null) {
            currentSettings = profile.getCaiDat();
            
            // Hiển thị cài đặt từ API
            switchSound.setChecked(currentSettings.isAmThanh());
            switchBackgroundMusic.setChecked(currentSettings.isNhacNen());
            switchNotifications.setChecked(currentSettings.isThongBao());
            
            // Set language
            if ("vi".equals(currentSettings.getNgonNgu())) {
                spinnerLanguage.setSelection(0);
            } else {
                spinnerLanguage.setSelection(1);
            }
            
            Toast.makeText(this, "Đã tải cài đặt từ server", Toast.LENGTH_SHORT).show();
        } else {
            // Nếu chưa có cài đặt, tạo mặc định
            currentSettings = new UserProfileModel.CaiDatModel();
            currentSettings.setAmThanh(true);
            currentSettings.setNhacNen(false);
            currentSettings.setThongBao(true);
            currentSettings.setNgonNgu("vi");
            
            Toast.makeText(this, "Sử dụng cài đặt mặc định", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveSettings() {
        Log.d(TAG, "💾 Saving settings to API...");
        
        // Tạo request từ UI
        UserApiService.UserSettingsModel request = new UserApiService.UserSettingsModel(
            switchSound.isChecked(),
            switchBackgroundMusic.isChecked(),
            switchNotifications.isChecked(),
            spinnerLanguage.getSelectedItemPosition() == 0 ? "vi" : "en"
        );
        
        Call<ApiResponse> call = userApiService.updateSettings(request);
        
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "✅ Settings saved successfully");
                    Toast.makeText(ApiSettingsActivity.this, "Đã lưu cài đặt thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "❌ Failed to save settings: " + response.code());
                    Toast.makeText(ApiSettingsActivity.this, "Lỗi khi lưu cài đặt", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e(TAG, "❌ Network error saving settings", t);
                Toast.makeText(ApiSettingsActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        
        // Hiển thị cài đặt mặc định khi lỗi
        switchSound.setChecked(true);
        switchBackgroundMusic.setChecked(false);
        switchNotifications.setChecked(true);
        spinnerLanguage.setSelection(0);
    }
}