package com.example.android.ui.friends;

public class FriendRequest {
    private String name;
    private String timeAgo;

    public FriendRequest(String name, String timeAgo) {
        this.name = name;
        this.timeAgo = timeAgo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }
}
