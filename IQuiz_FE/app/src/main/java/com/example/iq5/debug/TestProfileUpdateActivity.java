package com.example.iq5.debug;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.R;
import com.example.iq5.data.model.ProfileUpdateModel;
import com.example.iq5.data.repository.ProfileRepository;

/**
 * Activity để test update profile với data cố định
 */
public class TestProfileUpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_profile_update);

        Button btnTest = findViewById(R.id.btnTest);
        btnTest.setOnClickListener(v -> testUpdateProfile());
    }

    private void testUpdateProfile() {
        ProfileRepository repository = new ProfileRepository(this);
        
        // Tạo data test với tất cả field có giá trị
        ProfileUpdateModel model = new ProfileUpdateModel();
        model.hoTen = "Test User Updated";
        model.email = "testupdate" + System.currentTimeMillis() + "@example.com"; // Email unique
        model.anhDaiDien = ""; // Empty string thay vì null
        
        Toast.makeText(this, "Đang test với email: " + model.email, Toast.LENGTH_SHORT).show();
        
        repository.updateProfileAsync(model, new ProfileRepository.UpdateCallback() {
            @Override
            public void onSuccess(String message) {
                runOnUiThread(() -> {
                    Toast.makeText(TestProfileUpdateActivity.this, 
                        "✅ Thành công: " + message, Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(TestProfileUpdateActivity.this, 
                        "❌ Lỗi: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }
}