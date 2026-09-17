package com.sample.wallet_server.KafkaService;

import com.sample.wallet_server.Wallet.WalletService;
import com.sample.wallet_server.WalletDTO.UserRegisterDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    private final WalletService walletService;
    public ConsumerService(WalletService walletService) {
        this.walletService = walletService;
    }

    @KafkaListener(topics = "userRegister-event",groupId = "wallet-server")
    public void getUserRegisterEvent(UserRegisterDTO userRegisterDTO) {

        Long userId = userRegisterDTO.getUserId();
        String username = userRegisterDTO.getUsername();
        String email = userRegisterDTO.getEmail();

        walletService.createWallet(userId, username, email);
    }
}
