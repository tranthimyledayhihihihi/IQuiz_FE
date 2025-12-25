package com.example.iq5.feature.auth.ui;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.iq5.R;
import com.example.iq5.feature.auth.data.AuthRepository;
import com.example.iq5.feature.auth.model.SettingsResponse;

public class SettingsActivity extends AppCompatActivity {

    SwitchCompat switchDarkMode, switchSound;
    Spinner spinnerLanguage;
    SeekBar seekVolume;
    TextView tvVolumePercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // ĐÚNG ID TRONG XML
        switchDarkMode = findViewById(R.id.switchDarkMode);
        switchSound = findViewById(R.id.switchSound);
        spinnerLanguage = findViewById(R.id.spinnerLanguage);
        seekVolume = findViewById(R.id.seekVolume);
        tvVolumePercent = findViewById(R.id.tvVolumePercent);

        // Load JSON với null check
        AuthRepository repo = new AuthRepository(this);
        SettingsResponse s = repo.getSettingsData();

        // Bind JSON vào UI với null check
        if (s != null) {
            switchDarkMode.setChecked(s.darkMode);
            switchSound.setChecked(true); // JSON chưa có, bạn có thể thêm sau

            if (s.language != null) {
                if (s.language.equals("vi")) spinnerLanguage.setSelection(0);
                else if (s.language.equals("en")) spinnerLanguage.setSelection(1);
            }
        } else {
            // Sử dụng giá trị mặc định khi JSON null
            switchDarkMode.setChecked(false);
            switchSound.setChecked(true);
            spinnerLanguage.setSelection(0); // Default to Vietnamese
        }

        // Seekbar volume (mock)
        seekVolume.setProgress(70);
        tvVolumePercent.setText("70%");

        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

}
