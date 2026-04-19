package com.shopservice.service;

import com.shopservice.dto.*;
import com.shopservice.entity.*;
import com.shopservice.exception.ApiException;
import com.shopservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HexFormat;
import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepo;
    private final CartRepository cartRepo;
    private final ShippingInfoRepository shippingRepo;

    @Transactional
    public PlaceOrderResponse placeOrder(String token) {
        Cart cart = cartRepo.findBySessionToken(token)
                .orElseThrow(() -> new ApiException(400, "Cart is empty"));
        if (cart.getItems().isEmpty())
            throw new ApiException(400, "Cart is empty");

        ShippingInfo shipping = shippingRepo.findBySessionToken(token).orElse(null);

        byte[] bytes = new byte[3];
        new SecureRandom().nextBytes(bytes);
        String orderId = "ORD-" + HexFormat.of().formatHex(bytes).toUpperCase();

        double total = cart.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQty())
                .sum();

        Order order = Order.builder()
                .id(orderId)
                .sessionToken(token)
                .total(Math.round(total * 100.0) / 100.0)
                .placedAt(Instant.now().toString())
                .shippingName(shipping != null ? shipping.getName() : null)
                .shippingEmail(shipping != null ? shipping.getEmail() : null)
                .shippingAddress(shipping != null ? shipping.getAddress() : null)
                .shippingCity(shipping != null ? shipping.getCity() : null)
                .shippingZip(shipping != null ? shipping.getZip() : null)
                .shippingCountry(shipping != null ? shipping.getCountry() : null)
                .shippingDeliveryDate(shipping != null ? shipping.getDeliveryDate() : null)
                .build();

        cart.getItems().forEach(cartItem -> {
            OrderItem oi = OrderItem.builder()
                    .order(order)
                    .productId(cartItem.getProductId())
                    .name(cartItem.getName())
                    .price(cartItem.getPrice())
                    .qty(cartItem.getQty())
                    .build();
            order.getItems().add(oi);
        });

        orderRepo.save(order);

        // Clear cart after order
        cart.getItems().clear();
        cartRepo.save(cart);

        return new PlaceOrderResponse(orderId, toResponse(order));
    }

    public List<OrderResponse> getOrders(String token) {
        return orderRepo.findBySessionTokenOrderByPlacedAtAsc(token)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public OrderResponse getLatest(String token) {
        List<Order> orders = orderRepo.findBySessionTokenOrderByPlacedAtAsc(token);
        if (orders.isEmpty()) return null;
        return toResponse(orders.get(orders.size() - 1));
    }

    private OrderResponse toResponse(Order o) {
        List<CartItemResponse> items = o.getItems().stream()
                .map(i -> new CartItemResponse(i.getProductId(), i.getName(), i.getPrice(), i.getQty()))
                .collect(Collectors.toList());

        ShippingDto ship = null;
        if (o.getShippingName() != null) {
            ship = new ShippingDto();
            ship.setSessionToken(o.getSessionToken());
            ship.setName(o.getShippingName());
            ship.setEmail(o.getShippingEmail());
            ship.setAddress(o.getShippingAddress());
            ship.setCity(o.getShippingCity());
            ship.setZip(o.getShippingZip());
            ship.setCountry(o.getShippingCountry());
            ship.setDeliveryDate(o.getShippingDeliveryDate());
        }
        return new OrderResponse(o.getId(), o.getSessionToken(), items, ship, o.getTotal(), o.getPlacedAt());
    }
}
