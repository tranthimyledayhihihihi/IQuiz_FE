package com.example.iquiz.activities;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.iquiz.R;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        Button btnReset = findViewById(R.id.btnReset);
        Button btnSave = findViewById(R.id.btnSave);

        // Chß╗⌐c n─âng tß║ím thß╗¥i
        btnReset.setOnClickListener(v -> {
            // reset settings
        });

        btnSave.setOnClickListener(v -> {
            // save settings
        });
    }
}
