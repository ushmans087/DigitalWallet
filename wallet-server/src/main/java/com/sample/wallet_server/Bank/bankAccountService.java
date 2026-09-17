package com.sample.wallet_server.Bank;

import com.sample.wallet_server.BankDTO.BankAccountDTO;
import com.sample.wallet_server.BankDTO.BankAccountRequestDTO;
import com.sample.wallet_server.ExceptionClass.*;
import com.sample.wallet_server.Validator.ValidatorEntity;
import com.sample.wallet_server.Validator.ValidatorRepo;
import com.sample.wallet_server.Wallet.WalletRepo;
import com.sample.wallet_server.Wallet.WalletEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class bankAccountService {

    private final BankAccountRepo bankAccountRep;
    private final WalletRepo walletRepo;
    private final ValidatorRepo validatorRepo;

    public bankAccountService(BankAccountRepo bankAccountRepo, WalletRepo walletRepo, ValidatorRepo validatorRepo) {
        this.bankAccountRep = bankAccountRepo;
        this.walletRepo = walletRepo;
        this.validatorRepo = validatorRepo;
    }

    @Cacheable(value = "bankAccountById" , key = "#p0")
    public List<BankAccountDTO> getAllAccounts(Long userId) {
        WalletEntity wallet = walletRepo.findByUserId(userId).orElseThrow(() -> new RuntimeException("Wallet not found"));

        return wallet.getBankAccounts().stream().map(account -> new BankAccountDTO(
                        account.getId(), account.getBankName(), account.getAccountNumber(),
                        account.getBranch(), account.getIfscCode(), account.getAccountType(),
                        account.getBalance(), account.isVerified())).toList();
    }

    @CacheEvict(value = "bankAccountById" , key = "#p0")
    public BankAccountEntity addAccount(Long userId, BankAccountRequestDTO request) {

        WalletEntity wallet = walletRepo.findByUserId(userId).orElseThrow(() -> new WalletNotFoundException("Wallet not found"));

        if (request.getBankName() == null || request.getBankName().isBlank() || request.getAccountNumber() == null || request.getAccountNumber().isBlank()) throw new NotEnoughDetailsException("Insufficient Details");
        if (request.getIfscCode() == null || request.getIfscCode().isBlank() || request.getBranch() == null || request.getBranch().isBlank()) throw new NotEnoughDetailsException("Insufficient Details");
        if (request.getAccountType() == null || request.getAccountType().isBlank()) throw new NotEnoughDetailsException("Insufficient Details");

        BankAccountEntity account = new BankAccountEntity(request.getBankName(), request.getAccountNumber(), request.getBranch(), request.getIfscCode(), request.getAccountType(), wallet);
        wallet.addBankAccount(account);

        return bankAccountRep.save(account);
    }

    @CacheEvict(value = "bankAccountById",key = "#p0")
    public void deleteAccount(Long userId, Long accountId) {
        BankAccountEntity account = getUserAccount(userId, accountId);
        bankAccountRep.delete(account);
    }

    public String deposit(Long userId, Long accountId, double amount) {
        if (amount <= 0) throw new InvalidAmountException("Amount is Invalid or Insufficient! Please Check Amount");

        BankAccountEntity account = getUserAccount(userId, accountId);
        validatorRepo.save(new ValidatorEntity(userId,accountId,amount,"DEPOSIT"));
        return "submitted for verification";
    }

    public String withdraw(Long userId, Long accountId, double amount) {
        BankAccountEntity account = getUserAccount(userId, accountId);

        if (amount <= 0 || account.getBalance() < amount) throw new InvalidAmountException("Amount is Invalid or Insufficient! Please Check Amount");
        validatorRepo.save(new ValidatorEntity(userId,accountId,amount,"WITHDRAW"));
        return "submitted for verification";
    }

    private BankAccountEntity getUserAccount(Long userId, Long accountId) {

        WalletEntity wallet = walletRepo.findByUserId(userId).orElseThrow(() -> new WalletNotFoundException("Wallet not found"));
        BankAccountEntity account = bankAccountRep.findById(accountId).orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));

        // if any mismatch between the wallet and bank account
        if (!account.getWallet().getId().equals(wallet.getId())) throw new AccountMismatchException("This account does not belong to you");
        return account;
    }
}