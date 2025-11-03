package com.example.iquiz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.iquiz.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

//        Button btnBack = findViewById(R.id.btnBack);
//        btnBack.setOnClickListener(v -> finish());

        Button btnEditProfile = findViewById(R.id.btnEditProfile);
        btnEditProfile.setOnClickListener(v ->
                Toast.makeText(ProfileActivity.this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show()
        );

        Button btnShare = findViewById(R.id.btnShare);
        btnShare.setOnClickListener(v ->
                Toast.makeText(ProfileActivity.this, "Chia sẻ kết quả đang phát triển", Toast.LENGTH_SHORT).show()
        );

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
