package com.example.iq5.feature.auth.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.R;
import com.example.iq5.core.navigation.NavigationHelper;
import com.example.iq5.feature.auth.data.AuthRepository;
import com.example.iq5.feature.auth.model.LoginResponse;

public class LoginActivity extends AppCompatActivity {

    private EditText txtEmail, txtPassword;
    private Button btnLogin;

    // Dữ liệu mock login
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
        mock = repo.getLoginData(); // đọc dữ liệu mock từ JSON / asset

        if (mock != null) {
            if (txtEmail != null && mock.emailPlaceholder != null) {
                txtEmail.setHint(mock.emailPlaceholder);
            }
            if (txtPassword != null && mock.passwordPlaceholder != null) {
                txtPassword.setHint(mock.passwordPlaceholder);
            }
        }
    }

    private void initActions() {
        // Xử lý Login
        if (btnLogin != null) {
            btnLogin.setOnClickListener(v -> {
                String email = txtEmail != null
                        ? txtEmail.getText().toString().trim()
                        : "";
                String password = txtPassword != null
                        ? txtPassword.getText().toString().trim()
                        : "";

                // Validate input
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Tạm thời dùng mock để check login
                if (mock != null && mock.loginSuccess) {
                    goToHome();
                } else {
                    Toast.makeText(this, "Sai email hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Nút "Đăng ký"
        findViewById(R.id.tvRegister).setOnClickListener(v ->
                NavigationHelper.navigateToRegister(this)
        );
    }

    private void goToHome() {
        // Tham số thứ 2 = true nếu muốn clear stack (không back lại màn login)
        NavigationHelper.navigateToHome(this, true);
    }
}
