package com.sample.wallet_server.Verification;

import com.sample.wallet_server.Bank.BankAccountEntity;
import com.sample.wallet_server.Bank.BankAccountRepo;
import com.sample.wallet_server.Validator.ValidatorEntity;
import com.sample.wallet_server.Wallet.WalletEntity;
import com.sample.wallet_server.Wallet.WalletRepo;
import org.springframework.stereotype.Service;

@Service
public class VerificationService {

    private final WalletRepo walletRepo;
    private final BankAccountRepo bankAccountRepo;

    public VerificationService(WalletRepo walletRepo, BankAccountRepo bankAccountRepo) {
        this.walletRepo = walletRepo;
        this.bankAccountRepo = bankAccountRepo;
    }

    public boolean verifyBankToWallet(WalletEntity wallet,BankAccountEntity account,double amount) {

        if (!account.getWallet().getId().equals(wallet.getId())) return false;
        if (!account.isVerified()) return false;
        if (amount <= 0 || account.getBalance() < amount) return false;

        return true;
    }

    public boolean verifyWalletToBank(WalletEntity wallet,BankAccountEntity account,double amount){

        if (!account.getWallet().getId().equals(wallet.getId())) return false;
        if (!account.isVerified()) return false;
        if (amount <= 0 || wallet.getBalance() < amount) return false;

        return true;
    }

    public boolean verify(ValidatorEntity request) {

        if (request.getAmount() <= 0) return false;
        BankAccountEntity account = bankAccountRepo.findById(request.getBankAccountId()).orElse(null);

        if (account == null) return false;
        if (!account.getWallet().getUserId().equals(request.getUserId())) return false;
        if (!request.getType().equals("DEPOSIT") && !request.getType().equals("WITHDRAW")) return false;
        if (request.getType().equals("WITHDRAW") && account.getBalance() < request.getAmount()) return false;

        return true;
    }
}
