package com.exoapp.models.login;

public class LoginResponse {
    private Long userId;
    private String username;
    private int role;
    private boolean needsPasswordChange;

    public LoginResponse() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public boolean isNeedsPasswordChange() {
        return needsPasswordChange;
    }

    public void setNeedsPasswordChange(boolean needsPasswordChange) {
        this.needsPasswordChange = needsPasswordChange;
    }

    public String getUsername() {
        return username;
    }

    public int getRole() {
        return role;
    }

}

