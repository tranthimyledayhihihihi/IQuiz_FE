package com.example.iq5.feature.quiz.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button; // <-- Cần import Button
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;
import android.util.Log;

import com.example.iq5.R;
import com.example.iq5.core.util.ViewUtils;
import com.example.iq5.core.prefs.PrefsManager;
import com.example.iq5.feature.quiz.adapter.AnswerOptionAdapter;
import com.example.iq5.feature.quiz.data.QuizRepository;
import com.example.iq5.feature.quiz.model.Question;

import java.io.Serializable;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView txtQuestion;
    private RecyclerView rvOptions;
    private View loadingView;
    private ImageButton btnLifelineHint;
    private Button btnFinish;
    private Button btnSkip; // Khai báo nút BỎ QUA / TIẾP

    private List<Question> questionList;
    private int index = 0;

    private PrefsManager prefsManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_quiz);

        try {
            // 1. KHỞI TẠO PREFS MANAGER
            prefsManager = new PrefsManager(this);

            // 2. KHỞI TẠO VIEWS VÀ KIỂM TRA NULL
            CardView quizContentCard = findViewById(R.id.quizContentCard);
            loadingView = findViewById(R.id.loading);

            if (quizContentCard != null) {
                txtQuestion = quizContentCard.findViewById(R.id.txtQuestion);
                rvOptions   = quizContentCard.findViewById(R.id.recyclerOptions);
                btnLifelineHint = quizContentCard.findViewById(R.id.btnLifelineHint);
                btnFinish = quizContentCard.findViewById(R.id.btnFinish);
                btnSkip = quizContentCard.findViewById(R.id.btnSkip); // <-- Tìm kiếm nút BỎ QUA / TIẾP
            }

            // Kiểm tra các View quan trọng
            if (txtQuestion == null || rvOptions == null || loadingView == null || btnLifelineHint == null || btnFinish == null || btnSkip == null) { // <-- Kiểm tra btnSkip
                Toast.makeText(this, "Lỗi ID: Thiếu một trong các View quan trọng!", Toast.LENGTH_LONG).show();
                if (quizContentCard == null) {
                    Log.e("QuizActivity", "FATAL: ID quizContentCard not found in layout.");
                }
                finish();
                return;
            }

            // 3. THIẾT LẬP RECYCLERVIEW
            rvOptions.setLayoutManager(new GridLayoutManager(this, 2));

            // 4. THIẾT LẬP NÚT TRỢ GIÚP
            btnLifelineHint.setOnClickListener(v -> {
                showLifelineDialog();
            });

            // 5. THIẾT LẬP NÚT KẾT THÚC
            btnFinish.setOnClickListener(v -> {
                showReviewScreen();
            });

            // 6. THIẾT LẬP NÚT BỎ QUA / TIẾP (Chuyển sang câu hỏi kế tiếp)
            btnSkip.setOnClickListener(v -> {
                // Hiển thị đáp án (tùy chọn) và chuyển câu hỏi
                // Hiện tại, chỉ cần gọi nextQuestion() để chuyển ngay
                nextQuestion();
            });

            // 7. LẤY INTENT VÀ TẢI DỮ LIỆU
            int categoryId = getIntent().getIntExtra("categoryId", 1);
            int difficultyId = getIntent().getIntExtra("difficultyId", 1);

            load(categoryId, difficultyId);

        } catch (Exception e) {
            Log.e("QuizActivity", "FATAL CRASH DURING SETUP", e);
            Toast.makeText(this, "Lỗi khởi tạo nghiêm trọng: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void showLifelineDialog() {
        LifelineDialogFragment dialog = new LifelineDialogFragment();
        dialog.show(getSupportFragmentManager(), "lifeline_guide");
    }

    private void load(int categoryId, int difficultyId) {
        ViewUtils.show(loadingView);

        new QuizRepository(prefsManager).loadQuestions(categoryId, difficultyId, new QuizRepository.Callback() {
            @Override
            public void onSuccess(List<Question> list) {
                if (list == null || list.isEmpty()) {
                    ViewUtils.hide(loadingView);
                    txtQuestion.setText("Không có câu hỏi nào cho danh mục này.");
                    return;
                }

                questionList = list;
                ViewUtils.hide(loadingView);
                showQuestion();
            }

            @Override
            public void onError(String msg) {
                ViewUtils.hide(loadingView);
                Toast.makeText(QuizActivity.this, "Lỗi tải dữ liệu: " + msg, Toast.LENGTH_LONG).show();
                txtQuestion.setText("Error: " + msg);
            }
        });
    }

    /**
     * Tăng index và hiển thị câu hỏi kế tiếp.
     */
    private void nextQuestion() {
        rvOptions.postDelayed(() -> {
            index++;
            showQuestion();
        }, 300); // Độ trễ ngắn để tạo hiệu ứng chuyển

        // Kích hoạt lại nút Skip/Finish nếu chúng bị vô hiệu hóa
        btnSkip.setEnabled(true);
        btnFinish.setEnabled(true);
    }

    private void showQuestion() {
        if (questionList == null || index >= questionList.size()) {
            txtQuestion.setText("Quiz hoàn thành!");
            showReviewScreen();
            return;
        }

        Question q = questionList.get(index);
        txtQuestion.setText((index + 1) + ". " + q.getContent());

        rvOptions.setAdapter(new AnswerOptionAdapter(q.getOptions(), option -> {
            // Vô hiệu hóa click sau khi chọn (tránh click đúp)
            rvOptions.setAdapter(null);

            // Tắt nút Skip/Finish để ngăn người dùng thao tác trong khi chờ chuyển câu
            btnSkip.setEnabled(false);
            btnFinish.setEnabled(false);

            if (option.equals(q.getCorrectAnswer())) {
                txtQuestion.setText("Đúng! Chuyển câu hỏi...");
                nextQuestion(); // <-- Gọi nextQuestion
            } else {
                txtQuestion.setText("Sai rồi! Đáp án đúng là: " + q.getCorrectAnswer());
                // Nếu sai, bạn có thể quyết định chuyển câu hỏi luôn hoặc để người dùng tự nhấn Skip
                nextQuestion(); // Chuyển câu hỏi
            }
        }));
    }

    private void showReviewScreen() {
        Intent intent = new Intent(this, ReviewQuestionActivity.class);
        intent.putExtra("questions", (Serializable) questionList);
        startActivity(intent);
        finish();
    }
}