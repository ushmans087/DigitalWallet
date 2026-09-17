package com.sample.payment_server.Payment;

import lombok.NoArgsConstructor;

/*
Response from walletService containing user Details retrieved by (walletId,UserEmail)
{
    userId;
    walletId;
    amount;
    username;
}
 */

@NoArgsConstructor
public class WalletResponseDTO {
    private Long userId;
    private Long walletId;
    private double amount;
    private String email;
    private String username;

    WalletResponseDTO(Long userId, Long walletId, double amount, String username,String email) {
        this.userId = userId;
        this.walletId = walletId;
        this.amount = amount;
        this.email = email;
        this.username = username;
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
