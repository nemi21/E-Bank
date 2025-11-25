package com.ecommerce.service;

import com.ecommerce.exception.InsufficientStockException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.Transaction;
import com.ecommerce.model.User;
import com.ecommerce.model.Wallet;
import com.ecommerce.repository.TransactionRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public WalletService(WalletRepository walletRepository, UserRepository userRepository, TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Wallet createWallet(Long userId, Double initialBalance) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (walletRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException("Wallet already exists for this user");
        }

        Wallet wallet = new Wallet(user, initialBalance);
        Wallet savedWallet = walletRepository.save(wallet);

        if (initialBalance > 0) {
            Transaction transaction = new Transaction(
                    user,
                    Transaction.TransactionType.DEPOSIT,
                    initialBalance,
                    "Initial wallet balance",
                    initialBalance
            );
            transactionRepository.save(transaction);
        }

        return savedWallet;
    }

    public Wallet getWalletByUserId(Long userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user"));
    }

    @Transactional
    public Wallet deposit(Long userId, Double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        Wallet wallet = getWalletByUserId(userId);
        wallet.deposit(amount);
        Wallet updatedWallet = walletRepository.save(wallet);

        Transaction transaction = new Transaction(
                wallet.getUser(),
                Transaction.TransactionType.DEPOSIT,
                amount,
                "Deposit to wallet",
                updatedWallet.getBalance()
        );
        transactionRepository.save(transaction);

        return updatedWallet;
    }

    @Transactional
    public Wallet withdraw(Long userId, Double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        Wallet wallet = getWalletByUserId(userId);
        
        if (!wallet.hasSufficientBalance(amount)) {
            throw new InsufficientStockException("Insufficient wallet balance");
        }

        wallet.withdraw(amount);
        Wallet updatedWallet = walletRepository.save(wallet);

        Transaction transaction = new Transaction(
                wallet.getUser(),
                Transaction.TransactionType.WITHDRAWAL,
                amount,
                "Withdrawal from wallet",
                updatedWallet.getBalance()
        );
        transactionRepository.save(transaction);

        return updatedWallet;
    }

    public Double getBalance(Long userId) {
        Wallet wallet = getWalletByUserId(userId);
        return wallet.getBalance();
    }

    public List<Transaction> getTransactionHistory(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return transactionRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public boolean hasSufficientBalance(Long userId, Double amount) {
        Wallet wallet = getWalletByUserId(userId);
        return wallet.hasSufficientBalance(amount);
    }
}
