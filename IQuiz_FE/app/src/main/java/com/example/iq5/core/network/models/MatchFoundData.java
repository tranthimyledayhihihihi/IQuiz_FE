package com.example.iq5.core.network.models;

public class MatchFoundData {
    private String matchCode;
    private int opponentId;
    private String yourRole;

    public String getMatchCode() { return matchCode; }
    public void setMatchCode(String matchCode) { this.matchCode = matchCode; }

    public int getOpponentId() { return opponentId; }
    public void setOpponentId(int opponentId) { this.opponentId = opponentId; }

    public String getYourRole() { return yourRole; }
    public void setYourRole(String yourRole) { this.yourRole = yourRole; }
}