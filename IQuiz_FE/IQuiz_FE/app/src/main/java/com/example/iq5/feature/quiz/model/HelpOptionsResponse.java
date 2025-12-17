package com.example.iq5.feature.quiz.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class HelpOptionsResponse implements Serializable {

    @SerializedName("session_id")
    private String sessionId;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("available_lifelines")
    private List<Lifeline> availableLifelines; // Danh sách Lifeline

    @SerializedName("hint_content")
    private HintContent hintContent;

    // Constructor rỗng bắt buộc cho Gson
    public HelpOptionsResponse() {}

    // Constructor đầy đủ (nếu muốn khởi tạo thủ công)
    public HelpOptionsResponse(String sessionId, int userId, List<Lifeline> availableLifelines, HintContent hintContent) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.availableLifelines = availableLifelines;
        this.hintContent = hintContent;
    }

    // Getters
    public String getSessionId() { return sessionId; }
    public int getUserId() { return userId; }
    public List<Lifeline> getAvailableLifelines() { return availableLifelines; }
    public HintContent getHintContent() { return hintContent; }

    // Setters
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setAvailableLifelines(List<Lifeline> availableLifelines) { this.availableLifelines = availableLifelines; }
    public void setHintContent(HintContent hintContent) { this.hintContent = hintContent; }

    // Class con cho hint_content
    public static class HintContent implements Serializable {
        @SerializedName("is_requested")
        private boolean isRequested;

        @SerializedName("text")
        private String text;

        // Constructor rỗng
        public HintContent() {}

        // Constructor đầy đủ
        public HintContent(boolean isRequested, String text) {
            this.isRequested = isRequested;
            this.text = text;
        }

        // Getters
        public boolean isRequested() { return isRequested; }
        public String getText() { return text; }

        // Setters
        public void setRequested(boolean requested) { isRequested = requested; }
        public void setText(String text) { this.text = text; }
    }
}
