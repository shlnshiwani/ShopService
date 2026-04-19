package com.shopservice.service;

import com.shopservice.dto.ReviewRequest;
import com.shopservice.entity.Product;
import com.shopservice.entity.Review;
import com.shopservice.exception.ApiException;
import com.shopservice.repository.ProductRepository;
import com.shopservice.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepo;
    private final ReviewRepository reviewRepo;

    public List<Product> getAll() {
        return productRepo.findAll();
    }

    public Product getById(String id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new ApiException(404, "Product not found: " + id));
    }

    public List<Review> getReviews(String productId) {
        getById(productId); // validate product exists
        return reviewRepo.findByProductId(productId);
    }

    @Transactional
    public Review addReview(String productId, ReviewRequest req) {
        getById(productId); // validate product exists
        Review review = Review.builder()
                .productId(productId)
                .reviewerName(req.getReviewerName())
                .comment(req.getComment())
                .rating(req.getRating())
                .createdAt(Instant.now().toString())
                .build();
        return reviewRepo.save(review);
    }
}
