package com.example.iq5.feature.quiz.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SelectionScreenResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private SelectionData data;

    public SelectionScreenResponse() {} // Cần thiết cho Gson

    public SelectionData getData() { return data; }

    // Model nội bộ: Khớp với trường "data"
    public static class SelectionData {
        @SerializedName("sections")
        private List<SectionItem> sections;

        public List<SectionItem> getSections() { return sections; }
    }

    // Model nội bộ: Khớp với các khối "categories" và "difficulty"
    public static class SectionItem {
        @SerializedName("type")
        private String type;
        @SerializedName("title")
        private String title;

        @SerializedName("items")
        private List<Object> items; // BẮT BUỘC: Parse thô thành List<Object>

        public String getType() { return type; }
        public List<Object> getItems() { return items; }
    }
}