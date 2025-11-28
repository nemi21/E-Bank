package com.ecommerce.service;

import com.ecommerce.dto.DashboardStatsDTO;
import com.ecommerce.dto.TopProductDTO;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderStatus;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminAnalyticsService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public AdminAnalyticsService(OrderRepository orderRepository, 
                                 UserRepository userRepository, 
                                 ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public DashboardStatsDTO getDashboardStats() {
        List<Order> allOrders = orderRepository.findAll();
        
        Long totalOrders = (long) allOrders.size();
        Long totalUsers = userRepository.count();
        Long totalProducts = productRepository.count();
        
        // Calculate total revenue (only from PAID orders)
        Double totalRevenue = allOrders.stream()
                .filter(order -> order.getStatus() == OrderStatus.PAID || 
                                order.getStatus() == OrderStatus.PROCESSING ||
                                order.getStatus() == OrderStatus.SHIPPED ||
                                order.getStatus() == OrderStatus.DELIVERED)
                .mapToDouble(Order::getTotalPrice)
                .sum();
        
        // Count orders by status
        Long pendingOrders = allOrders.stream()
                .filter(order -> order.getStatus() == OrderStatus.PENDING)
                .count();
        
        Long paidOrders = allOrders.stream()
                .filter(order -> order.getStatus() == OrderStatus.PAID)
                .count();
        
        Long shippedOrders = allOrders.stream()
                .filter(order -> order.getStatus() == OrderStatus.SHIPPED)
                .count();

        return new DashboardStatsDTO(
                totalOrders,
                totalUsers,
                totalProducts,
                totalRevenue,
                pendingOrders,
                paidOrders,
                shippedOrders
        );
    }

    public List<TopProductDTO> getTopSellingProducts(int limit) {
        List<Order> paidOrders = orderRepository.findAll().stream()
                .filter(order -> order.getStatus() == OrderStatus.PAID ||
                                order.getStatus() == OrderStatus.PROCESSING ||
                                order.getStatus() == OrderStatus.SHIPPED ||
                                order.getStatus() == OrderStatus.DELIVERED)
                .collect(Collectors.toList());

        // Calculate sales per product
        Map<Long, TopProductStats> productStats = new HashMap<>();

        for (Order order : paidOrders) {
            order.getOrderItems().forEach(item -> {
                Long productId = item.getProduct().getId();
                String productName = item.getProduct().getName();
                int quantity = item.getQuantity();
                double revenue = item.getPrice() * quantity;

                productStats.computeIfAbsent(productId, 
                    k -> new TopProductStats(productId, productName))
                    .addSale(quantity, revenue);
            });
        }

        // Convert to DTO and sort by total sold
        return productStats.values().stream()
                .sorted((a, b) -> Long.compare(b.totalSold, a.totalSold))
                .limit(limit)
                .map(stats -> new TopProductDTO(
                    stats.productId,
                    stats.productName,
                    stats.totalSold,
                    stats.totalRevenue
                ))
                .collect(Collectors.toList());
    }

    public Double getTotalRevenue() {
        return orderRepository.findAll().stream()
                .filter(order -> order.getStatus() == OrderStatus.PAID ||
                                order.getStatus() == OrderStatus.PROCESSING ||
                                order.getStatus() == OrderStatus.SHIPPED ||
                                order.getStatus() == OrderStatus.DELIVERED)
                .mapToDouble(Order::getTotalPrice)
                .sum();
    }

    public Map<String, Object> getRevenueByPeriod() {
        List<Order> paidOrders = orderRepository.findAll().stream()
                .filter(order -> order.getStatus() == OrderStatus.PAID ||
                                order.getStatus() == OrderStatus.PROCESSING ||
                                order.getStatus() == OrderStatus.SHIPPED ||
                                order.getStatus() == OrderStatus.DELIVERED)
                .collect(Collectors.toList());

        Double totalRevenue = paidOrders.stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();

        Map<String, Object> result = new HashMap<>();
        result.put("totalRevenue", totalRevenue);
        result.put("totalOrders", paidOrders.size());
        result.put("averageOrderValue", paidOrders.isEmpty() ? 0 : totalRevenue / paidOrders.size());

        return result;
    }

    // Helper class to track product statistics
    private static class TopProductStats {
        Long productId;
        String productName;
        Long totalSold = 0L;
        Double totalRevenue = 0.0;

        TopProductStats(Long productId, String productName) {
            this.productId = productId;
            this.productName = productName;
        }

        void addSale(int quantity, double revenue) {
            this.totalSold += quantity;
            this.totalRevenue += revenue;
        }
    }
}
