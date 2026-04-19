package com.shopservice.controller;

import com.shopservice.dto.ShippingDto;
import com.shopservice.service.AuthService;
import com.shopservice.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final AuthService authService;

    @GetMapping
    public ShippingDto getShipping(
            @RequestHeader("X-Session-Token") String tokenHeader) {
        authService.requireSession(tokenHeader);
        return checkoutService.getShipping(tokenHeader);
    }

    @PostMapping
    public void saveShipping(
            @RequestHeader("X-Session-Token") String tokenHeader,
            @RequestBody ShippingDto dto) {
        authService.requireSession(tokenHeader);
        dto.setSessionToken(tokenHeader);
        checkoutService.saveShipping(dto);
    }
}
