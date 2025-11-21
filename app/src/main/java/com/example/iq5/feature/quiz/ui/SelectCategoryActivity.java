package com.example.iq5.feature.quiz.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.util.Log;

import com.example.iq5.R;
import com.example.iq5.feature.quiz.adapter.CategoryAdapter;
import com.example.iq5.feature.quiz.model.Category;
import com.example.iq5.feature.quiz.adapter.DifficultyAdapter;
import com.example.iq5.feature.quiz.model.Difficulty;
import com.example.iq5.feature.quiz.data.SpecialModeRepository;

import com.example.iq5.feature.quiz.model.SelectionScreenResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SelectCategoryActivity extends AppCompatActivity {

    private static final String TAG = "SelectCategoryActivity";

    private int selectedCategoryId = -1;
    private String selectedDifficultyId = null;

    private RecyclerView rvCategory;
    private RecyclerView rvDifficulty;
    private Button btnStartQuiz;

    private SpecialModeRepository repository;

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
        repository = new SpecialModeRepository(this);
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

        Intent i = new Intent(this, QuizActivity.class);
        i.putExtra("categoryId", selectedCategoryId);
        i.putExtra("difficultyId", selectedDifficultyId);
        startActivity(i);
    }

    private void loadSelectionData() {
        try {
            SelectionScreenResponse response = repository.getSelectionScreenData();

            if (response == null || response.getData() == null || response.getData().getSections() == null) {
                Toast.makeText(this, "Không tìm thấy dữ liệu cấu hình Quiz!", Toast.LENGTH_LONG).show();
                return;
            }

            List<Category> categoryList = new ArrayList<>();
            List<Difficulty> difficultyList = new ArrayList<>();

            Gson gson = new Gson();

            Type categoryListType = new TypeToken<List<Category>>() {}.getType();
            Type difficultyListType = new TypeToken<List<Difficulty>>() {}.getType();

            // Parse từng section
            for (SelectionScreenResponse.SectionItem section : response.getData().getSections()) {

                if (section == null || section.getItems() == null)
                    continue;

                String jsonString = gson.toJson(section.getItems());

                switch (section.getType()) {
                    case "categories":
                        categoryList = gson.fromJson(jsonString, categoryListType);
                        break;

                    case "difficulty":
                        difficultyList = gson.fromJson(jsonString, difficultyListType);
                        break;

                    default:
                        Log.w(TAG, "Loại section không xác định: " + section.getType());
                }
            }

            if (categoryList.isEmpty() && difficultyList.isEmpty()) {
                Toast.makeText(this, "Không có dữ liệu Danh mục / Độ khó!", Toast.LENGTH_LONG).show();
                return;
            }

            setupCategories(categoryList);
            setupDifficulties(difficultyList);

        } catch (Exception e) {
            Log.e(TAG, "Lỗi parsing dữ liệu SelectionScreen:", e);
            Toast.makeText(this, "Lỗi đọc dữ liệu cấu hình! Kiểm tra JSON/Model.", Toast.LENGTH_LONG).show();
        }
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

        // Chọn mặc định mục đầu tiên
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

        // Mặc định chọn độ khó đầu tiên
        selectedDifficultyId = difficultyList.get(0).getId();
    }
}
