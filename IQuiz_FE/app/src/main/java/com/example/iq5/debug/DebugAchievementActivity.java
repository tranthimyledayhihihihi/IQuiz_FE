package com.example.iq5.debug;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.R;
import com.example.iq5.core.network.AchievementApiService;
import com.example.iq5.core.prefs.PrefsManager;
import com.example.iq5.data.repository.AchievementApiRepository;

import java.util.List;

/**
 * Debug Activity để test Achievement API và token
 */
public class DebugAchievementActivity extends AppCompatActivity {

    private static final String TAG = "DebugAchievementActivity";
    
    private TextView tvTokenInfo;
    private TextView tvApiResult;
    private Button btnCheckToken;
    private Button btnTestApi;
    private Button btnClearToken;
    
    private PrefsManager prefsManager;
    private AchievementApiRepository achievementRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Tạo layout đơn giản
        createDebugLayout();
        
        // Initialize
        prefsManager = new PrefsManager(this);
        achievementRepository = new AchievementApiRepository(this);
        
        // Setup buttons
        setupButtons();
        
        // Show initial token info
        showTokenInfo();
    }
    
    private void createDebugLayout() {
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);
        
        // Title
        TextView title = new TextView(this);
        title.setText("🔧 DEBUG ACHIEVEMENT API");
        title.setTextSize(18);
        title.setPadding(0, 0, 0, 16);
        layout.addView(title);
        
        // Token info
        tvTokenInfo = new TextView(this);
        tvTokenInfo.setText("Token info loading...");
        tvTokenInfo.setTextSize(12);
        tvTokenInfo.setPadding(0, 0, 0, 16);
        layout.addView(tvTokenInfo);
        
        // Buttons
        btnCheckToken = new Button(this);
        btnCheckToken.setText("🔍 Check Token");
        layout.addView(btnCheckToken);
        
        btnTestApi = new Button(this);
        btnTestApi.setText("🏆 Test Achievement API");
        layout.addView(btnTestApi);
        
        btnClearToken = new Button(this);
        btnClearToken.setText("🗑️ Clear Token");
        layout.addView(btnClearToken);
        
        // API result
        tvApiResult = new TextView(this);
        tvApiResult.setText("API result will appear here...");
        tvApiResult.setTextSize(10);
        tvApiResult.setPadding(0, 16, 0, 0);
        layout.addView(tvApiResult);
        
        setContentView(layout);
    }
    
    private void setupButtons() {
        btnCheckToken.setOnClickListener(v -> showTokenInfo());
        btnTestApi.setOnClickListener(v -> testAchievementApi());
        btnClearToken.setOnClickListener(v -> clearToken());
    }
    
    private void showTokenInfo() {
        String token = prefsManager.getAuthToken();
        String role = prefsManager.getUserRole();
        
        if (token != null) {
            // Decode JWT để xem thông tin (đơn giản)
            String[] parts = token.split("\\.");
            String info = String.format(
                "🔑 TOKEN INFO:\n" +
                "✅ Token exists: YES\n" +
                "📏 Length: %d chars\n" +
                "👤 Role: %s\n" +
                "🔧 Parts: %d\n" +
                "📝 Preview: %s...",
                token.length(),
                role != null ? role : "unknown",
                parts.length,
                token.length() > 50 ? token.substring(0, 50) : token
            );
            tvTokenInfo.setText(info);
            
            Log.d(TAG, "Token exists: " + token.substring(0, Math.min(50, token.length())) + "...");
        } else {
            tvTokenInfo.setText("❌ TOKEN INFO:\n❌ No token found!\n🔄 Please login first.");
            Log.w(TAG, "No token found in PrefsManager");
        }
    }
    
    private void testAchievementApi() {
        tvApiResult.setText("🔄 Testing Achievement API...");
        
        Log.d(TAG, "🏆 Testing Achievement API...");
        
        achievementRepository.getMyAchievements(new AchievementApiRepository.AchievementsCallback() {
            @Override
            public void onSuccess(List<AchievementApiService.Achievement> achievements) {
                runOnUiThread(() -> {
                    String result = String.format(
                        "✅ API SUCCESS!\n" +
                        "🏆 Achievements: %d\n" +
                        "📊 Details:\n",
                        achievements.size()
                    );
                    
                    for (int i = 0; i < Math.min(3, achievements.size()); i++) {
                        AchievementApiService.Achievement ach = achievements.get(i);
                        result += String.format("  %d. %s (%s)\n", 
                            i+1, ach.getTenThanhTuu(), ach.isUnlocked() ? "✅" : "🔒");
                    }
                    
                    if (achievements.size() > 3) {
                        result += String.format("  ... and %d more\n", achievements.size() - 3);
                    }
                    
                    tvApiResult.setText(result);
                    Toast.makeText(DebugAchievementActivity.this, 
                        "✅ API Success: " + achievements.size() + " achievements", 
                        Toast.LENGTH_SHORT).show();
                    
                    Log.d(TAG, "✅ API Success: " + achievements.size() + " achievements");
                });
            }
            
            @Override
            public void onUnauthorized() {
                runOnUiThread(() -> {
                    String result = "❌ API UNAUTHORIZED!\n" +
                        "🔑 Token may be expired or invalid\n" +
                        "💡 Try logging in again\n" +
                        "🔧 Check backend logs for details";
                    
                    tvApiResult.setText(result);
                    Toast.makeText(DebugAchievementActivity.this, 
                        "❌ Unauthorized - Token expired", Toast.LENGTH_LONG).show();
                    
                    Log.e(TAG, "❌ API Unauthorized - Token expired or invalid");
                    
                    // Show current token for debugging
                    showTokenInfo();
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    String result = String.format(
                        "❌ API ERROR!\n" +
                        "🔥 Error: %s\n" +
                        "🔧 Check:\n" +
                        "  - Backend running?\n" +
                        "  - Network connection?\n" +
                        "  - API endpoint correct?",
                        error
                    );
                    
                    tvApiResult.setText(result);
                    Toast.makeText(DebugAchievementActivity.this, 
                        "❌ API Error: " + error, Toast.LENGTH_LONG).show();
                    
                    Log.e(TAG, "❌ API Error: " + error);
                });
            }
        });
    }
    
    private void clearToken() {
        prefsManager.clearAuthToken();
        showTokenInfo();
        tvApiResult.setText("🗑️ Token cleared! Please login again.");
        Toast.makeText(this, "Token cleared", Toast.LENGTH_SHORT).show();
        
        Log.d(TAG, "Token cleared");
    }
}