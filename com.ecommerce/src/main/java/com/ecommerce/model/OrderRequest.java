package com.ecommerce.model;

import java.util.List;

public class OrderRequest {

    private User user; // User making the order
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





