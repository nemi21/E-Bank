package com.ecommerce.controller;

import com.ecommerce.dto.ReviewRequestDTO;
import com.ecommerce.dto.ReviewResponseDTO;
import com.ecommerce.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
@Tag(name = "Product Reviews", description = "Product review and rating endpoints")
@SecurityRequirement(name = "bearerAuth")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    @Operation(summary = "Create a review for a product")
    public ResponseEntity<ReviewResponseDTO> createReview(
            @RequestParam Long userId,
            @Valid @RequestBody ReviewRequestDTO requestDTO) {
        
        ReviewResponseDTO review = reviewService.createReview(userId, requestDTO);
        return ResponseEntity.created(URI.create("/reviews/" + review.getId())).body(review);
    }

    @PutMapping("/{reviewId}")
    @Operation(summary = "Update your review")
    public ResponseEntity<ReviewResponseDTO> updateReview(
            @PathVariable Long reviewId,
            @RequestParam Long userId,
            @Valid @RequestBody ReviewRequestDTO requestDTO) {
        
        ReviewResponseDTO review = reviewService.updateReview(reviewId, userId, requestDTO);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "Delete your review")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId,
            @RequestParam Long userId) {
        
        reviewService.deleteReview(reviewId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get all reviews for a product")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByProduct(@PathVariable Long productId) {
        List<ReviewResponseDTO> reviews = reviewService.getReviewsByProduct(productId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all reviews by a user")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByUser(@PathVariable Long userId) {
        List<ReviewResponseDTO> reviews = reviewService.getReviewsByUser(userId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/product/{productId}/summary")
    @Operation(summary = "Get rating summary for a product")
    public ResponseEntity<Map<String, Object>> getProductRatingSummary(@PathVariable Long productId) {
        Map<String, Object> summary = reviewService.getProductRatingSummary(productId);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/{reviewId}")
    @Operation(summary = "Get review by ID")
    public ResponseEntity<ReviewResponseDTO> getReviewById(@PathVariable Long reviewId) {
        ReviewResponseDTO review = reviewService.getReviewById(reviewId);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/admin/{reviewId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete any review (Admin only)")
    public ResponseEntity<Void> deleteReviewByAdmin(@PathVariable Long reviewId) {
        reviewService.deleteReviewByAdmin(reviewId);
        return ResponseEntity.noContent().build();
    }
}
