package com.example.iq5.data.model;

import java.util.List;

public class PvPAnswerResponse {
    public boolean success;
    public List<PvPAnswerData> data;
    public String message;
    
    public static class PvPAnswerData {
        public int TraLoiID;
        public int TranDauID;
        public int UserID;
        public int CauHoiID;
        public String DapAnChon;
        public int ThoiGianTraLoi;
        public boolean LaDapAnDung;
        public String TenNguoiDung;
        public String CauHoi;
        public String DapAnA;
        public String DapAnB;
        public String DapAnC;
        public String DapAnD;
        public String DapAnDung;
    }
}