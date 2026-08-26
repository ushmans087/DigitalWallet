package com.sample.wallet_server.WalletDTO;

/*
Used for Inter - Transfering from (Wallet to Bank) and (Bank to Wallet)
senderId from JWT
{
    accountId; //bankaccountId
    amount;
}
 */

public class InterTransferRequestDTO {
    private double amount;
    private Long accountId;

    InterTransferRequestDTO() {}
    InterTransferRequestDTO(double amount, Long accountId) {
        this.amount = amount;
        this.accountId = accountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    public Long getAccountId() {
        return accountId;
    }
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
