package com.ecommerce.controller;

import java.net.URI;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dto.ProductDTO;
import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.ProductSearchService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	private ProductSearchService productSearchService;
	
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
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setImageUrl(productDTO.getImageUrl());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        return ResponseEntity.created(URI.create("/products/" + savedProduct.getId())).body(savedProduct);
    }



    
    //Update an existing Product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setImageUrl(productDTO.getImageUrl());

        if (productDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }

        return ResponseEntity.ok(productRepository.save(product));
    }

    
    //Delete a Product
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
    	productRepository.deleteById(id);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search products by keyword")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> products = productSearchService.quickSearch(keyword);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/filter")
    @Operation(summary = "Advanced product search with filters")
    public ResponseEntity<List<Product>> filterProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false, defaultValue = "name") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder) {
        
        List<Product> products = productSearchService.searchProducts(
            name, categoryId, minPrice, maxPrice, sortBy, sortOrder
        );
        
        return ResponseEntity.ok(products);
    }

    @GetMapping("/price-range")
    @Operation(summary = "Get products by price range")
    public ResponseEntity<List<Product>> getProductsByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        
        List<Product> products = productSearchService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/paginated")
    @Operation(summary = "Get products with pagination")
    public ResponseEntity<Page<Product>> getProductsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        
        Sort sort = sortOrder.equalsIgnoreCase("asc") 
            ? Sort.by(sortBy).ascending() 
            : Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> products = productRepository.findAll(pageable);
        
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/search/paginated")
    @Operation(summary = "Search products with pagination")
    public ResponseEntity<Map<String, Object>> searchProductsPaginated(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        List<Product> allResults = productSearchService.quickSearch(keyword);
        
        // Manual pagination
        int start = page * size;
        int end = Math.min(start + size, allResults.size());
        
        List<Product> pageContent = start < allResults.size() 
            ? allResults.subList(start, end) 
            : List.of();
        
        Map<String, Object> response = new HashMap<>();
        response.put("content", pageContent);
        response.put("currentPage", page);
        response.put("totalItems", allResults.size());
        response.put("totalPages", (int) Math.ceil((double) allResults.size() / size));
        response.put("pageSize", size);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/category/{categoryId}/paginated")
    @Operation(summary = "Get products by category with pagination")
    public ResponseEntity<Map<String, Object>> getProductsByCategoryPaginated(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        
        List<Product> allProducts = productRepository.findByCategoryId(categoryId);
        
        // Sort
        Comparator<Product> comparator = sortBy.equals("price") 
            ? Comparator.comparing(Product::getPrice)
            : Comparator.comparing(Product::getName);
        
        if (sortOrder.equalsIgnoreCase("desc")) {
            comparator = comparator.reversed();
        }
        
        allProducts.sort(comparator);
        
        // Paginate
        int start = page * size;
        int end = Math.min(start + size, allProducts.size());
        
        List<Product> pageContent = start < allProducts.size() 
            ? allProducts.subList(start, end) 
            : List.of();
        
        Map<String, Object> response = new HashMap<>();
        response.put("content", pageContent);
        response.put("currentPage", page);
        response.put("totalItems", allProducts.size());
        response.put("totalPages", (int) Math.ceil((double) allProducts.size() / size));
        response.put("pageSize", size);
        
        return ResponseEntity.ok(response);
    }
     
}
