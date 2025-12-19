package com.example.iq5.data.model;

import java.util.List;

public class PvPBattleResponse {
    public boolean success;
    public List<PvPBattleData> data;
    public String message;
    
    public static class PvPBattleData {
        public int TranDauID;
        public int NguoiChoi1ID;
        public Integer NguoiChoi2ID;
        public String NgayBatDau;
        public String NgayKetThuc;
        public String TrangThai;
        public Integer NguoiThangID;
        public Integer DiemNguoiChoi1;
        public Integer DiemNguoiChoi2;
        public String TenNguoiChoi1;
        public String TenNguoiChoi2;
    }
}