package com.sample.wallet_server.Bank;

import com.sample.wallet_server.BankDTO.BankAccountRequestDTO;
import com.sample.wallet_server.Validator.ValidatorEntity;
import com.sample.wallet_server.Validator.ValidatorRepo;
import com.sample.wallet_server.Wallet.WalletRepo;
import com.sample.wallet_server.Wallet.WalletEntity;
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

    public List<BankAccountEntity> getAllAccounts(Long userId) {
        WalletEntity wallet = walletRepo.findByUserId(userId).orElseThrow(() -> new RuntimeException("Wallet not found"));
        return wallet.getBankAccounts();
    }

    public BankAccountEntity addAccount(Long userId, BankAccountRequestDTO request) {
        WalletEntity wallet = walletRepo.findByUserId(userId).orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (request.getBankName() == null || request.getBankName().isBlank()) throw new RuntimeException("Bank name is required");
        if (request.getAccountNumber() == null || request.getAccountNumber().isBlank()) throw new RuntimeException("Account number is required");
        if (request.getIfscCode() == null || request.getIfscCode().isBlank()) throw new RuntimeException("IFSC code is required");
        if (request.getBranch() == null || request.getBranch().isBlank()) throw new RuntimeException("Branch is required");
        if (request.getAccountType() == null || request.getAccountType().isBlank()) throw new RuntimeException("Account type is required");

        BankAccountEntity account = new BankAccountEntity(request.getBankName(), request.getAccountNumber(), request.getBranch(), request.getIfscCode(), request.getAccountType(), wallet);
        wallet.addBankAccount(account);

        return bankAccountRep.save(account);
    }

    public void deleteAccount(Long userId, Long accountId) {
        BankAccountEntity account = getUserAccount(userId, accountId);
        bankAccountRep.delete(account);
    }

    public String deposit(Long userId, Long accountId, double amount) {
        if (amount <= 0) throw new RuntimeException("Deposit amount must be greater than zero");

        BankAccountEntity account = getUserAccount(userId, accountId);
        validatorRepo.save(new ValidatorEntity(userId,accountId,amount,"DEPOSIT"));
        return "submitted for verification";
    }

    public String withdraw(Long userId, Long accountId, double amount) {
        if (amount <= 0) throw new RuntimeException("Withdrawal amount must be greater than zero");
        BankAccountEntity account = getUserAccount(userId, accountId);

        if (account.getBalance() < amount) throw new RuntimeException("Insufficient balance");
        validatorRepo.save(new ValidatorEntity(userId,accountId,amount,"WITHDRAW"));
        return "submitted for verification";
    }

    private BankAccountEntity getUserAccount(Long userId, Long accountId) {
        WalletEntity wallet = walletRepo.findByUserId(userId).orElseThrow(() -> new RuntimeException("Wallet not found"));
        BankAccountEntity account = bankAccountRep.findById(accountId).orElseThrow(() -> new RuntimeException("Bank account not found"));

        if (!account.getWallet().getId().equals(wallet.getId())) throw new RuntimeException("This account does not belong to you");
        return account;
    }
}