package com.shopservice.controller;

import com.shopservice.dto.OrderResponse;
import com.shopservice.dto.PlaceOrderResponse;
import com.shopservice.service.AuthService;
import com.shopservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final AuthService authService;

    @PostMapping
    public PlaceOrderResponse placeOrder(
            @RequestHeader("X-Session-Token") String tokenHeader) {
        authService.requireSession(tokenHeader);
        return orderService.placeOrder(tokenHeader);
    }

    @GetMapping
    public List<OrderResponse> getOrders(
            @RequestHeader("X-Session-Token") String tokenHeader) {
        authService.requireSession(tokenHeader);
        return orderService.getOrders(tokenHeader);
    }

    @GetMapping("/latest")
    public OrderResponse getLatest(
            @RequestHeader("X-Session-Token") String tokenHeader) {
        authService.requireSession(tokenHeader);
        return orderService.getLatest(tokenHeader);
    }
}
