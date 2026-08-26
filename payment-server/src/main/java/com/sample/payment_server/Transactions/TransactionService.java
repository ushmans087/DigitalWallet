package com.sample.payment_server.Transactions;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final TransferRepo transferRepo;
    public TransactionService(TransferRepo transferRepo) {
        this.transferRepo = transferRepo;
    }

    public List<TransactionResponseDTO> getAllTransactions(Long walletId) {
        List<TransferEntity> transactions = transferRepo.findBySenderWalletId(walletId);

        return transactions.stream()
                .map(this::convertToDTO)
                .toList();
    }

    private TransactionResponseDTO convertToDTO(TransferEntity transaction) {

        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getSenderWalletId(),
                transaction.getReceiverWalletId(),
                transaction.getSenderId(),
                transaction.getReceiverId(),
                transaction.getSenderUsername(),
                transaction.getReceiverUsername(),
                transaction.getAmount(),
                transaction.getTimestamp(),
                transaction.getStatus(),
                transaction.getDescription()
        );
    }

    public List<TransactionResponseDTO> getRecentTransactions(Long walletId) {
        List<TransferEntity> transactions = transferRepo.findTop10BySenderWalletIdOrderByTimestampDesc(walletId);

        return transactions.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<TransactionResponseDTO> getAllTransactionsByFilter(Long userId, TransactionDateFilterDTO transactionFilterDTO) {
        LocalDateTime from = transactionFilterDTO.getFromDate();
        LocalDateTime to = transactionFilterDTO.getToDate();

        System.out.println(from);
        System.out.println(to);

        List<TransferEntity> transactions = transferRepo.findTransactions(from, to);

        System.out.println(transactions.toString());

        return transactions.stream()
                .map(this::convertToDTO)
                .toList();
    }
}