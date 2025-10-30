package com.example.quiz.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.quiz.model.Question;

import java.util.*;

public class DailyQuizViewModel extends ViewModel {
    private final MutableLiveData<Question> current = new MutableLiveData<>();
    private final List<Question> pool = new ArrayList<>();
    private final Random rng;
    private int score = 0;
    private int idx = 0;
    public boolean survivalMode = false;

    public DailyQuizViewModel() {
        long seed = getTodaySeed();
        rng = new Random(seed);
        seedQuestions();
        nextQuestion();
    }

    public LiveData<Question> getQuestion() { return current; }
    public int getScore() { return score; }
    public void resetScore() { score = 0; }

    public boolean answer(int optionIndex) {
        Question q = current.getValue();
        if (q == null) return false;
        boolean correct = optionIndex == q.answerIndex;
        if (correct) score += 10;
        return correct;
    }

    public Question nextQuestion() {
        if (pool.isEmpty()) seedQuestions();
        idx = (idx + 1) % pool.size();
        Question q = pool.get(idx);
        current.postValue(q);
        return q;
    }

    private void seedQuestions() {
        pool.clear();
        pool.add(new Question("q1","2 + 2 = ?", Arrays.asList("3","4","5","6"),1,"math"));
        pool.add(new Question("q2","Thủ đô Việt Nam?", Arrays.asList("Huế","Đà Nẵng","Hà Nội","Hải Phòng"),2,"geo"));
        pool.add(new Question("q3","Năm nhuận có bao nhiêu ngày?", Arrays.asList("365","366","367","364"),1,"calendar"));
        Collections.shuffle(pool, rng);
        idx = -1;
    }

    private long getTodaySeed() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR) * 10000L + (cal.get(Calendar.MONTH)+1)*100 + cal.get(Calendar.DAY_OF_MONTH);
    }
}
