package com.example.iq5.debug;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Test network connectivity to backend
 */
public class NetworkTestActivity extends AppCompatActivity {

    private static final String TAG = "NetworkTestActivity";
    private static final String BASE_URL = "http://10.0.2.2:5048";
    
    private TextView tvResult;
    private Button btnTestConnection;
    private Button btnTestAchievementEndpoint;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        createLayout();
        setupButtons();
    }
    
    private void createLayout() {
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);
        
        // Title
        TextView title = new TextView(this);
        title.setText("🌐 NETWORK TEST");
        title.setTextSize(18);
        title.setPadding(0, 0, 0, 16);
        layout.addView(title);
        
        // URL info
        TextView urlInfo = new TextView(this);
        urlInfo.setText("Backend URL: " + BASE_URL);
        urlInfo.setTextSize(12);
        urlInfo.setPadding(0, 0, 0, 16);
        layout.addView(urlInfo);
        
        // Test buttons
        btnTestConnection = new Button(this);
        btnTestConnection.setText("🔗 Test Basic Connection");
        layout.addView(btnTestConnection);
        
        btnTestAchievementEndpoint = new Button(this);
        btnTestAchievementEndpoint.setText("🏆 Test Achievement Endpoint");
        layout.addView(btnTestAchievementEndpoint);
        
        // Result
        tvResult = new TextView(this);
        tvResult.setText("Press buttons to test network...");
        tvResult.setTextSize(10);
        tvResult.setPadding(0, 16, 0, 0);
        layout.addView(tvResult);
        
        setContentView(layout);
    }
    
    private void setupButtons() {
        btnTestConnection.setOnClickListener(v -> testBasicConnection());
        btnTestAchievementEndpoint.setOnClickListener(v -> testAchievementEndpoint());
    }
    
    private void testBasicConnection() {
        tvResult.setText("🔄 Testing basic connection...");
        
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
            .url(BASE_URL + "/api/testquiz/categories")  // Public endpoint
            .build();
            
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    String result = "❌ CONNECTION FAILED!\n" +
                        "Error: " + e.getMessage() + "\n\n" +
                        "Possible causes:\n" +
                        "- Backend not running\n" +
                        "- Wrong URL (should be 10.0.2.2 for emulator)\n" +
                        "- Firewall blocking connection\n" +
                        "- Network permission missing";
                    
                    tvResult.setText(result);
                    Toast.makeText(NetworkTestActivity.this, 
                        "❌ Connection failed: " + e.getMessage(), 
                        Toast.LENGTH_LONG).show();
                    
                    Log.e(TAG, "Connection failed", e);
                });
            }
            
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body() != null ? response.body().string() : "No body";
                
                runOnUiThread(() -> {
                    String result = "✅ CONNECTION SUCCESS!\n" +
                        "Status: " + response.code() + " " + response.message() + "\n" +
                        "URL: " + request.url() + "\n" +
                        "Response length: " + responseBody.length() + " chars\n\n" +
                        "This means network is working!";
                    
                    tvResult.setText(result);
                    Toast.makeText(NetworkTestActivity.this, 
                        "✅ Connection OK: " + response.code(), 
                        Toast.LENGTH_SHORT).show();
                    
                    Log.d(TAG, "Connection success: " + response.code());
                });
            }
        });
    }
    
    private void testAchievementEndpoint() {
        tvResult.setText("🔄 Testing achievement endpoint (without token)...");
        
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
            .url(BASE_URL + "/api/user/achievement/me")
            .build();
            
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    String result = "❌ ENDPOINT FAILED!\n" +
                        "Error: " + e.getMessage() + "\n\n" +
                        "This means network issue or backend down.";
                    
                    tvResult.setText(result);
                    Toast.makeText(NetworkTestActivity.this, 
                        "❌ Endpoint failed: " + e.getMessage(), 
                        Toast.LENGTH_LONG).show();
                    
                    Log.e(TAG, "Endpoint failed", e);
                });
            }
            
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body() != null ? response.body().string() : "No body";
                
                runOnUiThread(() -> {
                    String result;
                    if (response.code() == 401) {
                        result = "✅ ENDPOINT WORKS!\n" +
                            "Status: 401 Unauthorized (Expected!)\n" +
                            "This means the endpoint exists and works.\n" +
                            "401 is correct because we didn't send token.\n\n" +
                            "Problem is likely in token handling.";
                    } else {
                        result = "🤔 UNEXPECTED RESPONSE!\n" +
                            "Status: " + response.code() + " " + response.message() + "\n" +
                            "Body: " + responseBody.substring(0, Math.min(200, responseBody.length()));
                    }
                    
                    tvResult.setText(result);
                    Toast.makeText(NetworkTestActivity.this, 
                        "Response: " + response.code(), 
                        Toast.LENGTH_SHORT).show();
                    
                    Log.d(TAG, "Endpoint response: " + response.code());
                });
            }
        });
    }
}