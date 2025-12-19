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

/**
 * Simple test activity để debug
 */
public class SimpleTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 50, 50, 50);
        
        TextView title = new TextView(this);
        title.setText("🔧 DEBUG TEST");
        title.setTextSize(20);
        title.setPadding(0, 0, 0, 30);
        layout.addView(title);
        
        // Test Old Activity
        Button btnOld = new Button(this);
        btnOld.setText("📂 OLD SelectCategoryActivity (Mock Data)");
        btnOld.setOnClickListener(v -> {
            Toast.makeText(this, "Opening OLD activity...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, SelectCategoryActivity.class));
        });
        layout.addView(btnOld);
        
        // Test New Activity
        Button btnNew = new Button(this);
        btnNew.setText("🚀 NEW ApiSelectCategoryActivity (Real API)");
        btnNew.setOnClickListener(v -> {
            Toast.makeText(this, "Opening NEW API activity...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ApiSelectCategoryActivity.class));
        });
        layout.addView(btnNew);
        
        TextView info = new TextView(this);
        info.setText("\n📝 Test both activities:\n" +
                "- OLD should show empty (mock data)\n" +
                "- NEW should call real API\n" +
                "\nCheck Logcat for details!");
        info.setTextSize(12);
        layout.addView(info);
        
        setContentView(layout);
    }
}