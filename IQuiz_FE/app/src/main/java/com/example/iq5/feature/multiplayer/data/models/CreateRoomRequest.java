package com.example.iq5.feature.multiplayer.data.models;

public class CreateRoomRequest {
    private int soCauHoi;
    private int doKhoID;

    public CreateRoomRequest() {}

    public CreateRoomRequest(int soCauHoi, int doKhoID) {
        this.soCauHoi = soCauHoi;
        this.doKhoID = doKhoID;
    }

    public int getSoCauHoi() { return soCauHoi; }
    public void setSoCauHoi(int soCauHoi) { this.soCauHoi = soCauHoi; }

    public int getDoKhoID() { return doKhoID; }
    public void setDoKhoID(int doKhoID) { this.doKhoID = doKhoID; }
}