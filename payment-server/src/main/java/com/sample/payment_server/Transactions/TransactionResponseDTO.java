package com.sample.payment_server.Transactions;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
Response for /get, /get/recent, /get/filter. to avoid direct contact between entity and user
{
    SAME AS TRANSFER ENTITY
}
 */

@NoArgsConstructor
public class TransactionResponseDTO {

    private Long transactionId;
    private Long senderWalletId;
    private Long receiverWalletId;
    private Long senderId;
    private Long receiverId;
    private String senderUsername;
    private String receiverUsername;
    private double amount;
    private LocalDateTime timestamp;
    private TransferStatus status;
    private String description;

    public TransactionResponseDTO(
            Long transactionId,
            Long senderWalletId,
            Long receiverWalletId,
            Long senderId,
            Long receiverId,
            String senderUsername,
            String receiverUsername,
            double amount,
            LocalDateTime timestamp,
            TransferStatus status,
            String description) {

        this.transactionId = transactionId;
        this.senderWalletId = senderWalletId;
        this.receiverWalletId = receiverWalletId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.amount = amount;
        this.timestamp = timestamp;
        this.status = status;
        this.description = description;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public Long getSenderWalletId() {
        return senderWalletId;
    }

    public Long getReceiverWalletId() {
        return receiverWalletId;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public void setSenderWalletId(Long senderWalletId) {
        this.senderWalletId = senderWalletId;
    }

    public void setReceiverWalletId(Long receiverWalletId) {
        this.receiverWalletId = receiverWalletId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
