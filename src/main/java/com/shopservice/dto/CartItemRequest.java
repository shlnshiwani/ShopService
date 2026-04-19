package com.shopservice.dto;

import lombok.Data;

@Data
public class CartItemRequest {
    private String id;     // product id (e.g. "p1")
    private String name;
    private Double price;
}
