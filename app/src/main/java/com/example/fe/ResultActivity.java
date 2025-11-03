package com.example.fe;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView tv = findViewById(R.id.tvResult);
        tv.setText(Math.random() > 0.5 ? "🎉 Bạn đã thắng!" : "😢 Bạn đã thua!");
    }
}
