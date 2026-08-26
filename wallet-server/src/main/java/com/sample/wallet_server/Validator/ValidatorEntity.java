package com.sample.wallet_server.Validator;

import jakarta.persistence.*;

@Entity
@Table(name = "validator_requests")
public class ValidatorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long bankAccountId;

    private double amount;

    private String type;      // DEPOSIT / WITHDRAW
    private String status;    // PENDING / VERIFIED / REJECTED

    public ValidatorEntity() {
    }

    public ValidatorEntity(Long userId,Long bankAccountId, double amount, String type) {
        this.userId = userId;
        this.bankAccountId = bankAccountId;
        this.amount = amount;
        this.type = type;
        this.status = "PENDING";
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getBankAccountId() {
        return bankAccountId;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setBankAccountId(Long bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}