package com.ecommerce.dto;

public class TopProductDTO {
    private Long productId;
    private String productName;
    private Long totalSold;
    private Double totalRevenue;

    public TopProductDTO() {}

    public TopProductDTO(Long productId, String productName, Long totalSold, Double totalRevenue) {
        this.productId = productId;
        this.productName = productName;
        this.totalSold = totalSold;
        this.totalRevenue = totalRevenue;
    }

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(Long totalSold) {
        this.totalSold = totalSold;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
