package com.example.iq5.data.model;

// Dùng cho endpoint POST /auth/register
public class RegisterRequest {
    private String tenDangNhap;
    private String email;
    private String matKhau;
    private String hoTen;
    private String vaiTro; // Thường là 'sinhvien' khi đăng ký

    // Constructor
    public RegisterRequest(String tenDangNhap, String email, String matKhau, String hoTen, String vaiTro) {
        this.tenDangNhap = tenDangNhap;
        this.email = email;
        this.matKhau = matKhau;
        this.hoTen = hoTen;
        this.vaiTro = vaiTro;
    }

    // Getters and Setters (bỏ qua cho ngắn gọn)
    // ...
}