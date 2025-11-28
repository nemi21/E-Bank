package com.ecommerce.controller;

import com.ecommerce.dto.OrderResponseDTO;
import com.ecommerce.dto.OrderItemResponseDTO;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderStatus;
import com.ecommerce.service.EnhancedOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/orders")
@Tag(name = "Admin Order Management", description = "Admin-only order management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class AdminOrderController {

    private final EnhancedOrderService orderService;

    public AdminOrderController(EnhancedOrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all orders (Admin only)")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders(
            @RequestParam(required = false) OrderStatus status) {
        
        List<Order> orders = orderService.getAllOrders();
        
        // Filter by status if provided
        if (status != null) {
            orders = orders.stream()
                    .filter(order -> order.getStatus() == status)
                    .collect(Collectors.toList());
        }
        
        List<OrderResponseDTO> dtos = orders.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update order status (Admin only)")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {
        
        Order order = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(convertToResponseDTO(order));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get orders by status (Admin only)")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByStatus(@PathVariable OrderStatus status) {
        List<Order> orders = orderService.getAllOrders().stream()
                .filter(order -> order.getStatus() == status)
                .collect(Collectors.toList());
        
        List<OrderResponseDTO> dtos = orders.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(dtos);
    }

    private OrderResponseDTO convertToResponseDTO(Order order) {
        List<OrderItemResponseDTO> itemDTOs = order.getOrderItems().stream().map(item -> {
            return new OrderItemResponseDTO(
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getPrice(),
                item.getQuantity()
            );
        }).collect(Collectors.toList());

        return new OrderResponseDTO(
            order.getId(),
            order.getUser().getId(),
            order.getCreatedAt(),
            order.getPaidAt(),
            order.getTotalPrice(),
            order.getStatus(),
            itemDTOs
        );
    }
}
