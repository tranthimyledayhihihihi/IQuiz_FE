package com.example.iq5.feature.multiplayer.data.models;

public class OnlineCountResponse {
    private int onlineCount;
    private int playingCount;
    private int waitingCount;

    public OnlineCountResponse() {}

    public int getOnlineCount() { return onlineCount; }
    public void setOnlineCount(int onlineCount) { this.onlineCount = onlineCount; }

    public int getPlayingCount() { return playingCount; }
    public void setPlayingCount(int playingCount) { this.playingCount = playingCount; }

    public int getWaitingCount() { return waitingCount; }
    public void setWaitingCount(int waitingCount) { this.waitingCount = waitingCount; }
}