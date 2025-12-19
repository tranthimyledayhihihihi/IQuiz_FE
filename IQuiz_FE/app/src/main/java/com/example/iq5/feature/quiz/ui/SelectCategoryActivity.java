package com.example.iq5.feature.quiz.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.util.Log;

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

public class SelectCategoryActivity extends AppCompatActivity {

    private static final String TAG = "SelectCategoryActivity";

    private int selectedCategoryId = -1;
    private String selectedDifficultyId = null;

    private RecyclerView rvCategory;
    private RecyclerView rvDifficulty;
    private Button btnStartQuiz;

    // API components
    private PrefsManager prefsManager;
    private QuizApiService quizService;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_select_category);

        initViews();
        initRepository();

        loadSelectionData();

        btnStartQuiz.setOnClickListener(v -> handleStartQuiz());
    }

    private void initViews() {
        rvCategory = findViewById(R.id.rvCategory);
        rvDifficulty = findViewById(R.id.rvDifficulty);
        btnStartQuiz = findViewById(R.id.btnStartQuiz);

        if (rvCategory == null || rvDifficulty == null || btnStartQuiz == null) {
            Toast.makeText(this, "Lỗi layout: thiếu ID rvCategory/rvDifficulty/btnStartQuiz", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initRepository() {
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

        // Gọi API để lấy câu hỏi theo category (giống ApiSelectCategoryActivity)
        startQuizWithCategory(selectedCategoryId);
    }

    /**
     * Load categories từ API thật - GIỐNG ApiSelectCategoryActivity
     */
    private void loadSelectionData() {
        Log.d(TAG, "🔄 Loading REAL categories from SQL Server API...");
        Log.d(TAG, "🔗 API URL: " + ApiClient.getBaseUrl() + "chude/with-stats");
        
        // Gọi API thật để lấy categories
        quizService.getCategories().enqueue(new Callback<List<QuizApiService.CategoryResponse>>() {
            @Override
            public void onResponse(Call<List<QuizApiService.CategoryResponse>> call, Response<List<QuizApiService.CategoryResponse>> response) {
                Log.d(TAG, "📡 Response Code: " + response.code());
                
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
                    setupDefaultDifficulties(); // Tạo difficulties mặc định
                    
                    Toast.makeText(SelectCategoryActivity.this, 
                        "✅ Đã tải " + realCategories.size() + " danh mục từ SQL Server!", 
                        Toast.LENGTH_SHORT).show();
                        
                } else {
                    Log.e(TAG, "❌ Backend connected but NO DATA in database!");
                    Log.e(TAG, "❌ Response Code: " + response.code());
                    
                    Toast.makeText(SelectCategoryActivity.this, 
                        "❌ Không có dữ liệu trong database!\nCần thêm dữ liệu vào SQL Server.", 
                        Toast.LENGTH_LONG).show();
                }
            }
            
            @Override
            public void onFailure(Call<List<QuizApiService.CategoryResponse>> call, Throwable t) {
                Log.e(TAG, "❌ BACKEND CONNECTION FAILED!");
                Log.e(TAG, "❌ Error: " + t.getMessage());
                
                Toast.makeText(SelectCategoryActivity.this, 
                    "❌ Không kết nối được backend!\nKiểm tra backend có chạy không.", 
                    Toast.LENGTH_LONG).show();
            }
        });
    }
    
    /**
     * Tạo difficulties mặc định
     */
    private void setupDefaultDifficulties() {
        List<Difficulty> difficultyList = new ArrayList<>();
        difficultyList.add(new Difficulty("1", "Dễ", "Câu hỏi dễ"));
        difficultyList.add(new Difficulty("2", "Trung bình", "Câu hỏi trung bình"));
        difficultyList.add(new Difficulty("3", "Khó", "Câu hỏi khó"));
        
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
            Toast.makeText(this, "Chọn: " + category.getName(), Toast.LENGTH_SHORT).show();
        });

        rvCategory.setAdapter(adapter);

        selectedCategoryId = categoryList.get(0).getId();
    }

    private void setupDifficulties(List<Difficulty> difficultyList) {
        if (difficultyList.isEmpty()) {
            Toast.makeText(this, "Không có Độ khó nào!", Toast.LENGTH_SHORT).show();
            return;
        }

        rvDifficulty.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        DifficultyAdapter adapter = new DifficultyAdapter(difficultyList, difficulty -> {
            selectedDifficultyId = difficulty.getId();
            Toast.makeText(this, "Độ khó: " + difficulty.getName(), Toast.LENGTH_SHORT).show();
        });

        rvDifficulty.setAdapter(adapter);

        selectedDifficultyId = difficultyList.get(0).getId();
    }
    
    /**
     * Bắt đầu quiz bằng cách lấy câu hỏi từ API - GIỐNG ApiSelectCategoryActivity
     */
    private void startQuizWithCategory(int categoryId) {
        Log.d(TAG, "🚀 Starting quiz for category: " + categoryId);
        
        quizService.getQuestionsByCategory(categoryId).enqueue(new Callback<QuizApiService.TestQuizResponse>() {
            @Override
            public void onResponse(Call<QuizApiService.TestQuizResponse> call, Response<QuizApiService.TestQuizResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    QuizApiService.TestQuizResponse result = response.body();
                    
                    if (result.isSuccess() && result.getQuestions() != null && !result.getQuestions().isEmpty()) {
                        Log.d(TAG, "✅ Got " + result.getQuestions().size() + " questions for category " + categoryId);
                        
                        // Chuyển sang ApiQuizActivity với danh sách câu hỏi
                        NavigationHelper.navigateToApiQuizWithQuestions(
                            SelectCategoryActivity.this, 
                            result.getQuestions(),
                            result.getQuestions().get(0).getCategoryName()
                        );
                        
                        Toast.makeText(SelectCategoryActivity.this, 
                            "✅ Bắt đầu quiz với " + result.getQuestions().size() + " câu hỏi!", 
                            Toast.LENGTH_SHORT).show();
                            
                    } else {
                        Log.e(TAG, "❌ No questions found for category " + categoryId);
                        Toast.makeText(SelectCategoryActivity.this, 
                            "❌ Không có câu hỏi nào trong chủ đề này!", 
                            Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.e(TAG, "❌ Failed to get questions: " + response.code());
                    Toast.makeText(SelectCategoryActivity.this, 
                        "❌ Lỗi khi tải câu hỏi: " + response.code(), 
                        Toast.LENGTH_LONG).show();
                }
            }
            
            @Override
            public void onFailure(Call<QuizApiService.TestQuizResponse> call, Throwable t) {
                Log.e(TAG, "❌ Network error getting questions: " + t.getMessage());
                Toast.makeText(SelectCategoryActivity.this, 
                    "❌ Lỗi kết nối khi tải câu hỏi: " + t.getMessage(), 
                    Toast.LENGTH_LONG).show();
            }
        });
    }
}
