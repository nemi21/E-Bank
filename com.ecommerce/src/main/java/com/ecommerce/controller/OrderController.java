package com.ecommerce.controller;

import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
import com.ecommerce.model.OrderRequest;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.OrderItemRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderController(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // Create an Order
    @PostMapping
    @Transactional
    public Order createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        // Find the user
        User user = userRepository.findById(orderRequest.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create new order
        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        Double totalPrice = 0.0;

        // Build OrderItems
        for (OrderItem itemRequest : orderRequest.getOrderItems()) {
            Product product = productRepository.findById(itemRequest.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(product.getPrice());

            totalPrice += product.getPrice() * itemRequest.getQuantity();
            orderItems.add(orderItem);
        }

        order.setTotalPrice(totalPrice);
        order.setOrderItems(orderItems);

        return orderRepository.save(order);
    }

    // Get Order by ID
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
    
    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUser(@PathVariable Long userId) {
        return orderRepository.findByUserId(userId);
    }


    // Get all Orders
    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
