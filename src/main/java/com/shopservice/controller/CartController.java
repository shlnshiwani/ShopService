package com.shopservice.controller;

import com.shopservice.dto.CartItemRequest;
import com.shopservice.dto.CartItemResponse;
import com.shopservice.dto.ReorderRequest;
import com.shopservice.dto.QtyRequest;
import com.shopservice.service.AuthService;
import com.shopservice.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Shopping cart — add, update, remove and reorder items")
@SecurityRequirement(name = "SessionToken")
public class CartController {

    private final CartService cartService;
    private final AuthService authService;

    private String token(String header) {
        authService.requireSession(header);
        return header;
    }

    @GetMapping
    @Operation(summary = "Get cart items for the current session")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cart items returned"),
        @ApiResponse(responseCode = "401", description = "Invalid session token")
    })
    public List<CartItemResponse> getCart(
            @Parameter(hidden = true) @RequestHeader("X-Session-Token") String tokenHeader) {
        return cartService.getItems(token(tokenHeader));
    }

    @PostMapping
    @Operation(summary = "Add a product to the cart")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Item added, updated cart returned"),
        @ApiResponse(responseCode = "401", description = "Invalid session token")
    })
    public List<CartItemResponse> addItem(
            @Parameter(hidden = true) @RequestHeader("X-Session-Token") String tokenHeader,
            @RequestBody CartItemRequest req) {
        return cartService.addItem(token(tokenHeader), req);
    }

    @PutMapping("/reorder")
    @Operation(summary = "Reorder cart items (drag-and-drop persistence)",
               description = "Accepts the full ordered list of product IDs and persists the new sort order.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reordered cart returned"),
        @ApiResponse(responseCode = "401", description = "Invalid session token")
    })
    public List<CartItemResponse> reorder(
            @Parameter(hidden = true) @RequestHeader("X-Session-Token") String tokenHeader,
            @RequestBody ReorderRequest req) {
        return cartService.reorder(token(tokenHeader), req.getIds());
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Update quantity for a cart item")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Updated cart returned"),
        @ApiResponse(responseCode = "401", description = "Invalid session token"),
        @ApiResponse(responseCode = "404", description = "Product not in cart")
    })
    public List<CartItemResponse> updateQty(
            @Parameter(hidden = true) @RequestHeader("X-Session-Token") String tokenHeader,
            @Parameter(description = "Product ID", required = true) @PathVariable String productId,
            @RequestBody QtyRequest req) {
        return cartService.updateQty(token(tokenHeader), productId, req.getQty());
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Remove a product from the cart")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Updated cart returned"),
        @ApiResponse(responseCode = "401", description = "Invalid session token")
    })
    public List<CartItemResponse> removeItem(
            @Parameter(hidden = true) @RequestHeader("X-Session-Token") String tokenHeader,
            @Parameter(description = "Product ID", required = true) @PathVariable String productId) {
        return cartService.removeItem(token(tokenHeader), productId);
    }
}
