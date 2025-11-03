package com.example.fe;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;

public class AchievementActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        GridView grid = findViewById(R.id.gridAchievements);
        String[] achievements = {"🏅 Tân binh", "🔥 Chuỗi 7 ngày", "💎 Siêu sao", "🎯 100 điểm", "🕹️ Thủ lĩnh"};
        grid.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, achievements));
    }
}
