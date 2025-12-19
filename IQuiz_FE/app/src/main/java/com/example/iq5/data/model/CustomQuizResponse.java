package com.example.iq5.data.model;

import java.util.List;

public class CustomQuizResponse {
    public boolean success;
    public List<CustomQuizData> data;
    public String message;
    
    public static class CustomQuizData {
        public int QuizTuyChinhID;
        public int UserID;
        public String TenQuiz;
        public String MoTa;
        public String NgayTao;
        public int SoLuongCauHoi;
        public int ThoiGianGioiHan;
        public String TenNguoiDung;
    }
}