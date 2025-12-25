package com.example.iq5.feature.multiplayer.data.models;

public class CreateRoomResponse {
    private String roomCode;
    private String message;
    private boolean success;

    public CreateRoomResponse() {}

    public String getRoomCode() { return roomCode; }
    public void setRoomCode(String roomCode) { this.roomCode = roomCode; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
}