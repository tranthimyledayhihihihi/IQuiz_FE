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
    
    @SerializedName("confirmNewPassword")
    private String confirmNewPassword;

    public ChangePasswordModel(String currentPassword, String newPassword, String confirmNewPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
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

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }
}
