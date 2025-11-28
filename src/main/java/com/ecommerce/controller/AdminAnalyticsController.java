package com.ecommerce.controller;

import com.ecommerce.dto.DashboardStatsDTO;
import com.ecommerce.dto.TopProductDTO;
import com.ecommerce.service.AdminAnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/analytics")
@Tag(name = "Admin Analytics", description = "Admin-only analytics and reporting endpoints")
@SecurityRequirement(name = "bearerAuth")
public class AdminAnalyticsController {

    private final AdminAnalyticsService analyticsService;

    public AdminAnalyticsController(AdminAnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get dashboard overview statistics (Admin only)")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        DashboardStatsDTO stats = analyticsService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/revenue")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get total revenue (Admin only)")
    public ResponseEntity<Map<String, Object>> getTotalRevenue() {
        Map<String, Object> revenue = analyticsService.getRevenueByPeriod();
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/products/top-selling")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get top-selling products (Admin only)")
    public ResponseEntity<List<TopProductDTO>> getTopSellingProducts(
            @RequestParam(defaultValue = "10") int limit) {
        List<TopProductDTO> topProducts = analyticsService.getTopSellingProducts(limit);
        return ResponseEntity.ok(topProducts);
    }
}