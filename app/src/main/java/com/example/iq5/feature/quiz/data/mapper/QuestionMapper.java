package com.example.iq5.feature.quiz.data.mapper;

import com.example.iq5.feature.quiz.model.Question;
import com.example.iq5.feature.quiz.model.Category;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuestionMapper {

    public static Question fromJson(JSONObject obj) {
        try {
            int id = obj.getInt("id");
            String content = obj.getString("content");

            JSONArray arr = obj.getJSONArray("options");
            ArrayList<String> opts = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++)
                opts.add(arr.getString(i));

            String correct = obj.getString("correctAnswer");
            String explain = obj.optString("explanation", "");

            return new Question(id, content, opts, correct, explain);

        } catch (Exception e) {
            return null;
        }
    }
}
