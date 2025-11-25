package com.ecommerce.controller;

import com.ecommerce.dto.OrderRequestDTO;
import com.ecommerce.dto.OrderResponseDTO;
import com.ecommerce.dto.OrderItemResponseDTO;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderStatus;
import com.ecommerce.service.EnhancedOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Order management with payment integration")
public class EnhancedOrderController {

    private final EnhancedOrderService orderService;

    public EnhancedOrderController(EnhancedOrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "Create a new order (PENDING status)")
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        Order order = orderService.createOrder(orderRequestDTO);
        return ResponseEntity.created(URI.create("/api/orders/" + order.getId()))
                .body(convertToResponseDTO(order));
    }

    @PostMapping("/create-and-pay")
    @Operation(summary = "Create order and pay immediately")
    public ResponseEntity<OrderResponseDTO> createAndPayOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        Order order = orderService.createAndPayOrder(orderRequestDTO);
        return ResponseEntity.created(URI.create("/api/orders/" + order.getId()))
                .body(convertToResponseDTO(order));
    }

    @PostMapping("/{orderId}/pay")
    @Operation(summary = "Pay for an existing order")
    public ResponseEntity<OrderResponseDTO> payForOrder(
            @PathVariable Long orderId,
            @RequestParam Long userId) {
        
        Order order = orderService.payForOrder(orderId, userId);
        return ResponseEntity.ok(convertToResponseDTO(order));
    }

    @PostMapping("/{orderId}/cancel")
    @Operation(summary = "Cancel an order (with refund if paid)")
    public ResponseEntity<OrderResponseDTO> cancelOrder(
            @PathVariable Long orderId,
            @RequestParam Long userId) {
        
        Order order = orderService.cancelOrder(orderId, userId);
        return ResponseEntity.ok(convertToResponseDTO(order));
    }

    @PutMapping("/{orderId}/status")
    @Operation(summary = "Update order status (Admin only)")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {
        
        Order order = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(convertToResponseDTO(order));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(convertToResponseDTO(order));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all orders for a user")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByUser(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        List<OrderResponseDTO> dtos = orders.stream().map(this::convertToResponseDTO).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping
    @Operation(summary = "Get all orders")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        List<OrderResponseDTO> dtos = orders.stream().map(this::convertToResponseDTO).toList();
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
        }).toList();

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
