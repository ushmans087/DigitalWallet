package com.sample.payment_server.Payment;

import lombok.NoArgsConstructor;

/*
Request to walletService to update the amount in corresponding users after payment
{
    walletId;
    amount;
}
 */

@NoArgsConstructor
public class WalletUpdateRequestDTO {
    private Long walletId;
    private double amount;
    private String email;

    WalletUpdateRequestDTO(Long walletId, double amount,String email) {
        this.walletId = walletId;
        this.amount = amount;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
