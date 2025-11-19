package com.example.iquiz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.iquiz.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageView btnProfile = findViewById(R.id.btnProfile);
        ImageView btnSettings = findViewById(R.id.btnSettings);
        
        // Click avatar ΓåÆ mß╗ƒ ProfileActivity
        btnProfile.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "Profile clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        });


        // Click Settings

        btnSettings.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class))
        );


    }
}
