package com.example.iq5.feature.quiz.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ProgressBar;
import android.util.Log;
import android.view.View;

import com.example.iq5.R;
import com.example.iq5.core.navigation.NavigationHelper;
import com.example.iq5.feature.quiz.adapter.CategoryAdapter;
import com.example.iq5.feature.quiz.model.Category;
import com.example.iq5.feature.quiz.adapter.DifficultyAdapter;
import com.example.iq5.feature.quiz.model.Difficulty;

// Import API classes
import com.example.iq5.core.network.ApiClient;
import com.example.iq5.core.network.QuizApiService;
import com.example.iq5.core.prefs.PrefsManager;
import com.example.iq5.utils.QuickApiTest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.ArrayList;
import java.util.List;

/**
 * SelectCategoryActivity sử dụng API thật từ backend
 */
public class ApiSelectCategoryActivity extends AppCompatActivity {

    private static final String TAG = "ApiSelectCategoryActivity";

    private int selectedCategoryId = -1;
    private String selectedDifficultyId = null;

    private RecyclerView rvCategory;
    private RecyclerView rvDifficulty;
    private Button btnStartQuiz;
    private ProgressBar progressBar;

    // API components
    private PrefsManager prefsManager;
    private QuizApiService quizService;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_select_category);

        initViews();
        initApiComponents();
        loadCategoriesFromApi();

        btnStartQuiz.setOnClickListener(v -> handleStartQuiz());
    }

    private void initViews() {
        rvCategory = findViewById(R.id.rvCategory);
        rvDifficulty = findViewById(R.id.rvDifficulty);
        btnStartQuiz = findViewById(R.id.btnStartQuiz);
        progressBar = findViewById(R.id.progressBar);

        if (rvCategory == null || rvDifficulty == null || btnStartQuiz == null) {
            Toast.makeText(this, "Lỗi layout: thiếu ID rvCategory/rvDifficulty/btnStartQuiz", Toast.LENGTH_LONG).show();
            finish();
        }
        
        // Add long click listener for connection test
        btnStartQuiz.setOnLongClickListener(v -> {
            com.example.iq5.utils.BackendConnectionTest.testConnection(this);
            return true;
        });
    }

    private void initApiComponents() {
        prefsManager = new PrefsManager(this);
        Retrofit retrofit = ApiClient.getClient(prefsManager);
        quizService = ApiClient.createService(retrofit, QuizApiService.class);
    }

    private void handleStartQuiz() {
        if (selectedCategoryId == -1) {
            Toast.makeText(this, "Vui lòng chọn một danh mục!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedDifficultyId == null) {
            Toast.makeText(this, "Vui lòng chọn độ khó!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gọi API để lấy câu hỏi theo category
        startQuizWithCategory(selectedCategoryId);
    }
    
    /**
     * Bắt đầu quiz bằng cách lấy câu hỏi từ API
     */
    private void startQuizWithCategory(int categoryId) {
        showLoading(true);
        
        Log.d(TAG, "🚀 Starting quiz for category: " + categoryId);
        
        quizService.getQuestionsByCategory(categoryId).enqueue(new Callback<QuizApiService.TestQuizResponse>() {
            @Override
            public void onResponse(Call<QuizApiService.TestQuizResponse> call, Response<QuizApiService.TestQuizResponse> response) {
                showLoading(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    QuizApiService.TestQuizResponse result = response.body();
                    
                    if (result.isSuccess() && result.getQuestions() != null && !result.getQuestions().isEmpty()) {
                        Log.d(TAG, "✅ Got " + result.getQuestions().size() + " questions for category " + categoryId);
                        
                        // Chuyển sang ApiQuizActivity với danh sách câu hỏi
                        NavigationHelper.navigateToApiQuizWithQuestions(
                            ApiSelectCategoryActivity.this, 
                            result.getQuestions(),
                            result.getQuestions().get(0).getCategoryName()
                        );
                        
                        Toast.makeText(ApiSelectCategoryActivity.this, 
                            "✅ Bắt đầu quiz với " + result.getQuestions().size() + " câu hỏi!", 
                            Toast.LENGTH_SHORT).show();
                            
                    } else {
                        Log.e(TAG, "❌ No questions found for category " + categoryId);
                        Toast.makeText(ApiSelectCategoryActivity.this, 
                            "❌ Không có câu hỏi nào trong chủ đề này!", 
                            Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.e(TAG, "❌ Failed to get questions: " + response.code());
                    Toast.makeText(ApiSelectCategoryActivity.this, 
                        "❌ Lỗi khi tải câu hỏi: " + response.code(), 
                        Toast.LENGTH_LONG).show();
                }
            }
            
            @Override
            public void onFailure(Call<QuizApiService.TestQuizResponse> call, Throwable t) {
                showLoading(false);
                Log.e(TAG, "❌ Network error getting questions: " + t.getMessage());
                Toast.makeText(ApiSelectCategoryActivity.this, 
                    "❌ Lỗi kết nối khi tải câu hỏi: " + t.getMessage(), 
                    Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Load categories từ API thật - CHỈ LẤY DỮ LIỆU THẬT TỪ SQL SERVER
     */
    private void loadCategoriesFromApi() {
        showLoading(true);
        
        Log.d(TAG, "🔄 Loading REAL categories from SQL Server API...");
        Log.d(TAG, "🔗 API URL: " + ApiClient.getBaseUrl() + "chude/with-stats");
        
        // CHỈ call API thật, KHÔNG có mock data
        loadCategoriesFromApiReal();
    }
    

    
    /**
     * Load categories từ API thật (SQL Server) - CHỈ DỮ LIỆU THẬT
     */
    private void loadCategoriesFromApiReal() {
        Log.d(TAG, "🌐 Calling REAL API to get categories from SQL Server...");
        
        // SỬ DỤNG ENDPOINT THẬT: chude/with-stats
        Log.d(TAG, "🌐 Calling REAL API: " + ApiClient.getBaseUrl() + "chude/with-stats");
        quizService.getCategories().enqueue(new Callback<List<QuizApiService.CategoryResponse>>() {
            @Override
            public void onResponse(Call<List<QuizApiService.CategoryResponse>> call, Response<List<QuizApiService.CategoryResponse>> response) {
                showLoading(false);
                
                Log.d(TAG, "📡 Response Code: " + response.code());
                Log.d(TAG, "📡 Response URL: " + call.request().url());
                
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<QuizApiService.CategoryResponse> apiCategories = response.body();
                    Log.d(TAG, "✅ SUCCESS! Loaded " + apiCategories.size() + " REAL categories from SQL Server!");
                    
                    // Convert API response to Category objects
                    List<Category> realCategories = new ArrayList<>();
                    for (QuizApiService.CategoryResponse apiCat : apiCategories) {
                        realCategories.add(new Category(
                            apiCat.getId(),
                            apiCat.getName(),
                            apiCat.getIcon(),
                            apiCat.getQuiz_count(),
                            apiCat.getProgress_percent()
                        ));
                    }
                    
                    setupCategories(realCategories);
                    setupDefaultDifficulties();
                    
                    Toast.makeText(ApiSelectCategoryActivity.this, 
                        "✅ Đã tải " + realCategories.size() + " danh mục từ TEST endpoint!", 
                        Toast.LENGTH_LONG).show();
                        
                    // Log chi tiết từng category thật
                    for (Category cat : realCategories) {
                        Log.d(TAG, "📂 REAL Category: " + cat.getName() + " (ID: " + cat.getId() + ", Questions: " + cat.getQuizCount() + ")");
                    }
                    
                } else {
                    Log.e(TAG, "❌ Backend connected but NO DATA in database!");
                    Log.e(TAG, "❌ Response Code: " + response.code());
                    Log.e(TAG, "❌ Response Body Empty: " + (response.body() == null || response.body().isEmpty()));
                    
                    showErrorState("Backend không có dữ liệu trong bảng ChuDe!");
                    
                    Toast.makeText(ApiSelectCategoryActivity.this, 
                        "❌ BACKEND KHÔNG CÓ DỮ LIỆU!\n\n" +
                        "Database trống - Cần insert dữ liệu thật!\n\n" +
                        "🛠️ CÁCH FIX:\n" +
                        "1. Chạy: ./START_BACKEND_REAL.bat\n" +
                        "2. Hoặc: ./SETUP_REAL_DATA_NOW.bat\n" +
                        "3. Restart app\n\n" +
                        "📖 Xem: README_DU_LIEU_THAT.md", 
                        Toast.LENGTH_LONG).show();
                }
            }
            
            @Override
            public void onFailure(Call<List<QuizApiService.CategoryResponse>> call, Throwable t) {
                showLoading(false);
                Log.e(TAG, "❌ BACKEND CONNECTION FAILED!");
                Log.e(TAG, "❌ URL: " + call.request().url());
                Log.e(TAG, "❌ Error: " + t.getMessage());
                Log.e(TAG, "❌ Type: " + t.getClass().getSimpleName());
                
                showErrorState("Không kết nối được backend: " + t.getMessage());
                
                Toast.makeText(ApiSelectCategoryActivity.this, 
                    "❌ KHÔNG KẾT NỐI ĐƯỢC BACKEND!\n\n" +
                    "Cần backend chạy với dữ liệu thật!\n\n" +
                    "🛠️ CÁCH FIX:\n" +
                    "1. Chạy: ./START_BACKEND_REAL.bat\n" +
                    "2. Đợi backend start xong\n" +
                    "3. Restart app\n\n" +
                    "🔧 Long-press nút để test connection", 
                    Toast.LENGTH_LONG).show();
            }
        });
    }
    

    
    /**
     * Setup default difficulties
     */
    private void setupDefaultDifficulties() {
        List<Difficulty> difficultyList = new ArrayList<>();
        difficultyList.add(new Difficulty("easy", "Dễ", "Câu hỏi cơ bản"));
        difficultyList.add(new Difficulty("medium", "Trung bình", "Câu hỏi vừa phải"));
        difficultyList.add(new Difficulty("hard", "Khó", "Câu hỏi nâng cao"));
        
        setupDifficulties(difficultyList);
    }

    private void setupCategories(List<Category> categoryList) {
        if (categoryList.isEmpty()) {
            Toast.makeText(this, "Không có Danh mục nào!", Toast.LENGTH_SHORT).show();
            return;
        }

        rvCategory.setLayoutManager(new LinearLayoutManager(this));

        CategoryAdapter adapter = new CategoryAdapter(categoryList, category -> {
            selectedCategoryId = category.getId();
            Log.d(TAG, "Selected category: " + category.getName() + " (ID: " + category.getId() + ")");
            Toast.makeText(this, "Chọn: " + category.getName(), Toast.LENGTH_SHORT).show();
        });

        rvCategory.setAdapter(adapter);

        // Auto select first category
        selectedCategoryId = categoryList.get(0).getId();
        Log.d(TAG, "Auto selected first category ID: " + selectedCategoryId);
    }

    private void setupDifficulties(List<Difficulty> difficultyList) {
        if (difficultyList.isEmpty()) {
            Toast.makeText(this, "Không có Độ khó nào!", Toast.LENGTH_SHORT).show();
            return;
        }

        rvDifficulty.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        DifficultyAdapter adapter = new DifficultyAdapter(difficultyList, difficulty -> {
            selectedDifficultyId = difficulty.getId();
            Log.d(TAG, "Selected difficulty: " + difficulty.getName() + " (ID: " + difficulty.getId() + ")");
            Toast.makeText(this, "Độ khó: " + difficulty.getName(), Toast.LENGTH_SHORT).show();
        });

        rvDifficulty.setAdapter(adapter);

        // Auto select first difficulty
        selectedDifficultyId = difficultyList.get(0).getId();
        Log.d(TAG, "Auto selected first difficulty ID: " + selectedDifficultyId);
    }
    
    private void showLoading(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        
        if (btnStartQuiz != null) {
            btnStartQuiz.setEnabled(!show);
        }
    }
    
    /**
     * Hiển thị trạng thái lỗi khi không có dữ liệu thật từ SQL Server
     * CHỈ DÙNG DỮ LIỆU THẬT - KHÔNG CÓ FALLBACK
     */
    private void showErrorState(String errorMessage) {
        Log.e(TAG, "❌ Error state: " + errorMessage);
        
        // Disable button vì KHÔNG có dữ liệu thật
        if (btnStartQuiz != null) {
            btnStartQuiz.setEnabled(false);
            btnStartQuiz.setText("❌ Cần Dữ Liệu Thật");
        }
        
        // Clear any existing categories
        if (rvCategory != null) {
            rvCategory.setAdapter(null);
        }
        if (rvDifficulty != null) {
            rvDifficulty.setAdapter(null);
        }
        
        Log.e(TAG, "💡 App requires REAL data from SQL Server - no fallback!");
    }
}