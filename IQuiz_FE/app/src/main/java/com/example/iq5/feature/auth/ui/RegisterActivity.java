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
import com.example.iq5.feature.auth.model.RegisterResponse;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    
    private EditText edtName, edtEmail, edtPassword;
    private Button btnRegister;
    private ProgressBar progressBar;
    
    private RegisterResponse mock;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        initRepository();
        initMockData();
        initActions();
    }

    private void initViews() {
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);
    }

    private void initRepository() {
        authRepository = new AuthRepository(this);
    }

    private void initMockData() {
        mock = authRepository.getRegisterData();

        if (mock != null) {
            if (edtName != null && mock.namePlaceholder != null) {
                edtName.setHint(mock.namePlaceholder);
            }
            if (edtEmail != null && mock.emailPlaceholder != null) {
                edtEmail.setHint(mock.emailPlaceholder);
            }
            if (edtPassword != null && mock.passwordPlaceholder != null) {
                edtPassword.setHint(mock.passwordPlaceholder);
            }
        }
    }

    private void initActions() {
        btnRegister.setOnClickListener(v -> {
            String name = edtName != null ? edtName.getText().toString().trim() : "";
            String email = edtEmail != null ? edtEmail.getText().toString().trim() : "";
            String password = edtPassword != null ? edtPassword.getText().toString().trim() : "";

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                return;
            }

            String username = email.split("@")[0];
            performRegister(username, name, email, password);
        });

        findViewById(R.id.tvLogin).setOnClickListener(v ->
                NavigationHelper.goBack(this)
        );
    }

    private void performRegister(String username, String name, String email, String password) {
        Log.d(TAG, "📝 Đang đăng ký với username: " + username + ", email: " + email);
        
        showLoading(true);
        
        authRepository.registerAsync(username, name, email, password, new AuthRepository.RegisterCallback() {
            @Override
            public void onSuccess(String message) {
                runOnUiThread(() -> {
                    showLoading(false);
                    Log.d(TAG, "✅ Đăng ký thành công!");
                    Toast.makeText(RegisterActivity.this, "✅ " + message, Toast.LENGTH_SHORT).show();
                    
                    NavigationHelper.goBack(RegisterActivity.this);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    showLoading(false);
                    Log.e(TAG, "❌ Đăng ký thất bại: " + error);
                    Toast.makeText(RegisterActivity.this, "❌ " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void showLoading(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        if (btnRegister != null) {
            btnRegister.setEnabled(!show);
        }
    }
}
