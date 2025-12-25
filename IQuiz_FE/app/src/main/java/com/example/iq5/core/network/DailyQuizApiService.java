package com.example.iq5.core.network;

import com.example.iq5.data.model.AnswerSubmit;
import com.example.iq5.data.model.Question;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DailyQuizApiService {
    
    // Lấy quiz hàng ngày
    @GET("quizngay/today")
    Call<DailyQuizDetails> getTodayQuiz();
    
    // Bắt đầu làm quiz hàng ngày
    @POST("quizngay/start")
    Call<DailyQuizStartResponse> startTodayQuiz();
    
    // Nộp đáp án quiz hàng ngày
    @POST("quizngay/submit")
    Call<DailyQuizAnswerResponse> submitDailyAnswer(@Body AnswerSubmit answer);
    
    // Kết thúc quiz hàng ngày
    @POST("quizngay/end/{attemptId}")
    Call<DailyQuizResult> endTodayQuiz(@Path("attemptId") int attemptId);
    
    // Response classes
    public static class DailyQuizDetails {
        private int quizNgayID;
        private int cauHoiID;
        private String ngayTao;
        private String tieuDe;
        private String moTa;
        private Question cauHoi;
        
        public int getQuizNgayID() { return quizNgayID; }
        public void setQuizNgayID(int quizNgayID) { this.quizNgayID = quizNgayID; }
        
        public int getCauHoiID() { return cauHoiID; }
        public void setCauHoiID(int cauHoiID) { this.cauHoiID = cauHoiID; }
        
        public String getNgayTao() { return ngayTao; }
        public void setNgayTao(String ngayTao) { this.ngayTao = ngayTao; }
        
        public String getTieuDe() { return tieuDe; }
        public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }
        
        public String getMoTa() { return moTa; }
        public void setMoTa(String moTa) { this.moTa = moTa; }
        
        public Question getCauHoi() { return cauHoi; }
        public void setCauHoi(Question cauHoi) { this.cauHoi = cauHoi; }
    }
    
    public static class DailyQuizStartResponse {
        private int attemptID;
        private Question question;
        private String message;
        
        public int getAttemptID() { return attemptID; }
        public void setAttemptID(int attemptID) { this.attemptID = attemptID; }
        
        public Question getQuestion() { return question; }
        public void setQuestion(Question question) { this.question = question; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
    
    public static class DailyQuizAnswerResponse {
        private boolean isCorrect;
        private String message;
        
        public boolean isCorrect() { return isCorrect; }
        public void setCorrect(boolean correct) { isCorrect = correct; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
    
    public static class DailyQuizResult {
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
}