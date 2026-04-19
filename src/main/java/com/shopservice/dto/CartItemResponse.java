package com.shopservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItemResponse {
    private String id;     // productId exposed as "id" to match frontend contract
    private String name;
    private Double price;
    private Integer qty;
}
