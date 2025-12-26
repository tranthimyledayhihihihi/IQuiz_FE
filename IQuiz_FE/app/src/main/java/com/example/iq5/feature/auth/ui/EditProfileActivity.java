package com.example.iq5.feature.auth.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.iq5.R;
import com.example.iq5.data.model.ProfileUpdateModel;
import com.example.iq5.data.model.UserProfileModel;
import com.example.iq5.data.repository.ProfileRepository;
import com.google.android.material.textfield.TextInputEditText;

public class EditProfileActivity extends AppCompatActivity {

    // UI Components
    private ImageView imgAvatar;
    private TextInputEditText etName, etEmail;
    private Button btnSaveProfile, btnCancel;
    private ImageButton btnBack, btnSave;
    private CardView btnEditAvatar;
    
    // Data
    private ProfileRepository profileRepository;
    private UserProfileModel currentProfile;
    private String selectedAvatarPath;
    
    // Image picker
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initViews();
        initRepository();
        setupImagePicker();
        setupClickListeners();
        loadCurrentProfile();
    }

    private void initViews() {
        imgAvatar = findViewById(R.id.imgAvatar);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        btnCancel = findViewById(R.id.btnCancel);
        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        btnEditAvatar = findViewById(R.id.btnEditAvatar);
    }

    private void initRepository() {
        profileRepository = new ProfileRepository(this);
    }

    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        // Load image into ImageView
                        Glide.with(this)
                            .load(imageUri)
                            .placeholder(R.drawable.ic_user_placeholder)
                            .into(imgAvatar);
                        
                        // Store path for later upload
                        selectedAvatarPath = imageUri.toString();
                    }
                }
            }
        );
    }

    private void setupClickListeners() {
        // Back buttons
        btnBack.setOnClickListener(v -> finish());
        btnCancel.setOnClickListener(v -> finish());

        // Save buttons (both header and form)
        btnSave.setOnClickListener(v -> saveProfile());
        btnSaveProfile.setOnClickListener(v -> saveProfile());

        // Edit avatar
        btnEditAvatar.setOnClickListener(v -> openImagePicker());
    }

    private void openImagePicker() {
        new AlertDialog.Builder(this)
            .setTitle("Chọn ảnh đại diện")
            .setItems(new String[]{"Chọn từ thư viện", "Hủy"}, (dialog, which) -> {
                if (which == 0) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    imagePickerLauncher.launch(intent);
                }
            })
            .show();
    }

    private void loadCurrentProfile() {
        profileRepository.getMyProfileAsync(new ProfileRepository.ProfileCallback() {
            @Override
            public void onSuccess(UserProfileModel profile) {
                runOnUiThread(() -> {
                    currentProfile = profile;
                    populateFields(profile);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(EditProfileActivity.this, 
                        "Không thể tải thông tin: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void populateFields(UserProfileModel profile) {
        if (profile.getHoTen() != null) {
            etName.setText(profile.getHoTen());
        }
        
        if (profile.getTenDangNhap() != null) {
            etEmail.setText(profile.getTenDangNhap());
        }
        
        // Load avatar
        if (profile.getAnhDaiDien() != null && !profile.getAnhDaiDien().isEmpty()) {
            Glide.with(this)
                .load(profile.getAnhDaiDien())
                .placeholder(R.drawable.ic_user_placeholder)
                .into(imgAvatar);
        }
    }

    private void saveProfile() {
        String name = etName.getText() != null ? etName.getText().toString().trim() : "";
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";

        // Validation
        if (TextUtils.isEmpty(name)) {
            etName.setError("Vui lòng nhập họ tên");
            etName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Vui lòng nhập email");
            etEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không hợp lệ");
            etEmail.requestFocus();
            return;
        }

        // Show confirmation dialog
        showSaveConfirmationDialog(name, email);
    }

    private void showSaveConfirmationDialog(String name, String email) {
        String message = "Bạn có chắc chắn muốn lưu các thay đổi?\n\n";
        message += "• Họ tên: " + name + "\n";
        message += "• Email: " + email + "\n";
        
        if (selectedAvatarPath != null) {
            message += "• Ảnh đại diện: Sẽ được cập nhật";
        }

        new AlertDialog.Builder(this)
            .setTitle("Xác nhận lưu")
            .setMessage(message)
            .setPositiveButton("Lưu", (dialog, which) -> {
                performSave(name, email);
            })
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void performSave(String name, String email) {
        // Disable buttons to prevent multiple clicks
        btnSave.setEnabled(false);
        btnSaveProfile.setEnabled(false);

        // Create update model
        ProfileUpdateModel updateModel = new ProfileUpdateModel();
        updateModel.hoTen = name;
        updateModel.email = email;
        
        // Handle avatar if selected
        if (selectedAvatarPath != null) {
            // In a real app, you would upload the image to server first
            // For now, we'll use the URI as placeholder
            updateModel.anhDaiDien = selectedAvatarPath;
        }

        // Update profile
        profileRepository.updateProfileAsync(updateModel, new ProfileRepository.UpdateCallback() {
            @Override
            public void onSuccess(String message) {
                runOnUiThread(() -> {
                    // Profile update successful
                    Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                    
                    // Return success result
                    setResult(RESULT_OK);
                    finish();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(EditProfileActivity.this, 
                        "Lỗi cập nhật: " + error, Toast.LENGTH_SHORT).show();
                    
                    // Re-enable buttons
                    btnSave.setEnabled(true);
                    btnSaveProfile.setEnabled(true);
                });
            }
        });
    }

    private void changePassword(String currentPassword, String newPassword, String confirmNewPassword, String profileMessage) {
        profileRepository.changePasswordAsync(currentPassword, newPassword, confirmNewPassword,
            new ProfileRepository.ChangePasswordCallback() {
                @Override
                public void onSuccess(String message) {
                    runOnUiThread(() -> {
                        Toast.makeText(EditProfileActivity.this, 
                            profileMessage + "\n" + message, Toast.LENGTH_LONG).show();
                        
                        // Return success result
                        setResult(RESULT_OK);
                        finish();
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        Toast.makeText(EditProfileActivity.this, 
                            profileMessage + "\nNhưng lỗi đổi mật khẩu: " + error, 
                            Toast.LENGTH_LONG).show();
                        
                        // Re-enable buttons
                        btnSave.setEnabled(true);
                        btnSaveProfile.setEnabled(true);
                    });
                }
            });
    }

    @Override
    public void onBackPressed() {
        // Check if there are unsaved changes
        if (hasUnsavedChanges()) {
            new AlertDialog.Builder(this)
                .setTitle("Thoát mà không lưu?")
                .setMessage("Bạn có thay đổi chưa được lưu. Bạn có chắc chắn muốn thoát?")
                .setPositiveButton("Thoát", (dialog, which) -> super.onBackPressed())
                .setNegativeButton("Ở lại", null)
                .show();
        } else {
            super.onBackPressed();
        }
    }

    private boolean hasUnsavedChanges() {
        if (currentProfile == null) return false;
        
        String currentName = etName.getText() != null ? etName.getText().toString().trim() : "";
        String currentEmail = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        
        boolean nameChanged = !currentName.equals(currentProfile.getHoTen() != null ? currentProfile.getHoTen() : "");
        boolean emailChanged = !currentEmail.equals(currentProfile.getTenDangNhap() != null ? currentProfile.getTenDangNhap() : "");
        boolean avatarChanged = selectedAvatarPath != null;
        
        return nameChanged || emailChanged || avatarChanged;
    }
}