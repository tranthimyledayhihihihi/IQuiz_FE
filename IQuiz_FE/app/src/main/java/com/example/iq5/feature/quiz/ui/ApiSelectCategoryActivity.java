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
    private List<Category> categories = new ArrayList<>();

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
        
        // Set initial button text to remind user to select
        btnStartQuiz.setText("⚠️ Chọn danh mục và độ khó trước");
    }

    private void initApiComponents() {
        prefsManager = new PrefsManager(this);
        Retrofit retrofit = ApiClient.getClient(prefsManager);
        quizService = ApiClient.createService(retrofit, QuizApiService.class);
    }

    private void handleStartQuiz() {
        if (selectedCategoryId == -1) {
            Toast.makeText(this, "⚠️ Bạn chưa chọn danh mục!\nHãy chọn một chủ đề để bắt đầu quiz.", Toast.LENGTH_LONG).show();
            return;
        }

        if (selectedDifficultyId == null) {
            Toast.makeText(this, "⚠️ Bạn chưa chọn độ khó!\nHãy chọn mức độ khó để tiếp tục.", Toast.LENGTH_LONG).show();
            return;
        }

        // Show selected options before starting
        String categoryName = getSelectedCategoryName();
        Toast.makeText(this, "🚀 Bắt đầu quiz: " + categoryName + " - " + selectedDifficultyId, Toast.LENGTH_SHORT).show();
        
        // Gọi API để lấy câu hỏi theo category
        startQuizWithCategory(selectedCategoryId);
    }
    
    /**
     * Bắt đầu quiz bằng cách lấy câu hỏi từ API
     */
    private void startQuizWithCategory(int categoryId) {
        showLoading(true);
        
        Log.d(TAG, "🚀 Starting quiz for category: " + categoryId);
        
        quizService.getQuestionsByCategory(categoryId).enqueue(new Callback<com.example.iq5.data.model.SimpleQuizResponse>() {
            @Override
            public void onResponse(Call<com.example.iq5.data.model.SimpleQuizResponse> call, Response<com.example.iq5.data.model.SimpleQuizResponse> response) {
                showLoading(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    com.example.iq5.data.model.SimpleQuizResponse result = response.body();
                    
                    if (result.success && result.data != null && !result.data.isEmpty()) {
                        Log.d(TAG, "✅ Got " + result.data.size() + " questions for category " + categoryId);
                        
                        // Convert SimpleQuestionData to TestQuestionModel for compatibility
                        List<QuizApiService.TestQuestionModel> questions = new ArrayList<>();
                        for (com.example.iq5.data.model.SimpleQuizResponse.SimpleQuestionData data : result.data) {
                            QuizApiService.TestQuestionModel question = new QuizApiService.TestQuestionModel();
                            question.setId(data.id);
                            question.setQuestion(data.question);
                            question.setOptionA(data.option_a);
                            question.setOptionB(data.option_b);
                            question.setOptionC(data.option_c);
                            question.setOptionD(data.option_d);
                            question.setCorrectAnswer(data.correct_answer);
                            question.setCategoryId(data.category_id);
                            question.setDifficulty("Normal"); // Default difficulty
                            question.setCategoryName(getSelectedCategoryName()); // Get from selected category
                            questions.add(question);
                        }
                        
                        // Chuyển sang ApiQuizActivity với danh sách câu hỏi
                        NavigationHelper.navigateToApiQuizWithQuestions(
                            ApiSelectCategoryActivity.this, 
                            questions,
                            getSelectedCategoryName()
                        );
                        
                        Toast.makeText(ApiSelectCategoryActivity.this, 
                            "✅ Bắt đầu quiz với " + questions.size() + " câu hỏi!", 
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
            public void onFailure(Call<com.example.iq5.data.model.SimpleQuizResponse> call, Throwable t) {
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

        // Save categories list for later use
        this.categories = categoryList;

        rvCategory.setLayoutManager(new LinearLayoutManager(this));

        CategoryAdapter adapter = new CategoryAdapter(categoryList, category -> {
            selectedCategoryId = category.getId();
            Log.d(TAG, "Selected category: " + category.getName() + " (ID: " + category.getId() + ")");
            Toast.makeText(this, "✅ Chọn: " + category.getName(), Toast.LENGTH_SHORT).show();
            updateStartButtonText();
        });

        rvCategory.setAdapter(adapter);

        // Don't auto-select - user must choose
        selectedCategoryId = -1;
        Log.d(TAG, "No category selected - user must choose");
    }
    
    private String getSelectedCategoryName() {
        for (Category category : categories) {
            if (category.getId() == selectedCategoryId) {
                return category.getName();
            }
        }
        return "Unknown Category";
    }
    
    private void updateStartButtonText() {
        if (btnStartQuiz == null) return;
        
        if (selectedCategoryId == -1 && selectedDifficultyId == null) {
            btnStartQuiz.setText("⚠️ Chọn danh mục và độ khó trước");
        } else if (selectedCategoryId == -1) {
            btnStartQuiz.setText("⚠️ Chọn danh mục trước");
        } else if (selectedDifficultyId == null) {
            btnStartQuiz.setText("⚠️ Chọn độ khó trước");
        } else {
            btnStartQuiz.setText("🚀 BẮT ĐẦU QUIZ");
        }
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
            Toast.makeText(this, "✅ Độ khó: " + difficulty.getName(), Toast.LENGTH_SHORT).show();
            updateStartButtonText();
        });

        rvDifficulty.setAdapter(adapter);

        // Don't auto-select - user must choose
        selectedDifficultyId = null;
        Log.d(TAG, "No difficulty selected - user must choose");
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