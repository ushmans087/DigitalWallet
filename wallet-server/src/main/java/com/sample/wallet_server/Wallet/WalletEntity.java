package com.sample.wallet_server.Wallet;

import com.sample.wallet_server.Bank.BankAccountEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wallets")
public class WalletEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private String username;
    private String email;
    private double balance;

    @OneToMany(mappedBy ="wallet",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<BankAccountEntity> bankAccounts = new ArrayList<>();

    public WalletEntity() {}

    public WalletEntity(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<BankAccountEntity> getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(List<BankAccountEntity> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    public void addBankAccount(BankAccountEntity bankAccount) {
        this.bankAccounts.add(bankAccount);
    }
}