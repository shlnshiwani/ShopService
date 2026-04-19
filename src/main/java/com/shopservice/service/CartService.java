package com.shopservice.service;

import com.shopservice.dto.CartItemRequest;
import com.shopservice.dto.CartItemResponse;
import com.shopservice.entity.Cart;
import com.shopservice.entity.CartItem;
import com.shopservice.exception.ApiException;
import com.shopservice.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepo;

    // ── Read ────────────────────────────────────────────────────────────────

    public List<CartItemResponse> getItems(String token) {
        return cartRepo.findBySessionToken(token)
                .map(c -> toResponseList(c.getItems()))
                .orElse(List.of());
    }

    // ── Add ─────────────────────────────────────────────────────────────────

    @Transactional
    public List<CartItemResponse> addItem(String token, CartItemRequest req) {
        Cart cart = cartRepo.findBySessionToken(token)
                .orElseGet(() -> cartRepo.save(Cart.builder().sessionToken(token).build()));

        CartItem existing = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(req.getId()))
                .findFirst().orElse(null);

        if (existing != null) {
            existing.setQty(existing.getQty() + 1);
        } else {
            CartItem item = CartItem.builder()
                    .cart(cart)
                    .productId(req.getId())
                    .name(req.getName())
                    .price(req.getPrice())
                    .qty(1)
                    .sortOrder(cart.getItems().size())
                    .build();
            cart.getItems().add(item);
        }
        cartRepo.save(cart);
        return toResponseList(cart.getItems());
    }

    // ── Update qty ──────────────────────────────────────────────────────────

    @Transactional
    public List<CartItemResponse> updateQty(String token, String productId, int qty) {
        Cart cart = cartRepo.findBySessionToken(token)
                .orElseThrow(() -> new ApiException(404, "Cart not found"));
        cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .ifPresent(i -> i.setQty(Math.max(1, qty)));
        cartRepo.save(cart);
        return toResponseList(cart.getItems());
    }

    // ── Remove ──────────────────────────────────────────────────────────────

    @Transactional
    public List<CartItemResponse> removeItem(String token, String productId) {
        Cart cart = cartRepo.findBySessionToken(token).orElse(null);
        if (cart != null) {
            cart.getItems().removeIf(i -> i.getProductId().equals(productId));
            cartRepo.save(cart);
        }
        return cart != null ? toResponseList(cart.getItems()) : List.of();
    }

    // ── Reorder ─────────────────────────────────────────────────────────────

    @Transactional
    public List<CartItemResponse> reorder(String token, List<String> ids) {
        Cart cart = cartRepo.findBySessionToken(token)
                .orElseThrow(() -> new ApiException(404, "Cart not found"));

        Map<String, CartItem> byProductId = cart.getItems().stream()
                .collect(Collectors.toMap(CartItem::getProductId, i -> i));

        List<CartItem> reordered = new ArrayList<>();
        IntStream.range(0, ids.size()).forEach(idx -> {
            CartItem item = byProductId.get(ids.get(idx));
            if (item != null) {
                item.setSortOrder(idx);
                reordered.add(item);
            }
        });
        cart.getItems().clear();
        cart.getItems().addAll(reordered);
        cartRepo.save(cart);
        return toResponseList(cart.getItems());
    }

    // ── Clear (used by order placement) ─────────────────────────────────────

    @Transactional
    public void clearCart(String token) {
        cartRepo.findBySessionToken(token).ifPresent(cart -> {
            cart.getItems().clear();
            cartRepo.save(cart);
        });
    }

    // ── Helpers ─────────────────────────────────────────────────────────────

    private List<CartItemResponse> toResponseList(List<CartItem> items) {
        return items.stream()
                .map(i -> new CartItemResponse(i.getProductId(), i.getName(), i.getPrice(), i.getQty()))
                .collect(Collectors.toList());
    }
}
