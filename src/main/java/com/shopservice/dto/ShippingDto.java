package com.shopservice.dto;

import lombok.Data;

@Data
public class ShippingDto {
    private String sessionToken;
    private String name;
    private String email;
    private String address;
    private String city;
    private String zip;
    private String country;
    private String deliveryDate;
}
