package com.example.iq5.data.model;

public class PlayerAnswerDto {
    private int cauHoiId;
    private int nguoiDungId;
    private String luaChonCuaPlayer; // 'A', 'B', 'C', 'D'
    private long thoiGianTraLoiMs;

    // Constructor
    public PlayerAnswerDto(int cauHoiId, int nguoiDungId, String luaChonCuaPlayer, long thoiGianTraLoiMs) {
        this.cauHoiId = cauHoiId;
        this.nguoiDungId = nguoiDungId;
        this.luaChonCuaPlayer = luaChonCuaPlayer;
        this.thoiGianTraLoiMs = thoiGianTraLoiMs;
    }

    // Getters and Setters (bỏ qua cho ngắn gọn)
    // ...
}