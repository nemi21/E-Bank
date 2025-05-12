package com.ecommerce.repository;

import com.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

	 @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    // Custom query method to find products by category ID
    List<Product> findByCategoryId(Long categoryId);
}


