package com.example.iq5.feature.auth.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.R;
import com.example.iq5.feature.auth.data.AuthRepository;
import com.example.iq5.feature.auth.model.RegisterResponse;

public class RegisterActivity extends AppCompatActivity {

    EditText edtName, edtEmail, edtPassword;
    Button btnRegister;

    RegisterResponse mock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // ĐÚNG ID TRONG XML
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        btnRegister = findViewById(R.id.btnRegister);

        AuthRepository repo = new AuthRepository(this);
        mock = repo.getRegisterData();

        // Set placeholder từ JSON mock
        edtName.setHint(mock.namePlaceholder);
        edtEmail.setHint(mock.emailPlaceholder);
        edtPassword.setHint(mock.passwordPlaceholder);

        btnRegister.setOnClickListener(v -> {
            if (mock.registerSuccess) {
                Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.tvLogin).setOnClickListener(v ->
                finish()
        );
    }

}
