package com.example.iq5.feature.auth.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.R;
import com.example.iq5.feature.auth.data.AuthRepository;
import com.example.iq5.feature.auth.model.LoginResponse;

public class LoginActivity extends AppCompatActivity {

    EditText txtEmail, txtPassword;
    Button btnLogin;
    LoginResponse mock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        AuthRepository repo = new AuthRepository(this);
        mock = repo.getLoginData();

        txtEmail.setHint(mock.emailPlaceholder);
        txtPassword.setHint(mock.passwordPlaceholder);

        btnLogin.setOnClickListener(v -> {
            if (mock.loginSuccess) {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Login failed!", Toast.LENGTH_SHORT).show();
            }
        });

        // Navigate to Register
        findViewById(R.id.tvRegister).setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}
