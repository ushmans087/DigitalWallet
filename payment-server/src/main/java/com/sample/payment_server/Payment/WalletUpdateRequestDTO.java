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

    WalletUpdateRequestDTO(Long walletId, double amount) {
        this.walletId = walletId;
        this.amount = amount;
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
}
