package com.shopservice.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
    private Boolean rememberMe;

    // Session-sync fields (sent by Node.js dual-mode when creating a session)
    private String token;
    private String createdAt;
    private String expiresAt;
}
