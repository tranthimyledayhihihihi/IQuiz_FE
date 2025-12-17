package com.example.iq5.feature.quiz.model;

public class CurrentQuestionResponse {

    private String quiz_session_id;
    private int current_question_index;
    private int total_questions;
    private int time_remaining_seconds;
    private Question question;

    public String getQuiz_session_id() {
        return quiz_session_id;
    }

    public int getCurrent_question_index() {
        return current_question_index;
    }

    public int getTotal_questions() {
        return total_questions;
    }

    public int getTime_remaining_seconds() {
        return time_remaining_seconds;
    }

    public Question getQuestion() {
        return question;
    }
}
