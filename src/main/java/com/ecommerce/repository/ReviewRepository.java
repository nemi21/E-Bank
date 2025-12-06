package com.ecommerce.repository;

import com.ecommerce.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    // Get all reviews for a product
    List<Review> findByProductId(Long productId);
    
    // Get all reviews by a user
    List<Review> findByUserId(Long userId);
    
    // Check if user already reviewed a product
    Optional<Review> findByUserIdAndProductId(Long userId, Long productId);
    
    // Calculate average rating for a product
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
    Double getAverageRatingByProductId(@Param("productId") Long productId);
    
    // Count reviews for a product
    Long countByProductId(Long productId);
}
