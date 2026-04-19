package com.shopservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlaceOrderResponse {
    private String orderId;
    private OrderResponse order;
}
