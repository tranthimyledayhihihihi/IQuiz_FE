package com.example.iq5.debug;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.feature.quiz.ui.ApiSelectCategoryActivity;
import com.example.iq5.feature.quiz.ui.SelectCategoryActivity;
import com.example.iq5.utils.QuickApiTest;
import com.example.iq5.debug.TestStreakApiActivity;
import com.example.iq5.debug.TestSettingsApiActivity;
import com.example.iq5.debug.DebugSettingsActivity;

/**
 * Activity để test trực tiếp các API activities
 */
public class TestApiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        createTestLayout();
    }
    
    private void createTestLayout() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);
        
        // Title
        TextView title = new TextView(this);
        title.setText("🧪 API Test Activity");
        title.setTextSize(24);
        title.setPadding(0, 0, 0, 32);
        layout.addView(title);
        
        // Test API Connection
        Button btnTestApi = new Button(this);
        btnTestApi.setText("🔗 Test API Connection");
        btnTestApi.setOnClickListener(v -> {
            QuickApiTest.checkBackendStatus(this);
        });
        layout.addView(btnTestApi);
        
        // Old Select Category (Mock Data)
        Button btnOldCategory = new Button(this);
        btnOldCategory.setText("📂 Old Select Category (Mock Data)");
        btnOldCategory.setOnClickListener(v -> {
            Intent intent = new Intent(this, SelectCategoryActivity.class);
            startActivity(intent);
        });
        layout.addView(btnOldCategory);
        
        // New API Select Category
        Button btnApiCategory = new Button(this);
        btnApiCategory.setText("🚀 NEW API Select Category");
        btnApiCategory.setOnClickListener(v -> {
            Toast.makeText(this, "Opening API Select Category...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ApiSelectCategoryActivity.class);
            startActivity(intent);
        });
        layout.addView(btnApiCategory);
        
        // Test Streak API
        Button btnTestStreak = new Button(this);
        btnTestStreak.setText("🔥 Test Streak API");
        btnTestStreak.setOnClickListener(v -> {
            Intent intent = new Intent(this, TestStreakApiActivity.class);
            startActivity(intent);
        });
        layout.addView(btnTestStreak);
        
        // Test Settings API
        Button btnTestSettings = new Button(this);
        btnTestSettings.setText("⚙️ Test Settings API");
        btnTestSettings.setOnClickListener(v -> {
            Intent intent = new Intent(this, TestSettingsApiActivity.class);
            startActivity(intent);
        });
        layout.addView(btnTestSettings);
        
        // Debug Settings API
        Button btnDebugSettings = new Button(this);
        btnDebugSettings.setText("🔍 Debug Settings API");
        btnDebugSettings.setOnClickListener(v -> {
            Intent intent = new Intent(this, DebugSettingsActivity.class);
            startActivity(intent);
        });
        layout.addView(btnDebugSettings);
        
        // Info
        TextView info = new TextView(this);
        info.setText("\n📝 Instructions:\n" +
                "1. Test API Connection first\n" +
                "2. Try Old version (should be empty)\n" +
                "3. Try NEW version (should show 5 categories)\n" +
                "\nIf NEW version still empty, check Logcat!");
        info.setTextSize(14);
        info.setPadding(0, 32, 0, 0);
        layout.addView(info);
        
        setContentView(layout);
    }
}