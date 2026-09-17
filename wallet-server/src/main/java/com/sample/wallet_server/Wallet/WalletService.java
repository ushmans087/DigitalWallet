package com.sample.wallet_server.Wallet;

import com.sample.wallet_server.Bank.BankAccountEntity;
import com.sample.wallet_server.Bank.BankAccountRepo;
import com.sample.wallet_server.ExceptionClass.BankAccountNotFoundException;
import com.sample.wallet_server.ExceptionClass.TransferRequestRejectedException;
import com.sample.wallet_server.ExceptionClass.WalletNotFoundException;
import com.sample.wallet_server.Verification.VerificationService;
import com.sample.wallet_server.WalletDTO.PaymentResponseDTO;
import com.sample.wallet_server.WalletDTO.WalletUpdateRequestDTO;
import jakarta.transaction.Transactional;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WalletService {

    private final WalletRepo walletRepo;
    private final BankAccountRepo bankAccountRepo;
    private final VerificationService verificationService;
    private final CacheManager cacheManager;

    public WalletService(WalletRepo walletRepo, VerificationService verificationService, BankAccountRepo bankAccountRepo, CacheManager cacheManager) {
        this.walletRepo = walletRepo;
        this.verificationService = verificationService;
        this.bankAccountRepo = bankAccountRepo;
        this.cacheManager = cacheManager;
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
    @Caching(evict = {
            @CacheEvict(value = "walletByEmail" , key = "#p1"), // by email
            @CacheEvict(value = "bankAccountById" , key = "#p0") // by userId
    })
    public WalletEntity bankToWallet(Long userId,String email,Long accountId, double amount) {

        WalletEntity wallet = walletRepo.findByUserId(userId).orElseThrow(() -> new WalletNotFoundException("Wallet not found"));
        BankAccountEntity account = bankAccountRepo.findById(accountId).orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));

        if (!verificationService.verifyBankToWallet(wallet,account,amount)) {
            throw new TransferRequestRejectedException("Bank to wallet transfer rejected");
        }

        account.setBalance(account.getBalance() - amount);
        wallet.setBalance(wallet.getBalance() + amount);

        bankAccountRepo.save(account);
        walletRepo.save(wallet);

        return wallet;
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "walletByEmail" , key = "#p1"),
            @CacheEvict(value = "bankAccountById" , key = "#p0")
    })
    public WalletEntity walletToBank(Long userId,String email,double amount,Long accountId) {

        WalletEntity wallet = walletRepo.findByUserId(userId).orElseThrow(() -> new WalletNotFoundException("Wallet not found"));
        BankAccountEntity account = bankAccountRepo.findById(accountId).orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));

        if (!verificationService.verifyWalletToBank(wallet,account,amount)) {
            throw new TransferRequestRejectedException("Wallet to Bank transfer rejected");
        }

        wallet.setBalance(wallet.getBalance() - amount);
        account.setBalance(account.getBalance() + amount);

        walletRepo.save(wallet);
        bankAccountRepo.save(account);

        return wallet;
    }

    @Cacheable(value = "walletByEmail", key = "#p0")
    public PaymentResponseDTO getWalletByEmail(String email) {
        Optional<WalletEntity> wallet = walletRepo.findByEmail(email);

        if (wallet.isEmpty()) {
            throw new WalletNotFoundException("Wallet not found");
        }

        PaymentResponseDTO response = new PaymentResponseDTO(
                wallet.get().getUserId(), wallet.get().getId(), wallet.get().getBalance(),
                wallet.get().getEmail(), wallet.get().getUsername()
        );

        return response;
    }

    public PaymentResponseDTO getWalletByWalletId(Long walletId) {
        Optional<WalletEntity> wallet = walletRepo.findById(walletId);

        if (wallet.isEmpty()) {
            throw new WalletNotFoundException("Wallet not found");
        }

        PaymentResponseDTO response = new PaymentResponseDTO(
                wallet.get().getUserId(), wallet.get().getId(), wallet.get().getBalance(),
                wallet.get().getEmail(), wallet.get().getUsername()
        );

        return response;
    }

    //here there should be a transactional , so it returns a string , if after
    // the transaction will get saved in db in Transaction service , but here wrong

    @CacheEvict(value = "walletByEmail" , key = "#dto.getEmail()")
    public String updateBalance(WalletUpdateRequestDTO dto) {
        Optional<WalletEntity> wallet = walletRepo.findById(dto.getWalletId());

        if (wallet.isEmpty()) {
            throw new WalletNotFoundException("Wallet not found");
        }

        wallet.get().setBalance(dto.getAmount());
        walletRepo.save(wallet.get());

        return "Balance updated";
    }
}
