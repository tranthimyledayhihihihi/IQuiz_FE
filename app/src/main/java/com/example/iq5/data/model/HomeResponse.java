package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Response model cho API Home
 * Tương ứng với dữ liệu trong home.json
 */
public class HomeResponse {
    
    @SerializedName("welcomeMessage")
    private String welcomeMessage;
    
    @SerializedName("featuredQuizzes")
    private List<FeaturedQuiz> featuredQuizzes;

    // Constructors
    public HomeResponse() {}

    public HomeResponse(String welcomeMessage, List<FeaturedQuiz> featuredQuizzes) {
        this.welcomeMessage = welcomeMessage;
        this.featuredQuizzes = featuredQuizzes;
    }

    // Getters & Setters
    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public List<FeaturedQuiz> getFeaturedQuizzes() {
        return featuredQuizzes;
    }

    public void setFeaturedQuizzes(List<FeaturedQuiz> featuredQuizzes) {
        this.featuredQuizzes = featuredQuizzes;
    }

    /**
     * Model cho mỗi quiz trong danh sách featured
     */
    public static class FeaturedQuiz {
        @SerializedName("id")
        private int id;
        
        @SerializedName("title")
        private String title;
        
        @SerializedName("difficulty")
        private String difficulty;

        // Constructors
        public FeaturedQuiz() {}

        public FeaturedQuiz(int id, String title, String difficulty) {
            this.id = id;
            this.title = title;
            this.difficulty = difficulty;
        }

        // Getters & Setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDifficulty() {
            return difficulty;
        }

        public void setDifficulty(String difficulty) {
            this.difficulty = difficulty;
        }
    }
}
