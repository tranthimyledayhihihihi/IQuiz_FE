package com.example.iq5.utils;

import android.util.Log;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class SimpleNetworkTest {
    private static final String TAG = "SimpleNetworkTest";

    public static void testDirectConnection() {
        new Thread(() -> {
            try {
                Log.d(TAG, "🔄 Testing direct HTTP connection...");
                
                // Test với IP thực của máy tính
                String[] testUrls = {
                    "http://192.168.1.6:5048/swagger",
                    "http://10.0.2.2:5048/swagger",
                    "http://127.0.0.1:5048/swagger"
                };
                
                for (String testUrl : testUrls) {
                    try {
                        Log.d(TAG, "🌐 Testing URL: " + testUrl);
                        
                        URL url = new URL(testUrl);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(10000); // 10 seconds
                        connection.setReadTimeout(10000);
                        
                        int responseCode = connection.getResponseCode();
                        Log.d(TAG, "✅ Response code for " + testUrl + ": " + responseCode);
                        
                        if (responseCode == 200) {
                            Log.d(TAG, "🎉 SUCCESS! URL works: " + testUrl);
                            break;
                        }
                        
                        connection.disconnect();
                        
                    } catch (Exception e) {
                        Log.e(TAG, "❌ Failed for " + testUrl + ": " + e.getMessage());
                    }
                }
                
            } catch (Exception e) {
                Log.e(TAG, "💥 General error: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }
}