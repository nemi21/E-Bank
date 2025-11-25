package com.ecommerce.service;

import com.ecommerce.dto.OrderItemDTO;
import com.ecommerce.dto.OrderRequestDTO;
import com.ecommerce.exception.InsufficientStockException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.*;
import com.ecommerce.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EnhancedOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public EnhancedOrderService(OrderRepository orderRepository, 
                                ProductRepository productRepository, 
                                UserRepository userRepository,
                                WalletRepository walletRepository,
                                TransactionRepository transactionRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Order createOrder(OrderRequestDTO orderRequestDTO) {
        User user = userRepository.findById(orderRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0.0;

        for (OrderItemDTO itemDTO : orderRequestDTO.getOrderItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            if (product.getStock() < itemDTO.getQuantity()) {
                throw new InsufficientStockException("Not enough stock for product: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPrice(product.getPrice());

            totalPrice += product.getPrice() * itemDTO.getQuantity();
            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice);

        return orderRepository.save(order);
    }

    @Transactional
    public Order payForOrder(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Order does not belong to this user");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("Order is not in PENDING status");
        }

        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user"));

        if (!wallet.hasSufficientBalance(order.getTotalPrice())) {
            throw new InsufficientStockException("Insufficient wallet balance. Required: " + 
                    order.getTotalPrice() + ", Available: " + wallet.getBalance());
        }

        wallet.withdraw(order.getTotalPrice());
        walletRepository.save(wallet);

        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus(OrderStatus.PAID);
        order.setPaidAt(LocalDateTime.now());
        Order paidOrder = orderRepository.save(order);

        Transaction transaction = new Transaction(
                order.getUser(),
                Transaction.TransactionType.PAYMENT,
                order.getTotalPrice(),
                "Payment for Order #" + order.getId(),
                wallet.getBalance()
        );
        transaction.setOrder(order);
        transactionRepository.save(transaction);

        return paidOrder;
    }

    @Transactional
    public Order createAndPayOrder(OrderRequestDTO orderRequestDTO) {
        Order order = createOrder(orderRequestDTO);
        return payForOrder(order.getId(), orderRequestDTO.getUserId());
    }

    @Transactional
    public Order cancelOrder(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Order does not belong to this user");
        }

        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalArgumentException("Cannot cancel delivered order");
        }

        if (order.getStatus() == OrderStatus.PAID || order.getStatus() == OrderStatus.PROCESSING) {
            Wallet wallet = walletRepository.findByUserId(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

            wallet.deposit(order.getTotalPrice());
            walletRepository.save(wallet);

            for (OrderItem item : order.getOrderItems()) {
                Product product = item.getProduct();
                product.setStock(product.getStock() + item.getQuantity());
                productRepository.save(product);
            }

            Transaction transaction = new Transaction(
                    order.getUser(),
                    Transaction.TransactionType.REFUND,
                    order.getTotalPrice(),
                    "Refund for cancelled Order #" + order.getId(),
                    wallet.getBalance()
            );
            transaction.setOrder(order);
            transactionRepository.save(transaction);

            order.setStatus(OrderStatus.REFUNDED);
        } else {
            order.setStatus(OrderStatus.CANCELLED);
        }

        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
