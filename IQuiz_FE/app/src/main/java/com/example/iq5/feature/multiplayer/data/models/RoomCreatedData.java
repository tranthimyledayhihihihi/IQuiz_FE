package com.example.iq5.feature.multiplayer.data.models;

public class RoomCreatedData {
    private String roomCode;

    public RoomCreatedData() {}

    public RoomCreatedData(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }
}