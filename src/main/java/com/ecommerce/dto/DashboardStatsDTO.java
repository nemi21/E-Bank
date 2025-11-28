package com.ecommerce.dto;

public class DashboardStatsDTO {
    private Long totalOrders;
    private Long totalUsers;
    private Long totalProducts;
    private Double totalRevenue;
    private Long pendingOrders;
    private Long paidOrders;
    private Long shippedOrders;

    public DashboardStatsDTO() {}

    public DashboardStatsDTO(Long totalOrders, Long totalUsers, Long totalProducts, Double totalRevenue,
                            Long pendingOrders, Long paidOrders, Long shippedOrders) {
        this.totalOrders = totalOrders;
        this.totalUsers = totalUsers;
        this.totalProducts = totalProducts;
        this.totalRevenue = totalRevenue;
        this.pendingOrders = pendingOrders;
        this.paidOrders = paidOrders;
        this.shippedOrders = shippedOrders;
    }

    // Getters and Setters
    public Long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public Long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(Long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Long getPendingOrders() {
        return pendingOrders;
    }

    public void setPendingOrders(Long pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

    public Long getPaidOrders() {
        return paidOrders;
    }

    public void setPaidOrders(Long paidOrders) {
        this.paidOrders = paidOrders;
    }

    public Long getShippedOrders() {
        return shippedOrders;
    }

    public void setShippedOrders(Long shippedOrders) {
        this.shippedOrders = shippedOrders;
    }
}
