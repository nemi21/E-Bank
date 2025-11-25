package com.ecommerce.dto;

import com.ecommerce.model.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDTO {
    private Long orderId;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
    private double totalPrice;
    private OrderStatus status;
    private List<OrderItemResponseDTO> orderItems;

    // Constructors
    public OrderResponseDTO() {}

    public OrderResponseDTO(Long orderId, Long userId, LocalDateTime createdAt, double totalPrice, List<OrderItemResponseDTO> orderItems) {
        this.orderId = orderId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.totalPrice = totalPrice;
        this.orderItems = orderItems;
        this.status = OrderStatus.PENDING;
    }

    public OrderResponseDTO(Long orderId, Long userId, LocalDateTime createdAt, LocalDateTime paidAt, 
                           double totalPrice, OrderStatus status, List<OrderItemResponseDTO> orderItems) {
        this.orderId = orderId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.paidAt = paidAt;
        this.totalPrice = totalPrice;
        this.status = status;
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

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderItemResponseDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemResponseDTO> orderItems) {
        this.orderItems = orderItems;
    }
}

