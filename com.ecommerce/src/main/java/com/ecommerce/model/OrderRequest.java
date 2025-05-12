package com.ecommerce.model;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class OrderRequest {

	@NotNull(message = "User is required")
    private User user; // User making the order
	
	@NotNull(message = "Order items are required")
    @Size(min = 1, message = "At least one order item is required")
    @Valid
    private List<OrderItem> orderItems; // List of order items

    // Getters and Setters
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}


