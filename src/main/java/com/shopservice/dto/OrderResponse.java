package com.shopservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderResponse {
    private String id;
    private String sessionToken;
    private List<CartItemResponse> items;
    private ShippingDto shipping;
    private Double total;
    private String placedAt;
}
