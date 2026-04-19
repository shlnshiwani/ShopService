package com.shopservice.dto;

import lombok.Data;

@Data
public class ProfileDto {
    private String username;
    private String displayName;
    private String contactEmail;
    private String avatar;
}
