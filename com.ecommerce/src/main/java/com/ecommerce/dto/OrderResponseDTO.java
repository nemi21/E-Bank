package com.ecommerce.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDTO {
    private Long orderId;
    private Long userId;
    private LocalDateTime createdAt;
    private double totalPrice;
    private List<OrderItemResponseDTO> orderItems;

    // Constructors
    public OrderResponseDTO() {}

    public OrderResponseDTO(Long orderId, Long userId, LocalDateTime createdAt, double totalPrice, List<OrderItemResponseDTO> orderItems) {
        this.orderId = orderId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.totalPrice = totalPrice;
        this.orderItems = orderItems;
    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<OrderItemResponseDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemResponseDTO> orderItems) {
        this.orderItems = orderItems;
    }
}

