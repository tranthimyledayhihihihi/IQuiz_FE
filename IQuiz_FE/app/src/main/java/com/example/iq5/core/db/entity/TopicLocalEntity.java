package com.example.iq5.core.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "TopicLocalEntity")
public class TopicLocalEntity {

    @PrimaryKey
    @ColumnInfo(name = "chuDeId")
    private int chuDeId;

    @ColumnInfo(name = "tenChuDe") // Đã thêm trường bị thiếu
    private String tenChuDe;

    // 1. CONSTRUCTOR RỖNG (BẮT BUỘC, HOẶC CÓ THỂ BỎ QUA NẾU CHỈ DÙNG CONSTRUCTOR ĐẦY ĐỦ)
    public TopicLocalEntity() {
    }

    // 2. CONSTRUCTOR ĐẦY ĐỦ (KHẮC PHỤC LỖI CHÍNH)
    // PHẢI CHỨA TẤT CẢ CÁC FIELD
    @Ignore
    public TopicLocalEntity(int chuDeId, String tenChuDe) {
        this.chuDeId = chuDeId;
        this.tenChuDe = tenChuDe;
    }

    @Ignore
    public TopicLocalEntity(int chuDeId, String tenChuDe, Object o) {
    }

    // --- Getters và Setters (Room cần chúng) ---

    public int getChuDeId() { return chuDeId; }
    public void setChuDeId(int chuDeId) { this.chuDeId = chuDeId; }

    public String getTenChuDe() { return tenChuDe; }
    public void setTenChuDe(String tenChuDe) { this.tenChuDe = tenChuDe; }
}