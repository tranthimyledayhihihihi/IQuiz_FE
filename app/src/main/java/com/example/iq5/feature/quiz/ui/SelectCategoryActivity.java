package com.example.iq5.feature.quiz.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button; // Cần import Button
import android.widget.Toast; // Dùng để thông báo lỗi/chọn

import com.example.iq5.R;
import com.example.iq5.feature.quiz.adapter.CategoryAdapter;
import com.example.iq5.feature.quiz.model.Category;
import com.example.iq5.feature.quiz.adapter.DifficultyAdapter; // Cần thiết
import com.example.iq5.feature.quiz.model.Difficulty; // Cần thiết

import java.util.Arrays;
import java.util.List;

public class SelectCategoryActivity extends AppCompatActivity {

    private int selectedCategoryId = -1; // -1: Chưa chọn Category
    private int selectedDifficultyId = 1; // ID mặc định: Giả sử 1 là Easy

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_select_category);

        RecyclerView rvCategory = findViewById(R.id.rvCategory);
        RecyclerView rvDifficulty = findViewById(R.id.rvDifficulty);
        Button btnStartQuiz = findViewById(R.id.btnStartQuiz); // Tìm nút Bắt đầu

        // 1. Thiết lập Category RecyclerView
        rvCategory.setLayoutManager(new LinearLayoutManager(this));

        List<Category> categoryList = Arrays.asList(
                new Category(1, "Science", ""),
                new Category(2, "Math", ""),
                new Category(3, "History", "")
        );

        CategoryAdapter categoryAdapter = new CategoryAdapter(categoryList, category -> {
            // Khi Category được chọn
            selectedCategoryId = category.getId();
            Toast.makeText(this, "Đã chọn Danh mục: " + category.getContent(), Toast.LENGTH_SHORT).show();
            // Cần thông báo cho adapter để highlight lựa chọn (nếu có logic đó)
        });

        rvCategory.setAdapter(categoryAdapter);

        // 2. Thiết lập Difficulty RecyclerView
        rvDifficulty.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<Difficulty> difficultyList = Arrays.asList(
                new Difficulty(1, "Easy"),
                new Difficulty(2, "Medium"),
                new Difficulty(3, "Hard")
        );

        DifficultyAdapter difficultyAdapter = new DifficultyAdapter(difficultyList, difficulty -> {
            // Khi Difficulty được chọn
            selectedDifficultyId = difficulty.getId();
            Toast.makeText(this, "Đã chọn Độ khó: " + difficulty.getContent(), Toast.LENGTH_SHORT).show();
            // Cần thông báo cho adapter để highlight lựa chọn (nếu có logic đó)
        });

        rvDifficulty.setAdapter(difficultyAdapter);


        // 3. Xử lý sự kiện nhấn nút BẮT ĐẦU QUIZ
        btnStartQuiz.setOnClickListener(v -> {
            if (selectedCategoryId == -1) {
                Toast.makeText(this, "Vui lòng chọn một Danh mục trước!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Chuyển sang QuizActivity và truyền Category ID & Difficulty ID
            Intent i = new Intent(this, QuizActivity.class);
            i.putExtra("categoryId", selectedCategoryId);
            i.putExtra("difficultyId", selectedDifficultyId);
            startActivity(i);
        });
    }
}