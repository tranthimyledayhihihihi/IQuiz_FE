package com.example.iq5.data.model;

public class TopicDto {
    private int chuDeId;
    private String tenChuDe;

    // Constructor mặc định (Gson thường cần cái này)
    public TopicDto() {
    }

    // Constructor đầy đủ tham số
    public TopicDto(int chuDeId, String tenChuDe) {
        this.chuDeId = chuDeId;
        this.tenChuDe = tenChuDe;
    }

    // GETTERS
    public int getChuDeId() {
        return chuDeId;
    }

    public String getTenChuDe() {
        return tenChuDe;
    }

    // SETTERS
    public void setChuDeId(int chuDeId) {
        this.chuDeId = chuDeId;
    }

    public void setTenChuDe(String tenChuDe) {
        this.tenChuDe = tenChuDe;
    }
}