package com.example.iq5.feature.multiplayer.data.models;

public class JoinRoomRequest {
    private String roomCode;

    public JoinRoomRequest() {}

    public JoinRoomRequest(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getRoomCode() { return roomCode; }
    public void setRoomCode(String roomCode) { this.roomCode = roomCode; }
}