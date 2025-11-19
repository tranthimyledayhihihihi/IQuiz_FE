package com.example.iq5.feature.auth.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.iq5.R;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button btnReset = findViewById(R.id.btnReset);
        Button btnSave = findViewById(R.id.btnSave);

        if (btnReset != null) {
            btnReset.setOnClickListener(v -> {
                Toast.makeText(this, "Reset settings", Toast.LENGTH_SHORT).show();
            });
        }

        if (btnSave != null) {
            btnSave.setOnClickListener(v -> {
                Toast.makeText(this, "Lưu cài đặt thành công", Toast.LENGTH_SHORT).show();
                finish();
            });
        }
    }
}
