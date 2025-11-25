package com.ecommerce.model;

public enum OrderStatus {
    PENDING,      // Order created, not yet paid
    PAID,         // Payment successful
    PROCESSING,   // Order being prepared
    SHIPPED,      // Order shipped
    DELIVERED,    // Order delivered
    CANCELLED,    // Order cancelled
    REFUNDED      // Order refunded
}
