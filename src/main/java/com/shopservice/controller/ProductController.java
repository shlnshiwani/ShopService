package com.shopservice.controller;

import com.shopservice.dto.ReviewRequest;
import com.shopservice.entity.Product;
import com.shopservice.entity.Review;
import com.shopservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product catalogue and reviews")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Get all products", description = "Returns all 12 products in the catalogue.")
    @ApiResponse(responseCode = "200", description = "Product list returned")
    public List<Product> getAll() {
        return productService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product found"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public Product getById(
            @Parameter(description = "Product ID, e.g. p1", required = true)
            @PathVariable String id) {
        return productService.getById(id);
    }

    @GetMapping("/{id}/reviews")
    @Operation(summary = "Get reviews for a product")
    @ApiResponse(responseCode = "200", description = "Reviews returned")
    public List<Review> getReviews(
            @Parameter(description = "Product ID", required = true)
            @PathVariable String id) {
        return productService.getReviews(id);
    }

    @PostMapping("/{id}/reviews")
    @Operation(summary = "Add a review to a product")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Review added"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public Review addReview(
            @Parameter(description = "Product ID", required = true)
            @PathVariable String id,
            @RequestBody ReviewRequest req) {
        return productService.addReview(id, req);
    }
}
