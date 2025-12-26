package com.example.iq5.core.network;

import com.example.iq5.data.model.AnswerSubmit;
import com.example.iq5.data.model.GameStartOptions;
import com.example.iq5.data.model.Question;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface QuizApiService {
    
    // Lấy danh sách chủ đề (categories)
    @GET("api/chude/with-stats")
    Call<java.util.List<CategoryResponse>> getCategories();
    
    // Test endpoint để kiểm tra backend
    @GET("api/test/categories")
    Call<java.util.List<CategoryResponse>> getTestCategories();
    
    // Bắt đầu làm bài quiz
    @POST("api/choi/start")
    Call<GameStartResponse> startQuiz(@Body GameStartOptions options);
    
    // Nộp đáp án
    @POST("api/choi/submit")
    Call<AnswerResponse> submitAnswer(@Body AnswerSubmit answer);
    
    // Lấy câu hỏi tiếp theo
    @GET("api/choi/next/{attemptId}")
    Call<Question> getNextQuestion(@Path("attemptId") int attemptId);
    
    // Kết thúc bài quiz
    @POST("api/choi/end/{attemptId}")
    Call<QuizResult> endQuiz(@Path("attemptId") int attemptId);
    
    // Lấy câu hỏi sai để ôn tập
    @GET("api/cauhoi/incorrect-review")
    Call<IncorrectQuestionsResponse> getIncorrectQuestions();
    
    // NEW: Lấy câu hỏi theo category (không cần authentication)
    @GET("api/testquiz/questions/{categoryId}")
    Call<com.example.iq5.data.model.SimpleQuizResponse> getQuestionsByCategory(@Path("categoryId") int categoryId);
    
    // Response classes
    public static class GameStartResponse {
        private int attemptID;
        private Question question;
        
        public int getAttemptID() { return attemptID; }
        public void setAttemptID(int attemptID) { this.attemptID = attemptID; }
        
        public Question getQuestion() { return question; }
        public void setQuestion(Question question) { this.question = question; }
    }
    
    public static class AnswerResponse {
        private boolean isCorrect;
        private String message;
        
        public boolean isCorrect() { return isCorrect; }
        public void setCorrect(boolean correct) { isCorrect = correct; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
    
    public static class QuizResult {
        private int quizAttemptID;
        private double diem;
        private int soCauDung;
        private int tongCauHoi;
        private String trangThaiKetQua;
        
        public int getQuizAttemptID() { return quizAttemptID; }
        public void setQuizAttemptID(int quizAttemptID) { this.quizAttemptID = quizAttemptID; }
        
        public double getDiem() { return diem; }
        public void setDiem(double diem) { this.diem = diem; }
        
        public int getSoCauDung() { return soCauDung; }
        public void setSoCauDung(int soCauDung) { this.soCauDung = soCauDung; }
        
        public int getTongCauHoi() { return tongCauHoi; }
        public void setTongCauHoi(int tongCauHoi) { this.tongCauHoi = tongCauHoi; }
        
        public String getTrangThaiKetQua() { return trangThaiKetQua; }
        public void setTrangThaiKetQua(String trangThaiKetQua) { this.trangThaiKetQua = trangThaiKetQua; }
    }
    
    public static class IncorrectQuestionsResponse {
        private int tongSoCauHoiSai;
        private java.util.List<Question> danhSach;
        
        public int getTongSoCauHoiSai() { return tongSoCauHoiSai; }
        public void setTongSoCauHoiSai(int tongSoCauHoiSai) { this.tongSoCauHoiSai = tongSoCauHoiSai; }
        
        public java.util.List<Question> getDanhSach() { return danhSach; }
        public void setDanhSach(java.util.List<Question> danhSach) { this.danhSach = danhSach; }
    }
    
    // Response class cho categories API
    public static class CategoryResponse {
        private int id;
        private String name;
        private String icon;
        private int quiz_count;
        private int progress_percent;
        
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }
        
        public int getQuiz_count() { return quiz_count; }
        public void setQuiz_count(int quiz_count) { this.quiz_count = quiz_count; }
        
        public int getProgress_percent() { return progress_percent; }
        public void setProgress_percent(int progress_percent) { this.progress_percent = progress_percent; }
    }
    
    // Response class cho TestQuiz API
    public static class TestQuizResponse {
        private boolean success;
        private String message;
        private int categoryId;
        private int totalQuestions;
        private java.util.List<TestQuestionModel> questions;
        
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public int getCategoryId() { return categoryId; }
        public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
        
        public int getTotalQuestions() { return totalQuestions; }
        public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }
        
        public java.util.List<TestQuestionModel> getQuestions() { return questions; }
        public void setQuestions(java.util.List<TestQuestionModel> questions) { this.questions = questions; }
    }
    
    public static class TestQuestionModel {
        private int id;
        private int categoryId;
        private String categoryName;
        private String difficulty;
        private String question;
        private String optionA;
        private String optionB;
        private String optionC;
        private String optionD;
        private String correctAnswer;
        private String image;
        
        // Getters and setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        
        public int getCategoryId() { return categoryId; }
        public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
        
        public String getCategoryName() { return categoryName; }
        public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
        
        public String getDifficulty() { return difficulty; }
        public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
        
        public String getQuestion() { return question; }
        public void setQuestion(String question) { this.question = question; }
        
        public String getOptionA() { return optionA; }
        public void setOptionA(String optionA) { this.optionA = optionA; }
        
        public String getOptionB() { return optionB; }
        public void setOptionB(String optionB) { this.optionB = optionB; }
        
        public String getOptionC() { return optionC; }
        public void setOptionC(String optionC) { this.optionC = optionC; }
        
        public String getOptionD() { return optionD; }
        public void setOptionD(String optionD) { this.optionD = optionD; }
        
        public String getCorrectAnswer() { return correctAnswer; }
        public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
        
        public String getImage() { return image; }
        public void setImage(String image) { this.image = image; }
    }
}