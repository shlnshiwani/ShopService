package com.shopservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SessionResponse {
    private String username;
    private String createdAt;
    private String expiresAt;
    private Boolean rememberMe;
    private String tokenPreview;
}
