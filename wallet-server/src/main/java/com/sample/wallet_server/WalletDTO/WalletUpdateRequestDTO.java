package com.sample.wallet_server.WalletDTO;

/*
Request from PaymentService to update Corresponding wallet after payment Done
 */
public class WalletUpdateRequestDTO {
    private Long walletId;
    private double amount;

    WalletUpdateRequestDTO() {}

    WalletUpdateRequestDTO(Long userId, double amount) {
        this.walletId = userId;
        this.amount = amount;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Long getWalletId() {
        return walletId;
    }

    public double getAmount() {
        return amount;
    }
}
