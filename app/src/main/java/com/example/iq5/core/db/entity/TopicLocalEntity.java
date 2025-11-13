package com.example.iq5.core.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "topics")
public class TopicLocalEntity {
    @PrimaryKey
    private int chuDeId;
    private String tenChuDe;
    private String moTa;

    public TopicLocalEntity(int chuDeId, String tenChuDe, String moTa) {
        this.chuDeId = chuDeId;
        this.tenChuDe = tenChuDe;
        this.moTa = moTa;
    }

    // Getters and Setters
    public int getChuDeId() { return chuDeId; }
    public String getTenChuDe() { return tenChuDe; }
    public String getMoTa() { return moTa; }
    public void setChuDeId(int chuDeId) { this.chuDeId = chuDeId; }
    public void setTenChuDe(String tenChuDe) { this.tenChuDe = tenChuDe; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
}