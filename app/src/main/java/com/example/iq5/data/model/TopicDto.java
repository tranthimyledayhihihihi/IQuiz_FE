package com.example.iq5.data.model;

public class TopicDto {
    private int chuDeId;
    private String tenChuDe;

    // THIẾU Constructor
    public TopicDto(int chuDeId, String tenChuDe) {
        this.chuDeId = chuDeId;
        this.tenChuDe = tenChuDe;
    }

    // GETTERS (ĐÃ SỬA LỖI MISSING RETURN STATEMENT)

    public int getChuDeId() {
        return chuDeId; // <-- Bổ sung return
    }

    public String getTenChuDe() {
        return tenChuDe; // <-- Bổ sung return
    }

    // SETTERS (Cần thiết để Gson/Retrofit hoạt động tốt hơn)

    public void setChuDeId(int chuDeId) {
        this.chuDeId = chuDeId;
    }

    public void setTenChuDe(String tenChuDe) {
        this.tenChuDe = tenChuDe;
    }
}