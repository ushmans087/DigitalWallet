package com.sample.payment_server.Transactions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransferRepo extends JpaRepository<TransferEntity, Long> {

        List<TransferEntity> findBySenderWalletId(Long senderWalletId);
        List<TransferEntity> findTop10BySenderWalletIdOrderByTimestampDesc(
                Long senderWalletId
        );

        @Query("""
            SELECT t
            FROM TransferEntity t
            WHERE t.timestamp >= :from
              AND t.timestamp < :to
            ORDER BY t.timestamp DESC
        """)
        List<TransferEntity> findTransactions(LocalDateTime from, LocalDateTime to);
}
