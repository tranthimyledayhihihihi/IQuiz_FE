package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Model cho Change Password Request
 */
public class ChangePasswordModel {
    
    @SerializedName("currentPassword")
    private String currentPassword;
    
    @SerializedName("newPassword")
    private String newPassword;

    public ChangePasswordModel(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
