package com.example.iq5.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.iq5.core.network.ApiClient;
import com.example.iq5.core.network.QuizApiService;
import com.example.iq5.core.prefs.PrefsManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.List;

/**
 * Utility class to test backend connection and diagnose issues
 */
public class BackendConnectionTest {
    
    private static final String TAG = "BackendConnectionTest";
    
    public static void testConnection(Context context) {
        Log.d(TAG, "🔍 Starting backend connection test...");
        
        PrefsManager prefsManager = new PrefsManager(context);
        Retrofit retrofit = ApiClient.getClient(prefsManager);
        QuizApiService quizService = ApiClient.createService(retrofit, QuizApiService.class);
        
        Log.d(TAG, "🔗 Testing URL: " + ApiClient.getBaseUrl() + "chude/with-stats");
        
        quizService.getCategories().enqueue(new Callback<List<QuizApiService.CategoryResponse>>() {
            @Override
            public void onResponse(Call<List<QuizApiService.CategoryResponse>> call, Response<List<QuizApiService.CategoryResponse>> response) {
                String result = "📡 CONNECTION TEST RESULT:\n\n";
                result += "✅ Status: CONNECTED\n";
                result += "📊 HTTP Code: " + response.code() + "\n";
                result += "🔗 URL: " + call.request().url() + "\n";
                
                if (response.isSuccessful() && response.body() != null) {
                    List<QuizApiService.CategoryResponse> categories = response.body();
                    result += "📂 Categories Found: " + categories.size() + "\n\n";
                    
                    for (int i = 0; i < Math.min(categories.size(), 3); i++) {
                        QuizApiService.CategoryResponse cat = categories.get(i);
                        result += "• " + cat.getName() + " (ID: " + cat.getId() + ")\n";
                    }
                    
                    result += "\n🎉 BACKEND IS WORKING!";
                    Log.d(TAG, "✅ Backend connection successful!");
                } else {
                    result += "❌ Empty Response\n";
                    result += "📝 Message: " + response.message() + "\n";
                    result += "\n⚠️ Backend running but no data!";
                    Log.w(TAG, "⚠️ Backend connected but returned empty data");
                }
                
                Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                Log.d(TAG, result);
            }
            
            @Override
            public void onFailure(Call<List<QuizApiService.CategoryResponse>> call, Throwable t) {
                String result = "📡 CONNECTION TEST RESULT:\n\n";
                result += "❌ Status: FAILED\n";
                result += "🔗 URL: " + call.request().url() + "\n";
                result += "💥 Error: " + t.getMessage() + "\n";
                result += "🔧 Type: " + t.getClass().getSimpleName() + "\n\n";
                
                result += "🛠️ TROUBLESHOOTING:\n";
                result += "1. Start backend: dotnet run\n";
                result += "2. Check SQL Server running\n";
                result += "3. Verify port 5048 open\n";
                result += "4. Check emulator network\n\n";
                
                result += "📖 See: BACKEND_STARTUP_GUIDE.md";
                
                Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                Log.e(TAG, "❌ Backend connection failed: " + t.getMessage());
            }
        });
    }
    
    /**
     * Quick test method that returns result via callback
     */
    public static void quickTest(Context context, TestCallback callback) {
        PrefsManager prefsManager = new PrefsManager(context);
        Retrofit retrofit = ApiClient.getClient(prefsManager);
        QuizApiService quizService = ApiClient.createService(retrofit, QuizApiService.class);
        
        quizService.getCategories().enqueue(new Callback<List<QuizApiService.CategoryResponse>>() {
            @Override
            public void onResponse(Call<List<QuizApiService.CategoryResponse>> call, Response<List<QuizApiService.CategoryResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(true, "Backend connected! Found " + response.body().size() + " categories.");
                } else {
                    callback.onResult(false, "Backend connected but no data (HTTP " + response.code() + ")");
                }
            }
            
            @Override
            public void onFailure(Call<List<QuizApiService.CategoryResponse>> call, Throwable t) {
                callback.onResult(false, "Connection failed: " + t.getMessage());
            }
        });
    }
    
    public interface TestCallback {
        void onResult(boolean success, String message);
    }
}