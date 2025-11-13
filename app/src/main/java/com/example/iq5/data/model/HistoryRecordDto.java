package com.example.iq5.data.model;

import java.util.Date;

public class HistoryRecordDto {
    private int lichSuID;
    private String loaiDoiTuong; // NguoiDung/DoAn/NopBai...
    private int doiTuongID;
    private String hanhDong; // created/updated/deleted
    private String chiTiet;
    private Date thoiGian;

    // Constructor
    public HistoryRecordDto(int lichSuID, String loaiDoiTuong, int doiTuongID, String hanhDong, String chiTiet, Date thoiGian) {
        this.lichSuID = lichSuID;
        this.loaiDoiTuong = loaiDoiTuong;
        this.doiTuongID = doiTuongID;
        this.hanhDong = hanhDong;
        this.chiTiet = chiTiet;
        this.thoiGian = thoiGian;
    }

    // Getters and Setters (bỏ qua cho ngắn gọn)
    // ...
}