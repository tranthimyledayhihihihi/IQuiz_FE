package com.example.iq5.core.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "users")
public class UserLocalEntity {
    @PrimaryKey
    private int userId;
    private String tenDangNhap;
    private String email;
    private String hoTen;
    private String vaiTro;
    private Date ngayTao;

    public UserLocalEntity(int userId, String tenDangNhap, String email, String hoTen, String vaiTro, Date ngayTao) {
        this.userId = userId;
        this.tenDangNhap = tenDangNhap;
        this.email = email;
        this.hoTen = hoTen;
        this.vaiTro = vaiTro;
        this.ngayTao = ngayTao;
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public String getTenDangNhap() { return tenDangNhap; }
    public String getEmail() { return email; }
    public String getHoTen() { return hoTen; }
    public String getVaiTro() { return vaiTro; }
    public Date getNgayTao() { return ngayTao; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }
    public void setEmail(String email) { this.email = email; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }
    public void setNgayTao(Date ngayTao) { this.ngayTao = ngayTao; }
}