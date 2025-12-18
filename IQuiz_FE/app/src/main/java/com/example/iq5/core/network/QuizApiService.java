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
    
    // Bắt đầu làm bài quiz
    @POST("choi/start")
    Call<GameStartResponse> startQuiz(@Body GameStartOptions options);
    
    // Nộp đáp án
    @POST("choi/submit")
    Call<AnswerResponse> submitAnswer(@Body AnswerSubmit answer);
    
    // Lấy câu hỏi tiếp theo
    @GET("choi/next/{attemptId}")
    Call<Question> getNextQuestion(@Path("attemptId") int attemptId);
    
    // Kết thúc bài quiz
    @POST("choi/end/{attemptId}")
    Call<QuizResult> endQuiz(@Path("attemptId") int attemptId);
    
    // Lấy câu hỏi sai để ôn tập
    @GET("cauhoi/incorrect-review")
    Call<IncorrectQuestionsResponse> getIncorrectQuestions();
    
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
}