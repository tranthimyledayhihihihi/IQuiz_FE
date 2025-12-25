package com.example.iq5.feature.multiplayer.data.models;

public class JoinRoomResponse {
    private String matchCode;
    private String message;
    private boolean success;
    private int opponentId;

    public JoinRoomResponse() {}

    public String getMatchCode() { return matchCode; }
    public void setMatchCode(String matchCode) { this.matchCode = matchCode; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public int getOpponentId() { return opponentId; }
    public void setOpponentId(int opponentId) { this.opponentId = opponentId; }
}