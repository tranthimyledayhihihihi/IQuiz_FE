package com.example.iq5.core.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * API Service for submitting quiz results and updating achievements
 */
public interface QuizResultApiService {
    
    @POST("user/quizresult/submit")
    Call<QuizResultResponse> submitResult(@Body SubmitQuizResultRequest request);
    
    /**
     * Request model for submitting quiz result
     */
    class SubmitQuizResultRequest {
        private int tongCauHoi;
        private int soCauDung;
        private int categoryId;
        private int difficultyId;
        
        public SubmitQuizResultRequest(int tongCauHoi, int soCauDung, int categoryId, int difficultyId) {
            this.tongCauHoi = tongCauHoi;
            this.soCauDung = soCauDung;
            this.categoryId = categoryId;
            this.difficultyId = difficultyId;
        }
        
        // Getters and setters
        public int getTongCauHoi() { return tongCauHoi; }
        public void setTongCauHoi(int tongCauHoi) { this.tongCauHoi = tongCauHoi; }
        
        public int getSoCauDung() { return soCauDung; }
        public void setSoCauDung(int soCauDung) { this.soCauDung = soCauDung; }
        
        public int getCategoryId() { return categoryId; }
        public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
        
        public int getDifficultyId() { return difficultyId; }
        public void setDifficultyId(int difficultyId) { this.difficultyId = difficultyId; }
    }
    
    /**
     * Response model for quiz result submission
     */
    class QuizResultResponse {
        private boolean success;
        private String message;
        private QuizResultData result;
        
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public QuizResultData getResult() { return result; }
        public void setResult(QuizResultData result) { this.result = result; }
        
        public static class QuizResultData {
            private int diem;
            private int soCauDung;
            private int tongCauHoi;
            private int ketQuaId;
            
            public int getDiem() { return diem; }
            public void setDiem(int diem) { this.diem = diem; }
            
            public int getSoCauDung() { return soCauDung; }
            public void setSoCauDung(int soCauDung) { this.soCauDung = soCauDung; }
            
            public int getTongCauHoi() { return tongCauHoi; }
            public void setTongCauHoi(int tongCauHoi) { this.tongCauHoi = tongCauHoi; }
            
            public int getKetQuaId() { return ketQuaId; }
            public void setKetQuaId(int ketQuaId) { this.ketQuaId = ketQuaId; }
        }
    }
}