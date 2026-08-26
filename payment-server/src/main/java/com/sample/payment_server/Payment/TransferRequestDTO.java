package com.sample.payment_server.Payment;

import lombok.NoArgsConstructor;

/*
Request from Client containing Amount and receiverId to Transfer
{
    receiverWalletId;
    amount;
    description;
}
 */

@NoArgsConstructor
public class TransferRequestDTO {

    private Long receiverWalletId;
    private double amount;
    private String description;

    public TransferRequestDTO(Long receiverWalletId, double amount, String description) {
        this.receiverWalletId = receiverWalletId;
        this.amount = amount;
        this.description = description;
    }

    public Long getReceiverWalletId() {
        return receiverWalletId;
    }

    public void setReceiverWalletId(Long receiverWalletId) {
        this.receiverWalletId = receiverWalletId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}