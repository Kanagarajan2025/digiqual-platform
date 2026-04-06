package com.digiqual.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    
    private boolean success;
    private String message;
    private String token;
    private String email;
    private String role;
    private String userId;
    
    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
