package com.example.iq5.data.model;

import java.util.List;

public class DailyRewardResponse {
    public boolean success;
    public List<DailyRewardData> data;
    public String message;
    public boolean claimed;

    public static class DailyRewardData {
        public int ThuongID;
        public int UserID;
        public String NgayNhan;
        public String LoaiThuong;
        public int GiaTri;
        public String MoTa;
    }
}