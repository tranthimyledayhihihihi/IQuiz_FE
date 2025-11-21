// File cần sửa: SelectCategoryActivity.java

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

// Imports các Response Model
import com.example.iq5.feature.quiz.model.SelectionScreenResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken; // Cần thiết
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

        repository = new SpecialModeRepository(this);

        rvCategory = findViewById(R.id.rvCategory);
        rvDifficulty = findViewById(R.id.rvDifficulty);
        btnStartQuiz = findViewById(R.id.btnStartQuiz);

        if (rvCategory == null || rvDifficulty == null || btnStartQuiz == null) {
            Toast.makeText(this, "Lỗi layout: Thiếu ID rvCategory, rvDifficulty, hoặc btnStartQuiz.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        loadSelectionData(); // Tải toàn bộ dữ liệu màn hình

        btnStartQuiz.setOnClickListener(v -> {
            if (selectedCategoryId == -1) {
                Toast.makeText(this, "Vui lòng chọn một Danh mục trước!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedDifficultyId == null) {
                Toast.makeText(this, "Vui lòng chọn Độ khó!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent i = new Intent(this, QuizActivity.class);
            i.putExtra("categoryId", selectedCategoryId);
            i.putExtra("difficultyId", selectedDifficultyId);
            startActivity(i);
        });
    }

    private void loadSelectionData() {
        try {
            SelectionScreenResponse response = repository.getSelectionScreenData();

            if (response == null || response.getData() == null || response.getData().getSections() == null) {
                Toast.makeText(this, "Không tìm thấy dữ liệu cấu hình Quiz (Response rỗng)!", Toast.LENGTH_LONG).show();
                return;
            }

            List<Category> categoryList = new ArrayList<>();
            List<Difficulty> difficultyList = new ArrayList<>();

            Gson gson = new Gson();
            // Định nghĩa TypeToken cho chuyển đổi an toàn hơn
            Type categoryListType = new TypeToken<List<Category>>() {}.getType();
            Type difficultyListType = new TypeToken<List<Difficulty>>() {}.getType();

            for (SelectionScreenResponse.SectionItem section : response.getData().getSections()) {
                List<Object> itemsRaw = section.getItems();
                if (itemsRaw == null || itemsRaw.isEmpty()) continue;

                // Chuyển List<Object> thô thành chuỗi JSON và sau đó parse bằng TypeToken
                String itemsJsonString = gson.toJson(itemsRaw);

                if ("categories".equals(section.getType())) {
                    categoryList = gson.fromJson(itemsJsonString, categoryListType);
                } else if ("difficulty".equals(section.getType())) {
                    difficultyList = gson.fromJson(itemsJsonString, difficultyListType);
                }
            }

            // Xử lý khi List rỗng sau khi trích xuất
            if (categoryList.isEmpty() && difficultyList.isEmpty()) {
                Toast.makeText(this, "Không có Danh mục/Độ khó nào được trích xuất.", Toast.LENGTH_LONG).show();
                return;
            }

            setupCategories(categoryList);
            setupDifficulties(difficultyList);

        } catch (Exception e) {
            Log.e(TAG, "Lỗi FATAL khi tải dữ liệu SelectionScreen:", e);
            Toast.makeText(this, "Lỗi parsing dữ liệu. Kiểm tra Model và JSON!", Toast.LENGTH_LONG).show();
        }
    }

    private void setupCategories(List<Category> categoryList) {
        if (categoryList.isEmpty()) {
            Toast.makeText(this, "Không có Danh mục nào để hiển thị.", Toast.LENGTH_SHORT).show();
            return;
        }

        rvCategory.setLayoutManager(new LinearLayoutManager(this));

        CategoryAdapter categoryAdapter = new CategoryAdapter(categoryList, category -> {
            selectedCategoryId = category.getId();
            Toast.makeText(this, "Đã chọn Danh mục: " + category.getName(), Toast.LENGTH_SHORT).show();
            // Cần gọi notifyDataSetChanged/notifyItemChanged trên adapter nếu bạn có logic highlight
        });

        rvCategory.setAdapter(categoryAdapter);

        // Chọn mặc định Category đầu tiên
        selectedCategoryId = categoryList.get(0).getId();
    }

    private void setupDifficulties(List<Difficulty> difficultyList) {
        if (difficultyList.isEmpty()) {
            Toast.makeText(this, "Không có Độ khó nào để hiển thị.", Toast.LENGTH_SHORT).show();
            return;
        }

        rvDifficulty.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        DifficultyAdapter difficultyAdapter = new DifficultyAdapter(difficultyList, difficulty -> {
            selectedDifficultyId = difficulty.getId();
            Toast.makeText(this, "Đã chọn Độ khó: " + difficulty.getName(), Toast.LENGTH_SHORT).show();
            // Cần gọi notifyDataSetChanged/notifyItemChanged trên adapter nếu bạn có logic highlight
        });

        rvDifficulty.setAdapter(difficultyAdapter);

        // Thiết lập mặc định là độ khó đầu tiên
        selectedDifficultyId = difficultyList.get(0).getId();
    }
}