package com.example.iq5.debug;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.core.prefs.PrefsManager;
import com.example.iq5.data.model.AchievementsResponse;
import com.example.iq5.data.repository.AchievementApiRepository;

import java.util.List;

public class DebugAchievementActivity extends AppCompatActivity {

    private static final String TAG = "DebugAchievement";

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
        createDebugLayout();

        prefsManager = new PrefsManager(this);
        achievementRepository = new AchievementApiRepository(this);

        setupButtons();
        showTokenInfo();
    }

    private void setupButtons() {
        btnCheckToken.setOnClickListener(v -> showTokenInfo());
        btnTestApi.setOnClickListener(v -> testAchievementApi());
        btnClearToken.setOnClickListener(v -> clearToken());
    }

    private void showTokenInfo() {
        String token = prefsManager.getToken();

        if (token != null && !token.isEmpty()) {
            tvTokenInfo.setText(
                    "🔑 TOKEN OK\n" +
                            "Length: " + token.length() + "\n" +
                            "Preview: " + token.substring(0, Math.min(40, token.length())) + "..."
            );
            Log.d(TAG, "Token OK");
        } else {
            tvTokenInfo.setText("❌ NO TOKEN – PLEASE LOGIN");
            Log.e(TAG, "No token found");
        }
    }

    private void testAchievementApi() {
        tvApiResult.setText("🔄 Calling Achievement API...");

        achievementRepository.getMyAchievements(
                new AchievementApiRepository.AchievementsCallback() {

                    @Override
                    public void onSuccess(List<AchievementsResponse.Achievement> list) {
                        runOnUiThread(() -> {
                            StringBuilder sb = new StringBuilder();
                            sb.append("✅ API SUCCESS\n");
                            sb.append("🏆 Total: ").append(list.size()).append("\n\n");

                            for (AchievementsResponse.Achievement a : list) {
                                sb.append("• ")
                                        .append(a.tenThanhTuu)
                                        .append(" | +")
                                        .append(a.diemThuong)
                                        .append("\n");
                            }

                            tvApiResult.setText(sb.toString());
                            Toast.makeText(DebugAchievementActivity.this,
                                    "API OK: " + list.size() + " achievements",
                                    Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onUnauthorized() {
                        runOnUiThread(() -> {
                            tvApiResult.setText("❌ UNAUTHORIZED (TOKEN EXPIRED)");
                            Toast.makeText(DebugAchievementActivity.this,
                                    "Token expired – login again",
                                    Toast.LENGTH_LONG).show();
                        });
                    }

                    @Override
                    public void onError(String error) {
                        runOnUiThread(() -> {
                            tvApiResult.setText("❌ ERROR: " + error);
                            Toast.makeText(DebugAchievementActivity.this,
                                    error,
                                    Toast.LENGTH_LONG).show();
                        });
                    }
                }
        );
    }

    private void clearToken() {
        prefsManager.clearToken();
        showTokenInfo();
        tvApiResult.setText("🗑️ Token cleared");
    }

    // =====================================================
    // SIMPLE DEBUG UI
    // =====================================================
    private void createDebugLayout() {
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);

        tvTokenInfo = new TextView(this);
        tvApiResult = new TextView(this);

        btnCheckToken = new Button(this);
        btnCheckToken.setText("Check Token");

        btnTestApi = new Button(this);
        btnTestApi.setText("Test Achievement API");

        btnClearToken = new Button(this);
        btnClearToken.setText("Clear Token");

        layout.addView(tvTokenInfo);
        layout.addView(btnCheckToken);
        layout.addView(btnTestApi);
        layout.addView(btnClearToken);
        layout.addView(tvApiResult);

        setContentView(layout);
    }
}
