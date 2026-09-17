package com.sample.payment_server.Transactions;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final TransferRepo transferRepo;
    public TransactionService(TransferRepo transferRepo) {
        this.transferRepo = transferRepo;
    }

    public Page<TransactionResponseDTO> getAllTransactions(Long userId, int pageNumber, int limit) {

        Pageable pageable = PageRequest.of(pageNumber-1, limit);
        Page<TransferEntity> transactions = transferRepo.findBySenderIdOrReceiverIdOrderByTimestampDesc(userId,userId,pageable);

        return transactions.map(this::convertToDTO);
    }

    private TransactionResponseDTO convertToDTO(TransferEntity transaction) {
        return new TransactionResponseDTO(
                transaction.getId(), transaction.getSenderWalletId(), transaction.getReceiverWalletId(),
                transaction.getSenderId(), transaction.getReceiverId(), transaction.getSenderUsername(),
                transaction.getReceiverUsername(), transaction.getAmount(), transaction.getTimestamp(),
                transaction.getStatus(), transaction.getDescription()
        );
    }

    public Page<TransactionResponseDTO> getRecentTransactions(Long userId,int pageNumber,int limit) {

        // here both will return [] empty list if no user find or no transaction done , so we cant able to differentiate

        Pageable pageable = PageRequest.of(pageNumber-1, limit);
        Page<TransferEntity> transactions = transferRepo.findBySenderIdOrReceiverIdOrderByTimestampDesc(userId,userId,pageable);

        return transactions.map(this::convertToDTO);
    }

    public List<TransactionResponseDTO> getAllTransactionsByFilter(Long userId, TransactionDateFilterDTO transactionFilterDTO) {
        LocalDateTime from = transactionFilterDTO.getFromDate();
        LocalDateTime to = transactionFilterDTO.getToDate();

        List<TransferEntity> transactions = transferRepo.findTransactions(userId, from, to);
        return transactions.stream().map(this::convertToDTO).toList();
    }
}