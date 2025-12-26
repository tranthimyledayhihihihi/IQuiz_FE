package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;

public class QuizSubmitRequest {

    @SerializedName("submission")
    private QuizSubmissionModel submission;

    public QuizSubmitRequest(QuizSubmissionModel submission) {
        this.submission = submission;
    }

    public QuizSubmissionModel getSubmission() {
        return submission;
    }

    public void setSubmission(QuizSubmissionModel submission) {
        this.submission = submission;
    }
}
