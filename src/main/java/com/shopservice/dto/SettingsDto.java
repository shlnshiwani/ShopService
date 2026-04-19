package com.shopservice.dto;

import lombok.Data;

@Data
public class SettingsDto {
    private String username;
    private Boolean emailNotifications;
    private Boolean smsNotifications;
    private String theme;
    private String currency;
    private Integer itemsPerPage;
}
