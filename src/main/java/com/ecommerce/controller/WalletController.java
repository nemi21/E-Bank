package com.ecommerce.controller;

import com.ecommerce.dto.WalletTransactionDTO;
import com.ecommerce.model.Transaction;
import com.ecommerce.model.Wallet;
import com.ecommerce.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wallets")
@Tag(name = "Wallet", description = "E-Bank wallet management endpoints")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    @Operation(summary = "Create a new wallet for a user")
    public ResponseEntity<Wallet> createWallet(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0.0") Double initialBalance) {
        
        Wallet wallet = walletService.createWallet(userId, initialBalance);
        return ResponseEntity.created(URI.create("/wallets/" + wallet.getId())).body(wallet);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get wallet by user ID")
    public ResponseEntity<Wallet> getWalletByUserId(@PathVariable Long userId) {
        Wallet wallet = walletService.getWalletByUserId(userId);
        return ResponseEntity.ok(wallet);
    }

    @GetMapping("/user/{userId}/balance")
    @Operation(summary = "Get wallet balance")
    public ResponseEntity<Map<String, Double>> getBalance(@PathVariable Long userId) {
        Double balance = walletService.getBalance(userId);
        Map<String, Double> response = new HashMap<>();
        response.put("balance", balance);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/user/{userId}/deposit")
    @Operation(summary = "Deposit money into wallet")
    public ResponseEntity<Wallet> deposit(
            @PathVariable Long userId,
            @Valid @RequestBody WalletTransactionDTO transactionDTO) {
        
        Wallet wallet = walletService.deposit(userId, transactionDTO.getAmount());
        return ResponseEntity.ok(wallet);
    }

    @PostMapping("/user/{userId}/withdraw")
    @Operation(summary = "Withdraw money from wallet")
    public ResponseEntity<Wallet> withdraw(
            @PathVariable Long userId,
            @Valid @RequestBody WalletTransactionDTO transactionDTO) {
        
        Wallet wallet = walletService.withdraw(userId, transactionDTO.getAmount());
        return ResponseEntity.ok(wallet);
    }

    @GetMapping("/user/{userId}/transactions")
    @Operation(summary = "Get transaction history")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable Long userId) {
        List<Transaction> transactions = walletService.getTransactionHistory(userId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/user/{userId}/check-balance")
    @Operation(summary = "Check if user has sufficient balance")
    public ResponseEntity<Map<String, Object>> checkBalance(
            @PathVariable Long userId,
            @RequestParam Double amount) {
        
        boolean hasSufficient = walletService.hasSufficientBalance(userId, amount);
        Double currentBalance = walletService.getBalance(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("hasSufficientBalance", hasSufficient);
        response.put("currentBalance", currentBalance);
        response.put("requiredAmount", amount);
        
        return ResponseEntity.ok(response);
    }
}
