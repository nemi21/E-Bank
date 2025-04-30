package com.ecommerce.controller;

import com.ecommerce.model.Product;
import com.ecommerce.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    //Create a new Product
    @PostMapping
    public Product createProduct(@Valid @RequestBody Product product) {
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



