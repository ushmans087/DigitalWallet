package com.sample.wallet_server.WalletDTO;

import lombok.NoArgsConstructor;

/*
Response to PaymentService for asking user details by (WalletId and UserEmail)
{
    userId;
    walletId;
    amount;
    username;
}
 */

@NoArgsConstructor
public class PaymentResponseDTO {

    private Long userId;
    private Long walletId;
    private double amount;
    private String email;
    private String username;

    public PaymentResponseDTO(Long userId, Long walletId, double amount, String email,String username) {
        this.userId = userId;
        this.walletId = walletId;
        this.amount = amount;
        this.username = username;
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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
}
