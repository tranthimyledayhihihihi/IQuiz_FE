package com.example.iq5.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.iq5.core.network.ApiClient;
import com.example.iq5.core.network.QuizApiService;
import com.example.iq5.core.prefs.PrefsManager;
// import com.example.iq5.debug.ApiDebugActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Quick API test để kiểm tra nhanh API có hoạt động không
 */
public class QuickApiTest {
    private static final String TAG = "QuickApiTest";
    
    /**
     * Mở API Debug Activity
     */
    public static void openDebugActivity(Context context) {
        Toast.makeText(context, "Debug activity not available", Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(context, ApiDebugActivity.class);
        // context.startActivity(intent);
    }
    
    /**
     * Test nhanh API có hoạt động không
     */
    public static void quickTest(Context context) {
        Log.d(TAG, "🚀 Quick API Test Starting...");
        Toast.makeText(context, "🚀 Testing API...", Toast.LENGTH_SHORT).show();
        
        PrefsManager prefsManager = new PrefsManager(context);
        Retrofit retrofit = ApiClient.getClient(prefsManager);
        QuizApiService quizService = ApiClient.createService(retrofit, QuizApiService.class);
        
        // Test getIncorrectQuestions (không cần token)
        quizService.getIncorrectQuestions().enqueue(new Callback<QuizApiService.IncorrectQuestionsResponse>() {
            @Override
            public void onResponse(Call<QuizApiService.IncorrectQuestionsResponse> call, Response<QuizApiService.IncorrectQuestionsResponse> response) {
                String message;
                if (response.isSuccessful()) {
                    message = "✅ API Connection OK! Response: " + response.code();
                    Log.d(TAG, message);
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                } else if (response.code() == 401) {
                    message = "✅ API OK but needs authentication (401)";
                    Log.d(TAG, message);
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                } else {
                    message = "⚠️ API responded with: " + response.code();
                    Log.w(TAG, message);
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                }
            }
            
            @Override
            public void onFailure(Call<QuizApiService.IncorrectQuestionsResponse> call, Throwable t) {
                String message = "❌ API Failed: " + t.getMessage();
                Log.e(TAG, message);
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                
                // Gợi ý debug
                Toast.makeText(context, "💡 Tip: Check if backend is running on port 5048", 
                    Toast.LENGTH_LONG).show();
            }
        });
    }
    
    /**
     * Kiểm tra backend có đang chạy không
     */
    public static void checkBackendStatus(Context context) {
        Log.d(TAG, "🔍 Checking backend status...");
        
        PrefsManager prefsManager = new PrefsManager(context);
        Retrofit retrofit = ApiClient.getClient(prefsManager);
        QuizApiService quizService = ApiClient.createService(retrofit, QuizApiService.class);
        
        // Test với một endpoint đơn giản
        quizService.getIncorrectQuestions().enqueue(new Callback<QuizApiService.IncorrectQuestionsResponse>() {
            @Override
            public void onResponse(Call<QuizApiService.IncorrectQuestionsResponse> call, Response<QuizApiService.IncorrectQuestionsResponse> response) {
                String message = "🟢 Backend is running! Response: " + response.code();
                Log.d(TAG, message);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
            
            @Override
            public void onFailure(Call<QuizApiService.IncorrectQuestionsResponse> call, Throwable t) {
                String message = "🔴 Backend not reachable: " + t.getMessage();
                Log.e(TAG, message);
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                
                // Hiển thị hướng dẫn
                showBackendTroubleshooting(context);
            }
        });
    }
    
    private static void showBackendTroubleshooting(Context context) {
        String troubleshooting = 
            "🔧 Backend Troubleshooting:\n" +
            "1. Check if backend is running\n" +
            "2. Check port 5048 is accessible\n" +
            "3. For emulator: use 10.0.2.2:5048\n" +
            "4. For real device: use your PC's IP\n" +
            "5. Check firewall settings";
            
        Toast.makeText(context, troubleshooting, Toast.LENGTH_LONG).show();
        Log.d(TAG, troubleshooting);
    }
}