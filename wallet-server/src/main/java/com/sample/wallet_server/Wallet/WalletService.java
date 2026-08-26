package com.sample.wallet_server.Wallet;

import com.sample.wallet_server.Bank.BankAccountEntity;
import com.sample.wallet_server.Bank.BankAccountRepo;
import com.sample.wallet_server.Verification.VerificationService;
import com.sample.wallet_server.WalletDTO.PaymentResponseDTO;
import com.sample.wallet_server.WalletDTO.WalletUpdateRequestDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WalletService {

    private final WalletRepo walletRepo;
    private final BankAccountRepo bankAccountRepo;
    private final VerificationService verificationService;

    public WalletService(WalletRepo walletRepo, VerificationService verificationService, BankAccountRepo bankAccountRepo) {
        this.walletRepo = walletRepo;
        this.verificationService = verificationService;
        this.bankAccountRepo = bankAccountRepo;
    }

    public WalletEntity createWallet(Long userId, String username, String email) {
        Optional<WalletEntity> optional = walletRepo.findByUserId(userId);

        if (optional.isPresent()) {
            return optional.get();
        }

        WalletEntity wallet = new WalletEntity(username, email);
        wallet.setUserId(userId);
        wallet.setBalance(0.0);

        return walletRepo.save(wallet);
    }

    public WalletEntity getWallet(Long userId) {
        Optional<WalletEntity> optional = walletRepo.findByUserId(userId);
        if(optional.isEmpty())return null;

        return optional.get();
    }

    public double getBalance(Long userId) {
        Optional<WalletEntity> optional = walletRepo.findByUserId(userId);
        if(optional.isEmpty())return 0;

        return optional.get().getBalance();
    }

    @Transactional
    public WalletEntity bankToWallet(Long userId, Long accountId, double amount) {

        if (!verificationService.verifyBankToWallet(userId, accountId, amount)) {
            throw new RuntimeException("Bank to wallet transfer rejected");
        }

        WalletEntity wallet = walletRepo.findByUserId(userId).get();
        BankAccountEntity account = bankAccountRepo.findById(accountId).get();

        account.setBalance(account.getBalance() - amount);
        wallet.setBalance(wallet.getBalance() + amount);

        bankAccountRepo.save(account);
        walletRepo.save(wallet);

        return wallet;
    }

    @Transactional
    public WalletEntity walletToBank(Long userId,double amount,Long accountId) {

        if (!verificationService.verifyWalletToBank(userId, accountId, amount)) {
            throw new RuntimeException("Wallet to bank transfer rejected");
        }

        WalletEntity wallet = walletRepo.findByUserId(userId).get();
        BankAccountEntity account = bankAccountRepo.findById(accountId).get();

        wallet.setBalance(wallet.getBalance() - amount);
        account.setBalance(account.getBalance() + amount);

        walletRepo.save(wallet);
        bankAccountRepo.save(account);

        return wallet;
    }

    public PaymentResponseDTO getWalletByEmail(String email) {
        Optional<WalletEntity> wallet = walletRepo.findByEmail(email);

        if (wallet.isEmpty()) {
            throw new RuntimeException("Wallet not found");
        }

        PaymentResponseDTO response = new PaymentResponseDTO(
                wallet.get().getUserId(),
                wallet.get().getId(),
                wallet.get().getBalance(),
                wallet.get().getUsername()
        );

        return response;
    }
    public PaymentResponseDTO getWalletByWalletId(Long walletId) {

        Optional<WalletEntity> wallet = walletRepo.findById(walletId);

        if (wallet.isEmpty()) {
            throw new RuntimeException("Wallet not found");
        }

        PaymentResponseDTO response = new PaymentResponseDTO(
                wallet.get().getUserId(),
                wallet.get().getId(),
                wallet.get().getBalance(),
                wallet.get().getUsername()
        );

        return response;
    }
    public String updateBalance(WalletUpdateRequestDTO dto) {
        Optional<WalletEntity> wallet =
                walletRepo.findById(dto.getWalletId());

        if (wallet.isEmpty()) {
            return "Wallet not found";
        }

        wallet.get().setBalance(dto.getAmount());
        walletRepo.save(wallet.get());

        return "Balance updated";
    }
}
