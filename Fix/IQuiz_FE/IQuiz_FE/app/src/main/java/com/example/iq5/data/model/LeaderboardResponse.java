package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Response cho bảng xếp hạng
 */
public class LeaderboardResponse {
    
    @SerializedName("type")
    public String type;
    
    @SerializedName("tongSoNguoi")
    public int tongSoNguoi;
    
    @SerializedName("trangHienTai")
    public int trangHienTai;
    
    @SerializedName("tongSoTrang")
    public int tongSoTrang;
    
    @SerializedName("danhSach")
    public List<RankingEntry> danhSach;
    
    public static class RankingEntry {
        @SerializedName("rank")
        public int rank;
        
        @SerializedName("userID")
        public int userID;
        
        @SerializedName("hoTen")
        public String hoTen;
        
        @SerializedName("anhDaiDien")
        public String anhDaiDien;
        
        @SerializedName("diem")
        public int diem;
        
        @SerializedName("soBaiLam")
        public int soBaiLam;
    }
}
