package com.ecommerce.repository;

import com.ecommerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
//CategoryRepository interface that extends JpaRepository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}

