package com.example.iq5.feature.specialmode.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.feature.specialmode.adapter.CustomQuestionAdapter;
import com.example.iq5.feature.specialmode.model.CustomQuestion;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomQuizEditorActivity extends AppCompatActivity {

    public static final String EXTRA_CUSTOM_QUIZ_ID = "CUSTOM_QUIZ_ID";
    public static final String EXTRA_CUSTOM_QUIZ_TITLE = "CUSTOM_QUIZ_TITLE";

    private String quizId;
    private String quizTitle;

    private TextView tvQuizTitle;
    private TextView tvQuestionCount;
    private RecyclerView rvQuestions;
    private CustomQuestionAdapter adapter;
    private final List<CustomQuestion> questions = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_quiz_editor);

        quizId = getIntent().getStringExtra(EXTRA_CUSTOM_QUIZ_ID);
        quizTitle = getIntent().getStringExtra(EXTRA_CUSTOM_QUIZ_TITLE);

        tvQuizTitle = findViewById(R.id.tv_quiz_title);
        tvQuestionCount = findViewById(R.id.tv_question_count);
        rvQuestions = findViewById(R.id.rv_questions);
        View btnAddQuestion = findViewById(R.id.btn_add_question);
        View btnDone = findViewById(R.id.btn_done);

        tvQuizTitle.setText("Bộ: " + (quizTitle != null ? quizTitle : ""));

        adapter = new CustomQuestionAdapter(new CustomQuestionAdapter.Listener() {
            @Override
            public void onEdit(int position, CustomQuestion question) {
                showQuestionDialog(question, position);
            }

            @Override
            public void onDelete(int position, CustomQuestion question) {
                questions.remove(position);
                adapter.submitList(new ArrayList<>(questions));
                updateQuestionCount();
            }
        });
        rvQuestions.setAdapter(adapter);

        // TODO: nếu bạn lưu vào DB / SharedPreferences thì load ở đây

        btnAddQuestion.setOnClickListener(v -> showQuestionDialog(null, -1));

        btnDone.setOnClickListener(v -> {
            // TODO: lưu danh sách câu hỏi nếu cần
            setResult(RESULT_OK);
            finish(); // quay lại CustomQuizFragment
        });

        updateQuestionCount();
    }

    private void updateQuestionCount() {
        tvQuestionCount.setText(questions.size() + " câu hỏi");
    }

    private void showQuestionDialog(@Nullable CustomQuestion existing, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(existing == null ? "Thêm câu hỏi" : "Sửa câu hỏi");

        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_question_editor, null, false);

        EditText etContent = dialogView.findViewById(R.id.et_content);
        EditText etA = dialogView.findViewById(R.id.et_option_a);
        EditText etB = dialogView.findViewById(R.id.et_option_b);
        EditText etC = dialogView.findViewById(R.id.et_option_c);
        EditText etD = dialogView.findViewById(R.id.et_option_d);
        EditText etExplanation = dialogView.findViewById(R.id.et_explanation);
        RadioGroup rgCorrect = dialogView.findViewById(R.id.rg_correct);
        RadioButton rbA = dialogView.findViewById(R.id.rb_a);
        RadioButton rbB = dialogView.findViewById(R.id.rb_b);
        RadioButton rbC = dialogView.findViewById(R.id.rb_c);
        RadioButton rbD = dialogView.findViewById(R.id.rb_d);

        if (existing != null) {
            etContent.setText(existing.content);
            etA.setText(existing.optionA);
            etB.setText(existing.optionB);
            etC.setText(existing.optionC);
            etD.setText(existing.optionD);
            etExplanation.setText(existing.explanation);

            if ("A".equals(existing.correctOption)) rbA.setChecked(true);
            else if ("B".equals(existing.correctOption)) rbB.setChecked(true);
            else if ("C".equals(existing.correctOption)) rbC.setChecked(true);
            else if ("D".equals(existing.correctOption)) rbD.setChecked(true);
        } else {
            rbA.setChecked(true);
        }

        builder.setView(dialogView);
        builder.setPositiveButton("Lưu", (dialog, which) -> {});
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(d -> dialog.getButton(DialogInterface.BUTTON_POSITIVE)
                .setOnClickListener(v -> {
                    String content = etContent.getText().toString().trim();
                    String a = etA.getText().toString().trim();
                    String b = etB.getText().toString().trim();
                    String c = etC.getText().toString().trim();
                    String d1 = etD.getText().toString().trim();
                    String explanation = etExplanation.getText().toString().trim();

                    if (TextUtils.isEmpty(content)) {
                        etContent.setError("Không được để trống");
                        return;
                    }
                    if (TextUtils.isEmpty(a) || TextUtils.isEmpty(b)
                            || TextUtils.isEmpty(c) || TextUtils.isEmpty(d1)) {
                        Snackbar.make(dialogView,
                                "Các phương án A, B, C, D không được trống",
                                Snackbar.LENGTH_SHORT).show();
                        return;
                    }

                    int checkedId = rgCorrect.getCheckedRadioButtonId();
                    String correct = "A";
                    if (checkedId == rbA.getId()) correct = "A";
                    else if (checkedId == rbB.getId()) correct = "B";
                    else if (checkedId == rbC.getId()) correct = "C";
                    else if (checkedId == rbD.getId()) correct = "D";

                    if (existing == null) {
                        CustomQuestion q = new CustomQuestion();
                        q.id = UUID.randomUUID().toString();
                        q.quizId = quizId;
                        q.content = content;
                        q.optionA = a;
                        q.optionB = b;
                        q.optionC = c;
                        q.optionD = d1;
                        q.correctOption = correct;
                        q.explanation = explanation;

                        questions.add(q);
                    } else {
                        existing.content = content;
                        existing.optionA = a;
                        existing.optionB = b;
                        existing.optionC = c;
                        existing.optionD = d1;
                        existing.correctOption = correct;
                        existing.explanation = explanation;
                    }

                    adapter.submitList(new ArrayList<>(questions));
                    updateQuestionCount();
                    dialog.dismiss();
                }));

        dialog.show();
    }
}
