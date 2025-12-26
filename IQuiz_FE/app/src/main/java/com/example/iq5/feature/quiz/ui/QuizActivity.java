package com.example.iq5.feature.quiz.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.feature.quiz.adapter.AnswerOptionAdapter;
import com.example.iq5.feature.quiz.model.Option;
import com.example.iq5.feature.quiz.model.Question;
import com.example.iq5.feature.specialmode.data.SpecialModeRepository;
import com.example.iq5.feature.specialmode.model.WrongAnswerItem;
import com.example.iq5.feature.specialmode.model.WrongAnswersResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizActivity extends AppCompatActivity {

    private TextView txtQuestion;
    private RecyclerView rvOptions;
    private ImageButton btnLifelineHint;
    private Button btnFinish, btnSkip;

    private AnswerOptionAdapter optionAdapter;

    private final List<Question> questionList = new ArrayList<>();
    private int currentIndex = 0;

    private String entryMode;
    private SpecialModeRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        initViews();

        repository = new SpecialModeRepository(this);

        entryMode = getIntent().getStringExtra("ENTRY_MODE");

        if ("WRONG_REVIEW_RECENT".equals(entryMode)) {
            loadRecentWrongQuestions();
        } else {
            Toast.makeText(this,
                    "Chế độ chơi này chưa được hỗ trợ",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        txtQuestion = findViewById(R.id.txtQuestion);
        rvOptions = findViewById(R.id.recyclerOptions);
        btnLifelineHint = findViewById(R.id.btnLifelineHint);
        btnFinish = findViewById(R.id.btnFinish);
        btnSkip = findViewById(R.id.btnSkip);

        rvOptions.setLayoutManager(new GridLayoutManager(this, 2));
    }

    // ===============================
    // LOAD CÂU SAI GẦN ĐÂY
    // ===============================
    private void loadRecentWrongQuestions() {

        repository.getWrongAnswers()
                .enqueue(new Callback<WrongAnswersResponse>() {
                    @Override
                    public void onResponse(Call<WrongAnswersResponse> call,
                                           Response<WrongAnswersResponse> response) {

                        if (!response.isSuccessful()
                                || response.body() == null
                                || !response.body().success
                                || response.body().data == null
                                || response.body().data.isEmpty()) {

                            Toast.makeText(
                                    QuizActivity.this,
                                    "Không có câu sai để ôn tập",
                                    Toast.LENGTH_LONG
                            ).show();
                            finish();
                            return;
                        }

                        for (WrongAnswerItem item : response.body().data) {
                            questionList.add(mapToQuestion(item));
                        }

                        showQuestion();
                        setupButtons();
                    }

                    @Override
                    public void onFailure(Call<WrongAnswersResponse> call, Throwable t) {
                        Toast.makeText(
                                QuizActivity.this,
                                "Không tải được câu sai",
                                Toast.LENGTH_LONG
                        ).show();
                        finish();
                    }
                });
    }

    // ===============================
    // HIỂN THỊ CÂU HỎI
    // ===============================
    private void showQuestion() {
        if (currentIndex >= questionList.size()) {
            finishReview();
            return;
        }

        Question q = questionList.get(currentIndex);
        txtQuestion.setText(q.getQuestion_text());

        optionAdapter = new AnswerOptionAdapter(
                q.getOptions(),
                option -> onUserSelectOption(q, option)
        );

        rvOptions.setAdapter(optionAdapter);
    }

    private void onUserSelectOption(Question q, Option option) {
        q.setUser_selected_answer_id(option.getOption_id());

        if (q.isUserAnswerCorrect()) {
            Toast.makeText(this, "ĐÚNG!", Toast.LENGTH_SHORT).show();
        } else {
            Option correctOpt = q.getCorrectOption();
            Toast.makeText(
                    this,
                    "SAI! Đáp án đúng: " +
                            (correctOpt != null ? correctOpt.getOption_text() : "Không xác định"),
                    Toast.LENGTH_LONG
            ).show();
        }

        optionAdapter.notifyDataSetChanged();
    }

    private void setupButtons() {

        btnSkip.setOnClickListener(v -> {
            currentIndex++;
            showQuestion();
        });

        btnFinish.setOnClickListener(v -> finishReview());

        btnLifelineHint.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "Ôn tập không sử dụng trợ giúp",
                        Toast.LENGTH_SHORT
                ).show()
        );
    }

    private void finishReview() {
        Toast.makeText(
                this,
                "Hoàn thành ôn tập " + questionList.size() + " câu sai",
                Toast.LENGTH_LONG
        ).show();

        finish();
    }

    // ===============================
    // MAP WRONG → QUESTION
    // ===============================
    private Question mapToQuestion(WrongAnswerItem item) {

        Question q = new Question();

        q.setQuestion_text(item.cauHoi);
        q.setCorrect_answer_id(item.dapAnChinhXac);

        List<Option> options = new ArrayList<>();
        options.add(new Option("A", item.dapAnA));
        options.add(new Option("B", item.dapAnB));
        options.add(new Option("C", item.dapAnC));
        options.add(new Option("D", item.dapAnD));

        q.setOptions(options);

        return q;
    }
}
