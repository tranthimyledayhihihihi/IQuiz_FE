package com.example.iq5.debug;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.core.prefs.PrefsManager;
import com.example.iq5.data.api.ApiService;
import com.example.iq5.data.api.RetrofitClient;
import com.example.iq5.data.model.DailyRewardClaimResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestRewardAndStatsActivity extends AppCompatActivity {

    private static final String TAG = "TestRewardAndStats";

    private TextView tvResult;

    private ApiService api;
    private PrefsManager prefs;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = new PrefsManager(this);
        token = prefs.getToken();

        createLayout();
        initApi();
    }

    // ================= UI =================

    private void createLayout() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);

        Button btnClaim = new Button(this);
        btnClaim.setText("🎁 Claim Daily Reward (Achievement)");
        btnClaim.setOnClickListener(v -> testClaimReward());
        layout.addView(btnClaim);

        tvResult = new TextView(this);
        tvResult.setPadding(0, 32, 0, 0);
        layout.addView(tvResult);

        setContentView(layout);
    }

    // ================= API INIT =================

    private void initApi() {
        api = RetrofitClient.getApiService();
        log("✅ ApiService initialized");
    }

    // ================= TEST =================

    private void testClaimReward() {

        if (token == null || token.isEmpty()) {
            log("❌ Chưa đăng nhập (token null)");
            return;
        }

        log("🎁 Calling POST /api/user/achievement/daily-reward");

        api.claimDailyReward("Bearer " + token)
                .enqueue(new Callback<DailyRewardClaimResponse>() {
                    @Override
                    public void onResponse(
                            Call<DailyRewardClaimResponse> call,
                            Response<DailyRewardClaimResponse> response) {

                        if (!response.isSuccessful() || response.body() == null) {
                            log("❌ Claim failed: HTTP " + response.code());
                            return;
                        }

                        DailyRewardClaimResponse res = response.body();

                        // ✅ FIX: KHÔNG CÓ reward
                        log("✅ RESPONSE\n" +
                                "awarded = " + res.awarded + "\n" +
                                "message = " + res.message);
                    }

                    @Override
                    public void onFailure(
                            Call<DailyRewardClaimResponse> call,
                            Throwable t) {
                        log("❌ Network error: " + t.getMessage());
                    }
                });
    }

    // ================= UTILS =================

    private void log(String msg) {
        Log.d(TAG, msg);
        runOnUiThread(() -> tvResult.setText(msg));
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
