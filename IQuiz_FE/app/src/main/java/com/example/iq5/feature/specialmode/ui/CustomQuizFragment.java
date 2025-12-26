package com.example.iq5.feature.specialmode.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.iq5.R;
import com.google.android.material.snackbar.Snackbar;

public class CustomQuizFragment extends Fragment {

    public static final String EXTRA_QUIZ_TITLE = "EXTRA_QUIZ_TITLE";
    public static final String EXTRA_QUIZ_DESC  = "EXTRA_QUIZ_DESC";

    private EditText etQuizTitle;
    private EditText etQuizDesc;
    private Button btnCreateQuiz;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_custom_quiz, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        // ===== BIND VIEW (KHỚP XML BẠN GỬI) =====
        etQuizTitle   = view.findViewById(R.id.et_quiz_title);
        etQuizDesc    = view.findViewById(R.id.et_quiz_desc);
        btnCreateQuiz = view.findViewById(R.id.btn_create_quiz);

        btnCreateQuiz.setOnClickListener(v -> {
            String title = etQuizTitle.getText().toString().trim();
            String desc  = etQuizDesc.getText().toString().trim();

            if (TextUtils.isEmpty(title)) {
                Snackbar.make(
                        view,
                        "Vui lòng nhập tên bộ câu hỏi",
                        Snackbar.LENGTH_SHORT
                ).show();
                return;
            }

            // 👉 CHUYỂN SANG MÀN EDITOR
            Intent intent = new Intent(
                    requireContext(),
                    CustomQuizEditorActivity.class
            );
            intent.putExtra(EXTRA_QUIZ_TITLE, title);
            intent.putExtra(EXTRA_QUIZ_DESC, desc);

            startActivity(intent);

            etQuizTitle.setText("");
            etQuizDesc.setText("");
        });
    }
}
