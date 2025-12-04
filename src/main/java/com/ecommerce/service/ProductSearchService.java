package com.ecommerce.service;

import com.ecommerce.model.Product;
import com.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductSearchService {

    private final ProductRepository productRepository;

    public ProductSearchService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> searchProducts(String name, Long categoryId, Double minPrice, 
                                       Double maxPrice, String sortBy, String sortOrder) {
        
        // Get filtered products
        List<Product> products = productRepository.advancedSearch(name, categoryId, minPrice, maxPrice);
        
        // Apply sorting
        if (sortBy != null && !sortBy.isEmpty()) {
            products = sortProducts(products, sortBy, sortOrder);
        }
        
        return products;
    }

    public List<Product> quickSearch(String keyword) {
        return productRepository.searchByKeyword(keyword);
    }

    public List<Product> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        return productRepository.findByPriceRange(minPrice, maxPrice);
    }

    private List<Product> sortProducts(List<Product> products, String sortBy, String sortOrder) {
        Comparator<Product> comparator;
        
        switch (sortBy.toLowerCase()) {
            case "price":
                comparator = Comparator.comparing(Product::getPrice);
                break;
            case "name":
                comparator = Comparator.comparing(Product::getName);
                break;
            case "stock":
                comparator = Comparator.comparing(Product::getStock);
                break;
            default:
                return products; // No sorting
        }
        
        // Reverse if descending
        if ("desc".equalsIgnoreCase(sortOrder)) {
            comparator = comparator.reversed();
        }
        
        return products.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}
