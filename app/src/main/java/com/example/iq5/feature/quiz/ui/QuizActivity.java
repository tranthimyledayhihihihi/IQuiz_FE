package com.example.iq5.feature.quiz.ui;

import android.content.Intent;
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
import com.example.iq5.feature.quiz.data.SpecialModeRepository;
import com.example.iq5.feature.quiz.model.CurrentQuestionResponse;
import com.example.iq5.feature.quiz.model.HelpOptionsResponse;
import com.example.iq5.feature.quiz.model.Lifeline;
import com.example.iq5.feature.quiz.model.Option;
import com.example.iq5.feature.quiz.model.Question;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView txtQuestion;
    private RecyclerView rvOptions;
    private ImageButton btnLifelineHint;
    private Button btnFinish, btnSkip;

    private SpecialModeRepository repository;
    private Question currentQuestion;
    private List<Question> questionList = new ArrayList<>();
    private HelpOptionsResponse helpOptions;

    private AnswerOptionAdapter optionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        initViews();

        repository = new SpecialModeRepository(this);
        CurrentQuestionResponse data = repository.getCurrentQuestionData();
        helpOptions = repository.getLifelineOptions();

        if (data == null || data.getQuestion() == null) {
            txtQuestion.setText("Không thể tải câu hỏi!");
            return;
        }

        currentQuestion = data.getQuestion();
        questionList.add(currentQuestion);

        bindQuestion();
        setupOptionAdapter();
        setupButtons();
    }

    private void initViews() {
        txtQuestion = findViewById(R.id.txtQuestion);
        rvOptions = findViewById(R.id.recyclerOptions);
        btnLifelineHint = findViewById(R.id.btnLifelineHint);
        btnFinish = findViewById(R.id.btnFinish);
        btnSkip = findViewById(R.id.btnSkip);
    }

    private void bindQuestion() {
        txtQuestion.setText(currentQuestion.getQuestion_text());
    }

    private void setupOptionAdapter() {

        rvOptions.setLayoutManager(new GridLayoutManager(this, 2));

        optionAdapter = new AnswerOptionAdapter(
                currentQuestion.getOptions(),
                option -> onUserSelectOption(option)
        );

        rvOptions.setAdapter(optionAdapter);
    }

    private void onUserSelectOption(Option option) {
        currentQuestion.setUser_selected_answer_id(option.getOption_id());

        if (currentQuestion.isUserAnswerCorrect()) {
            Toast.makeText(this, "ĐÚNG!", Toast.LENGTH_SHORT).show();
        } else {
            Option correctOpt = currentQuestion.getCorrectOption();
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
            Toast.makeText(this, "Bạn đã bỏ qua câu hỏi", Toast.LENGTH_SHORT).show();
            currentQuestion.clearUserSelection();
            optionAdapter.notifyDataSetChanged();
        });

        btnFinish.setOnClickListener(v -> openReviewScreen());

        btnLifelineHint.setOnClickListener(v -> handleLifeline());
    }

    private void handleLifeline() {

        if (helpOptions == null || helpOptions.getAvailableLifelines() == null) {
            Toast.makeText(this, "Không có trợ giúp!", Toast.LENGTH_SHORT).show();
            return;
        }

        LifelineDialogFragment dialog = new LifelineDialogFragment(
                helpOptions.getAvailableLifelines(),
                lifeline -> applyLifeline(lifeline)
        );

        dialog.show(getSupportFragmentManager(), "LifelineDialog");
    }

    private void applyLifeline(Lifeline lifeline) {
        if (lifeline.isUsedOnCurrentQuestion()) {
            Toast.makeText(this, "Bạn đã dùng trợ giúp cho câu này rồi!", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (lifeline.getType()) {

            case "hint":
                String hint = helpOptions.getHintContent() != null
                        ? helpOptions.getHintContent().getText()
                        : "Không có gợi ý";
                Toast.makeText(this, "Gợi ý: " + hint, Toast.LENGTH_LONG).show();
                break;

            case "50:50":
                apply5050(currentQuestion);
                break;

            case "skip":
                currentQuestion.clearUserSelection();
                Toast.makeText(this, "Bạn đã bỏ qua câu hỏi!", Toast.LENGTH_SHORT).show();
                break;
        }

        lifeline.setUsedOnCurrentQuestion(true);
        lifeline.setCountRemaining(lifeline.getCountRemaining() - 1);

        optionAdapter.notifyDataSetChanged();
    }

    private void apply5050(Question q) {
        List<Option> options = q.getOptions();
        String correctId = q.getCorrect_answer_id();

        int hiddenCount = 0;

        for (Option opt : options) {
            if (!opt.getOption_id().equals(correctId) && hiddenCount < 2) {
                opt.setHidden(true);
                hiddenCount++;
            }
        }
    }

    private void openReviewScreen() {
        Intent intent = new Intent(this, ReviewQuestionActivity.class);
        intent.putExtra("questions", (Serializable) questionList);
        startActivity(intent);
    }
}
