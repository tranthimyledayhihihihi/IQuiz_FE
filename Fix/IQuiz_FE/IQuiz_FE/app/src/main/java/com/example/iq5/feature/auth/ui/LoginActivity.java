package com.example.iq5.feature.auth.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.R;
import com.example.iq5.core.navigation.NavigationHelper;
import com.example.iq5.feature.auth.data.AuthRepository;
import com.example.iq5.feature.auth.model.LoginResponse;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    
    private EditText txtEmail, txtPassword;
    private Button btnLogin;
    private ProgressBar progressBar;

    // Dữ liệu mock login
    private LoginResponse mock;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        initRepository();
        initMockData();
        initActions();
    }

    private void initViews() {
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar); // Thêm ProgressBar vào layout nếu chưa có
    }

    private void initRepository() {
        authRepository = new AuthRepository(this);
    }

    private void initMockData() {
        mock = authRepository.getLoginData(); // đọc dữ liệu mock từ JSON / asset

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
                String username = txtEmail != null
                        ? txtEmail.getText().toString().trim()
                        : "";
                String password = txtPassword != null
                        ? txtPassword.getText().toString().trim()
                        : "";

                // Validate input
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // ✅ GỌI API LOGIN THAY VÌ DÙNG MOCK
                performLogin(username, password);
            });
        }

        // Nút "Đăng ký"
        findViewById(R.id.tvRegister).setOnClickListener(v ->
                NavigationHelper.navigateToRegister(this)
        );
    }

    /**
     * ✅ GỌI API LOGIN
     */
    private void performLogin(String username, String password) {
        Log.d(TAG, "🔐 Đang đăng nhập với username: " + username);
        
        // Hiển thị loading
        showLoading(true);
        
        authRepository.loginAsync(username, password, new AuthRepository.LoginCallback() {
            @Override
            public void onSuccess(String token, String hoTen, String vaiTro) {
                runOnUiThread(() -> {
                    showLoading(false);
                    Log.d(TAG, "✅ Đăng nhập thành công! Token: " + token);
                    
                    // Save user info to SharedPreferences
                    getSharedPreferences("user_prefs", MODE_PRIVATE)
                        .edit()
                        .putString("user_name", hoTen)
                        .putString("user_email", username)
                        .putString("user_role", vaiTro)
                        .apply();
                    
                    Toast.makeText(LoginActivity.this, "✅ Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    goToHome();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    showLoading(false);
                    Log.e(TAG, "❌ Đăng nhập thất bại: " + error);
                    Toast.makeText(LoginActivity.this, "❌ " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void showLoading(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        if (btnLogin != null) {
            btnLogin.setEnabled(!show);
        }
    }

    private void goToHome() {
        // Tham số thứ 2 = true nếu muốn clear stack (không back lại màn login)
        NavigationHelper.navigateToHome(this, true);
    }
}
