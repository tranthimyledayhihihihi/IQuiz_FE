package com.example.iq5.feature.quiz.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class QuizReviewResponse implements Serializable {

    @SerializedName("quiz_id")
    private int quizId;
    @SerializedName("quiz_title")
    private String quizTitle;
    @SerializedName("result_summary")
    private ResultSummary resultSummary;
    @SerializedName("review_questions")
    private List<ReviewQuestion> reviewQuestions;

    // Constructor rỗng (Bắt buộc cho Gson)
    public QuizReviewResponse() {}

    // Getters
    public List<ReviewQuestion> getReviewQuestions() { return reviewQuestions; }
    public int getQuizId() { return quizId; }
    public String getQuizTitle() { return quizTitle; }
    public ResultSummary getResultSummary() { return resultSummary; }


    // Model nội bộ cho result_summary
    public static class ResultSummary implements Serializable {
        @SerializedName("score")
        private String score;
        @SerializedName("status")
        private String status;
        @SerializedName("time_taken")
        private String timeTaken;

        public ResultSummary() {}

        public String getScore() { return score; }
        public String getStatus() { return status; }
        public String getTimeTaken() { return timeTaken; }
    }

    // Model nội bộ cho từng câu hỏi khi xem lại
    public static class ReviewQuestion implements Serializable {

        @SerializedName("id")
        private int id;
        @SerializedName("text")
        private String text;
        @SerializedName("user_answer_id")
        private String userAnswerId;
        @SerializedName("correct_answer_id")
        private String correctAnswerId;
        @SerializedName("is_correct")
        private boolean isCorrect;
        @SerializedName("explanation")
        private String explanation;

        @SerializedName("options")
        private List<Option> options; // ĐÃ SỬA LỖI: Dùng List<Option> (Model tùy chỉnh)

        public ReviewQuestion() {}

        // Triển khai tất cả Getters
        public int getId() { return id; }
        public String getText() { return text; }
        public String getUserAnswerId() { return userAnswerId; }
        public String getCorrectAnswerId() { return correctAnswerId; }
        public String getExplanation() { return explanation; }
        public List<Option> getOptions() { return options; }
    }
}