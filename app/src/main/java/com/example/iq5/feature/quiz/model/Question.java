package com.example.iq5.feature.quiz.model;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    private String question_text;
    private String correct_answer_id;
    private String user_selected_answer_id;
    private List<Option> options;

    public String getQuestion_text() {
        return question_text;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }

    public String getCorrect_answer_id() {
        return correct_answer_id;
    }

    public void setCorrect_answer_id(String correct_answer_id) {
        this.correct_answer_id = correct_answer_id;
    }

    public String getUser_selected_answer_id() {
        return user_selected_answer_id;
    }

    public void setUser_selected_answer_id(String user_selected_answer_id) {
        this.user_selected_answer_id = user_selected_answer_id;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
}
