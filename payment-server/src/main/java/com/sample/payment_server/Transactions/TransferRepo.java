package com.sample.payment_server.Transactions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransferRepo extends JpaRepository<TransferEntity, Long> {

        List<TransferEntity> findBySenderWalletId(Long senderWalletId);

        Page<TransferEntity> findBySenderIdOrReceiverIdOrderByTimestampDesc(
                Long senderWalletId,
                Long receiverWalletId,
                Pageable pageable
        );

        @Query("""
            SELECT t
            FROM TransferEntity t
            WHERE t.timestamp >= :from
              AND t.timestamp < :to
              AND (t.senderId = :userId OR t.receiverId = :userId)
            ORDER BY t.timestamp DESC
        """)
        List<TransferEntity> findTransactions(Long userId,LocalDateTime from, LocalDateTime to);
}
