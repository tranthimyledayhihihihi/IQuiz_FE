package com.example.iq5.data.model;

import java.util.List;

public class WrongQuestionResponse {
    public boolean success;
    public List<WrongQuestionData> data;
    public String message;
    
    public static class WrongQuestionData {
        public int CauSaiID;
        public int UserID;
        public int CauHoiID;
        public String DapAnSai;
        public String DapAnDung;
        public String NgayTao;
        public String CauHoi;
        public String DapAnA;
        public String DapAnB;
        public String DapAnC;
        public String DapAnD;
        public String DapAnChinhXac;
        public String TenChuDe;
    }
}