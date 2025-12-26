package com.example.iq5.debug;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.R;
import com.example.iq5.feature.auth.ui.EditProfileActivity;

/**
 * Activity để test trang chỉnh sửa profile
 */
public class TestEditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_edit_profile);

        Button btnTestEdit = findViewById(R.id.btnTestEdit);
        btnTestEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivity(intent);
        });
    }
}