package com.example.iq5.feature.quiz.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WrongQuizResponse {

    @SerializedName("success")
    public boolean success;

    @SerializedName("questions")
    public List<WrongQuizQuestion> questions;
}
