package com.ecommerce.controller;

import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductController(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content if no products
        }
        return ResponseEntity.ok(products); // 200 OK with products
    }


    
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    //Create a new Product
    @PostMapping
    public Product createProduct(@Valid @RequestBody Product product) {
        Long categoryId = product.getCategory().getId(); // Extract category ID from request
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        product.setCategory(category); // Assign managed category to the product
        return productRepository.save(product);
    }

    
    //Update an existing Product
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @Valid @RequestBody Product updateProduct) {
    	Product product = productRepository.findById(id)
    			.orElseThrow(() -> new RuntimeException("Product not found"));
    	
    	product.setName(updateProduct.getName());
    	product.setDescription(updateProduct.getDescription());
    	product.setPrice(updateProduct.getPrice());
    	product.setStock(updateProduct.getStock());
    	product.setImageUrl(updateProduct.getImageUrl());
    	
    	return productRepository.save(product);
    }
    
    //Delete a Product
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
    	productRepository.deleteById(id);
    }
     
}
