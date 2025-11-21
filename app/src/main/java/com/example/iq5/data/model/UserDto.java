package com.example.iq5.data.model;

public class UserDto {
    private int userId;
    private String tenDangNhap;
    private String email;
    private String hoTen;
    private String vaiTro; // sinhvien/giangvien/admin

    // Cần Constructor và Getters/Setters đầy đủ
    public UserDto(int userId, String tenDangNhap, String email, String hoTen, String vaiTro) {
        this.userId = userId;
        this.tenDangNhap = tenDangNhap;
        this.email = email;
        this.hoTen = hoTen;
        this.vaiTro = vaiTro;
    }

    // Getters and Setters (bỏ qua cho ngắn gọn)
    // ...
}