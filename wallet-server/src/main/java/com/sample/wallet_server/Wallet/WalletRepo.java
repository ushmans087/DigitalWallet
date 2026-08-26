package com.sample.wallet_server.Wallet;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WalletRepo extends JpaRepository<WalletEntity,Long> {
    Optional<WalletEntity> findByUserId(Long userId);
    Optional<WalletEntity> findByEmail(String email);
    Optional<WalletEntity> findById(Long walletId);
}
