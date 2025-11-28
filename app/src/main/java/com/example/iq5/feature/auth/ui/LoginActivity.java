package com.example.iq5.feature.auth.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View; // THÊM IMPORT NÀY
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast; // THÊM IMPORT NÀY

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.R;
import com.example.iq5.core.navigation.NavigationHelper;
import com.example.iq5.feature.auth.data.AuthRepository;
import com.example.iq5.feature.auth.model.LoginResponse;

public class LoginActivity extends AppCompatActivity {

    private EditText txtEmail, txtPassword;
    private Button btnLogin;

    //chứa dữ liệu giả lập
    private LoginResponse mock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        initMockData();
        initActions();
    }

    private void initViews() {
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }
    private void initMockData() {
        AuthRepository repo = new AuthRepository(this);
        mock = repo.getLoginData();// Placeholder từ mock JSON
        // gán gợi ý vào ô nhập liệu
        txtEmail.setHint(mock.emailPlaceholder);
        txtPassword.setHint(mock.passwordPlaceholder);
        mock = repo.getLoginData();

        // Placeholder từ mock JSON
        if (txtEmail != null) txtEmail.setHint(mock.emailPlaceholder);
        if (txtPassword != null) txtPassword.setHint(mock.passwordPlaceholder);
    }
    private void initActions() {
        btnLogin.setOnClickListener(v -> {
            String email = txtEmail.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();
            // Validate input
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            // Nếu mock.loginSuccess = true → coi như đăng nhập hợp lệ
            // (sau này thay bằng API thật)
            if (mock.loginSuccess) {
                goToHome();
            } else {
                Toast.makeText(this, "Sai email hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
            }
        });

        if (btnLogin != null) {
            // SỬA LỖI: setOnClickListener sử dụng lambda expression, đã thêm import View/Toast
            btnLogin.setOnClickListener(v -> {

                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();

                // Validate input
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Nếu mock.loginSuccess = true → coi như đăng nhập hợp lệ
                if (mock != null && mock.loginSuccess) {
                    goToHome();
                } else {
                    Toast.makeText(this, "Sai email hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Chuyển sang trang Register
        View tvRegister = findViewById(R.id.tvRegister);
        if (tvRegister != null) {
            tvRegister.setOnClickListener(v -> {
                NavigationHelper.navigateToRegister(this);
            });
        }
    }

    private void goToHome() {
        NavigationHelper.navigateToHome(this, true);
    }
}