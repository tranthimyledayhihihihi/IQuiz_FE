package com.example.iq5.debug;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.core.network.ApiClient;
import com.example.iq5.core.prefs.PrefsManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Test Activity để kiểm tra Achievement API không cần authentication
 */
public class TestAchievementApiActivity extends AppCompatActivity {

    private static final String TAG = "TestAchievementApi";
    private static final String BASE_URL = "http://10.0.2.2:5048"; // Android emulator
    
    private OkHttpClient httpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Sử dụng layout Android mặc định
        setContentView(android.R.layout.activity_list_item);

        initHttpClient();
        
        Toast.makeText(this, "🏆 Test Achievement API - Bắt đầu test...", 
            Toast.LENGTH_LONG).show();
        
        // Chạy các test theo thứ tự
        testAchievementDefinitions();
    }
    
    private void initHttpClient() {
        httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build();
    }
    
    /**
     * Test 1: Lấy tất cả achievement definitions
     */
    private void testAchievementDefinitions() {
        String url = BASE_URL + "/api/test/testachievement/definitions";
        
        Request request = new Request.Builder()
            .url(url)
            .get()
            .build();
        
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    Log.e(TAG, "❌ Test definitions failed: " + e.getMessage());
                    Toast.makeText(TestAchievementApiActivity.this, 
                        "❌ Không thể kết nối backend: " + e.getMessage(), 
                        Toast.LENGTH_LONG).show();
                });
            }
            
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                
                runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject json = new JSONObject(responseBody);
                            JSONArray definitions = json.getJSONArray("definitions");
                            
                            Log.d(TAG, "✅ Definitions loaded: " + definitions.length());
                            Toast.makeText(TestAchievementApiActivity.this, 
                                "✅ Tìm thấy " + definitions.length() + " achievement definitions", 
                                Toast.LENGTH_LONG).show();
                            
                            // Tiếp tục test user achievements
                            testUserAchievements();
                            
                        } catch (Exception e) {
                            Log.e(TAG, "❌ Parse definitions error: " + e.getMessage());
                            Toast.makeText(TestAchievementApiActivity.this, 
                                "❌ Lỗi parse JSON: " + e.getMessage(), 
                                Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.e(TAG, "❌ Definitions API error: " + response.code());
                        Toast.makeText(TestAchievementApiActivity.this, 
                            "❌ API lỗi: " + response.code(), 
                            Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
    
    /**
     * Test 2: Lấy achievements của user 1
     */
    private void testUserAchievements() {
        String url = BASE_URL + "/api/test/testachievement/user/1";
        
        Request request = new Request.Builder()
            .url(url)
            .get()
            .build();
        
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    Log.e(TAG, "❌ Test user achievements failed: " + e.getMessage());
                    Toast.makeText(TestAchievementApiActivity.this, 
                        "❌ Lỗi user achievements: " + e.getMessage(), 
                        Toast.LENGTH_LONG).show();
                });
            }
            
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                
                runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject json = new JSONObject(responseBody);
                            JSONArray achievements = json.getJSONArray("achievements");
                            
                            Log.d(TAG, "✅ User achievements loaded: " + achievements.length());
                            Toast.makeText(TestAchievementApiActivity.this, 
                                "✅ User có " + achievements.length() + " thành tựu", 
                                Toast.LENGTH_LONG).show();
                            
                            if (achievements.length() == 0) {
                                // Nếu chưa có thành tựu, tạo mẫu
                                createSampleAchievements();
                            } else {
                                // Hiển thị thành tựu đầu tiên
                                showFirstAchievement(achievements);
                                testAchievementStats();
                            }
                            
                        } catch (Exception e) {
                            Log.e(TAG, "❌ Parse user achievements error: " + e.getMessage());
                            Toast.makeText(TestAchievementApiActivity.this, 
                                "❌ Lỗi parse user achievements: " + e.getMessage(), 
                                Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.e(TAG, "❌ User achievements API error: " + response.code());
                        Toast.makeText(TestAchievementApiActivity.this, 
                            "❌ User achievements API lỗi: " + response.code(), 
                            Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
    
    /**
     * Test 3: Tạo achievements mẫu
     */
    private void createSampleAchievements() {
        String url = BASE_URL + "/api/test/testachievement/create-sample/1";
        
        Request request = new Request.Builder()
            .url(url)
            .post(okhttp3.RequestBody.create("", null))
            .build();
        
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    Log.e(TAG, "❌ Create sample failed: " + e.getMessage());
                    Toast.makeText(TestAchievementApiActivity.this, 
                        "❌ Lỗi tạo mẫu: " + e.getMessage(), 
                        Toast.LENGTH_LONG).show();
                });
            }
            
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                
                runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject json = new JSONObject(responseBody);
                            String message = json.getString("message");
                            
                            Log.d(TAG, "✅ Sample created: " + message);
                            Toast.makeText(TestAchievementApiActivity.this, 
                                "✅ " + message, 
                                Toast.LENGTH_LONG).show();
                            
                            // Test lại user achievements
                            testUserAchievements();
                            
                        } catch (Exception e) {
                            Log.e(TAG, "❌ Parse create sample error: " + e.getMessage());
                            Toast.makeText(TestAchievementApiActivity.this, 
                                "❌ Lỗi parse create sample: " + e.getMessage(), 
                                Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.e(TAG, "❌ Create sample API error: " + response.code());
                        Toast.makeText(TestAchievementApiActivity.this, 
                            "❌ Create sample API lỗi: " + response.code(), 
                            Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
    
    /**
     * Test 4: Lấy thống kê achievements
     */
    private void testAchievementStats() {
        String url = BASE_URL + "/api/test/testachievement/stats/1";
        
        Request request = new Request.Builder()
            .url(url)
            .get()
            .build();
        
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    Log.e(TAG, "❌ Test stats failed: " + e.getMessage());
                    Toast.makeText(TestAchievementApiActivity.this, 
                        "❌ Lỗi stats: " + e.getMessage(), 
                        Toast.LENGTH_LONG).show();
                });
            }
            
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                
                runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject json = new JSONObject(responseBody);
                            int unlocked = json.getInt("unlockedCount");
                            int total = json.getInt("totalCount");
                            int percentage = json.getInt("completionPercentage");
                            
                            Log.d(TAG, "✅ Stats loaded: " + unlocked + "/" + total + " (" + percentage + "%)");
                            Toast.makeText(TestAchievementApiActivity.this, 
                                "📊 Thống kê: " + unlocked + "/" + total + " (" + percentage + "%)", 
                                Toast.LENGTH_LONG).show();
                            
                        } catch (Exception e) {
                            Log.e(TAG, "❌ Parse stats error: " + e.getMessage());
                            Toast.makeText(TestAchievementApiActivity.this, 
                                "❌ Lỗi parse stats: " + e.getMessage(), 
                                Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.e(TAG, "❌ Stats API error: " + response.code());
                        Toast.makeText(TestAchievementApiActivity.this, 
                            "❌ Stats API lỗi: " + response.code(), 
                            Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
    
    /**
     * Hiển thị thành tựu đầu tiên
     */
    private void showFirstAchievement(JSONArray achievements) {
        try {
            if (achievements.length() > 0) {
                JSONObject first = achievements.getJSONObject(0);
                JSONObject definition = first.getJSONObject("Definition");
                String name = definition.getString("TenThanhTuu");
                String description = definition.getString("MoTa");
                
                Toast.makeText(this, 
                    "🏆 Thành tựu đầu tiên: " + name + " - " + description, 
                    Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "❌ Show first achievement error: " + e.getMessage());
        }
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "👋 Thoát Test Achievement API", Toast.LENGTH_SHORT).show();
    }
}