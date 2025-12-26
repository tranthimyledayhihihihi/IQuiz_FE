package com.example.iq5.feature.specialmode.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.core.network.ApiClient;
import com.example.iq5.core.prefs.PrefsManager;
import com.example.iq5.data.api.ApiService;
import com.example.iq5.data.model.QuizSubmissionModel;
import com.example.iq5.data.model.QuizSubmitResponse;
import com.example.iq5.feature.specialmode.adapter.CustomQuestionAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomQuizEditorActivity extends AppCompatActivity {

    private static final String TAG = "CustomQuizEditor";

    private EditText etTitle, etDesc;
    private Button btnAdd, btnDone;
    private RecyclerView rvQuestions;

    private final List<QuizSubmissionModel.CauHoiSubmission> questions = new ArrayList<>();
    private CustomQuestionAdapter adapter;

    private ApiService apiService;
    private PrefsManager prefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_quiz_editor);

        // ===== Bind view =====
        etTitle = findViewById(R.id.et_quiz_title);
        etDesc = findViewById(R.id.et_quiz_desc);
        btnAdd = findViewById(R.id.btn_add_question);
        btnDone = findViewById(R.id.btn_done);
        rvQuestions = findViewById(R.id.rv_questions);

        // ===== Init =====
        prefs = new PrefsManager(this);
        apiService = ApiClient.getClient(prefs).create(ApiService.class);

        adapter = new CustomQuestionAdapter(questions);
        rvQuestions.setLayoutManager(new LinearLayoutManager(this));
        rvQuestions.setAdapter(adapter);

        // ===== Events =====
        btnAdd.setOnClickListener(v -> showAddDialog());
        btnDone.setOnClickListener(v -> submitQuiz());
    }

    // =====================================================
    // Dialog thêm câu hỏi (có Chủ đề + Độ khó)
    // =====================================================
    private void showAddDialog() {

        View view = getLayoutInflater()
                .inflate(R.layout.item_question_editor, null);

        EditText etQuestion = view.findViewById(R.id.et_question);
        EditText etA = view.findViewById(R.id.et_a);
        EditText etB = view.findViewById(R.id.et_b);
        EditText etC = view.findViewById(R.id.et_c);
        EditText etD = view.findViewById(R.id.et_d);

        Spinner spCorrect = view.findViewById(R.id.sp_correct);
        Spinner spTopic = view.findViewById(R.id.sp_topic);
        Spinner spDifficulty = view.findViewById(R.id.sp_difficulty);

        new AlertDialog.Builder(this)
                .setTitle("Thêm câu hỏi")
                .setView(view)
                .setPositiveButton("Thêm", (dialog, which) -> {

                    // ===== Lấy đáp án đúng =====
                    String correct;
                    switch (spCorrect.getSelectedItemPosition()) {
                        case 0:
                            correct = "DapAnA";
                            break;
                        case 1:
                            correct = "DapAnB";
                            break;
                        case 2:
                            correct = "DapAnC";
                            break;
                        default:
                            correct = "DapAnD";
                            break;
                    }

                    // ===== Lấy ID Chủ đề + Độ khó =====
                    int chuDeID = spTopic.getSelectedItemPosition() + 1;
                    int doKhoID = spDifficulty.getSelectedItemPosition() + 1;

                    // ===== Validate nhanh =====
                    if (etQuestion.getText().toString().trim().isEmpty()) {
                        Toast.makeText(this,
                                "Nội dung câu hỏi không được trống",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // ===== Tạo câu hỏi =====
                    QuizSubmissionModel.CauHoiSubmission item =
                            new QuizSubmissionModel.CauHoiSubmission(
                                    chuDeID,
                                    doKhoID,
                                    etQuestion.getText().toString(),
                                    etA.getText().toString(),
                                    etB.getText().toString(),
                                    etC.getText().toString(),
                                    etD.getText().toString(),
                                    correct
                            );

                    questions.add(item);
                    adapter.notifyItemInserted(questions.size() - 1);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // =====================================================
    // Submit quiz lên BE
    // =====================================================
    private void submitQuiz() {

        if (etTitle.getText().toString().trim().isEmpty()) {
            Toast.makeText(this,
                    "Vui lòng nhập tên quiz",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (questions.isEmpty()) {
            Toast.makeText(this,
                    "Quiz phải có ít nhất 1 câu hỏi",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        QuizSubmissionModel body =
                new QuizSubmissionModel(
                        etTitle.getText().toString(),
                        etDesc.getText().toString(),
                        questions
                );

        apiService.submitCustomQuiz(
                "Bearer " + prefs.getToken(),
                body
        ).enqueue(new Callback<QuizSubmitResponse>() {

            @Override
            public void onResponse(Call<QuizSubmitResponse> call,
                                   Response<QuizSubmitResponse> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(CustomQuizEditorActivity.this,
                            "🎉 Tạo quiz thành công",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.e(TAG, "BE reject: " + response.code());
                    Toast.makeText(CustomQuizEditorActivity.this,
                            "Lỗi BE: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<QuizSubmitResponse> call, Throwable t) {
                Log.e(TAG, "Network error", t);
                Toast.makeText(CustomQuizEditorActivity.this,
                        "Lỗi mạng: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
