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
import com.example.iq5.feature.quiz.ui.LifelineDialogFragment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        txtQuestion = findViewById(R.id.txtQuestion);
        rvOptions = findViewById(R.id.recyclerOptions);
        btnLifelineHint = findViewById(R.id.btnLifelineHint);
        btnFinish = findViewById(R.id.btnFinish);
        btnSkip = findViewById(R.id.btnSkip);

        repository = new SpecialModeRepository(this);
        CurrentQuestionResponse data = repository.getCurrentQuestionData();
        helpOptions = repository.getLifelineOptions();

        if (data == null || data.getQuestion() == null) {
            txtQuestion.setText("Không thể load câu hỏi");
            return;
        }

        currentQuestion = data.getQuestion();
        questionList.add(currentQuestion);

        txtQuestion.setText(currentQuestion.getQuestion_text());

        rvOptions.setLayoutManager(new GridLayoutManager(this, 2));
        rvOptions.setAdapter(new AnswerOptionAdapter(currentQuestion.getOptions(), option -> {
            currentQuestion.setUser_selected_answer_id(option.getOption_id());

            if (option.getOption_id().equals(currentQuestion.getCorrect_answer_id())) {
                Toast.makeText(this, "ĐÚNG!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "SAI! Đáp án đúng: " + currentQuestion.getCorrect_answer_id(),
                        Toast.LENGTH_LONG).show();
            }

            rvOptions.getAdapter().notifyDataSetChanged();
        }));

        btnSkip.setOnClickListener(v -> {
            Toast.makeText(this, "Bạn đã bỏ qua câu hỏi", Toast.LENGTH_SHORT).show();
            currentQuestion.setUser_selected_answer_id(null);
        });

        btnFinish.setOnClickListener(v -> openReviewScreen());

        btnLifelineHint.setOnClickListener(v -> {
            if (helpOptions != null && helpOptions.getAvailableLifelines() != null) {
                LifelineDialogFragment dialog = new LifelineDialogFragment(helpOptions.getAvailableLifelines(), lifeline -> {
                    switch (lifeline.getType()) {
                        case "hint":
                            if (!lifeline.isUsedOnCurrentQuestion()) {
                                String hint = helpOptions.getHintContent() != null
                                        ? helpOptions.getHintContent().getText() : "Không có gợi ý";
                                Toast.makeText(this, "Gợi ý: " + hint, Toast.LENGTH_LONG).show();
                                lifeline.setUsedOnCurrentQuestion(true);
                                lifeline.setCountRemaining(lifeline.getCountRemaining() - 1);
                            }
                            break;
                        case "50:50":
                            if (!lifeline.isUsedOnCurrentQuestion()) {
                                apply5050(currentQuestion);
                                lifeline.setUsedOnCurrentQuestion(true);
                                lifeline.setCountRemaining(lifeline.getCountRemaining() - 1);
                            }
                            break;
                        case "skip":
                            if (!lifeline.isUsedOnCurrentQuestion()) {
                                currentQuestion.setUser_selected_answer_id(null);
                                lifeline.setUsedOnCurrentQuestion(true);
                                lifeline.setCountRemaining(lifeline.getCountRemaining() - 1);
                            }
                            break;
                    }
                    rvOptions.getAdapter().notifyDataSetChanged();
                });
                dialog.show(getSupportFragmentManager(), "LifelineDialog");
            }
        });
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
        rvOptions.getAdapter().notifyDataSetChanged();
    }

    private void openReviewScreen() {
        Intent intent = new Intent(this, ReviewQuestionActivity.class);
        intent.putExtra("questions", (Serializable) questionList);
        startActivity(intent);
    }
}
