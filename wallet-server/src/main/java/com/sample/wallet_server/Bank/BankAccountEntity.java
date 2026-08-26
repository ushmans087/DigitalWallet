package com.sample.wallet_server.Bank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sample.wallet_server.Wallet.WalletEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "bank_accounts")
@AllArgsConstructor
public class BankAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bankName;
    private String accountNumber;
    private String branch;
    private String ifscCode;
    private String accountType;
    private double balance;

    private boolean verified;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    @JsonIgnore
    private WalletEntity wallet;

    public BankAccountEntity() {
    }

    public BankAccountEntity(String bankName, String accountNumber, String branch, String ifscCode, String accountType, WalletEntity wallet) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.branch = branch;
        this.ifscCode = ifscCode;
        this.accountType = accountType;
        this.balance = 0.0;
        this.verified = true;
        this.wallet = wallet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public WalletEntity getWallet() {
        return wallet;
    }

    public void setWallet(WalletEntity wallet) {
        this.wallet = wallet;
    }
}