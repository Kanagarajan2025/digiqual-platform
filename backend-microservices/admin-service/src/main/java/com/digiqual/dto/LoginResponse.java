package com.digiqual.dto;

public class LoginResponse {
    
    private boolean success;
    private String message;
    private String token;
    private String email;
    private String role;
    private String userId;

    public LoginResponse() {
    }

    public LoginResponse(boolean success, String message, String token, String email, String role, String userId) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.email = email;
        this.role = role;
        this.userId = userId;
    }
    
    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
