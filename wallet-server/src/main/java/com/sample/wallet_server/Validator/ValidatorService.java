package com.sample.wallet_server.Validator;

import com.sample.wallet_server.Bank.BankAccountRepo;
import com.sample.wallet_server.Verification.VerificationService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidatorService {

    private final ValidatorRepo validatorRepo;
    private final BankAccountRepo bankAccountRepo;
    private final VerificationService verificationService;

    public ValidatorService(ValidatorRepo validatorRepo, BankAccountRepo bankAccountRepo, VerificationService verificationService) {
        this.validatorRepo = validatorRepo;
        this.bankAccountRepo = bankAccountRepo;
        this.verificationService = verificationService;
    }

    public List<ValidatorEntity> getPending() {
        return validatorRepo.findByStatus("PENDING");
    }

    @CacheEvict(value = "bankAccountById" , key = "#request.getUserId()")
    public boolean verifyPending(Long id,ValidatorEntity request) {

        if (request == null || !request.getStatus().equals("PENDING") || !verificationService.verify(request)) return false;

        var account = bankAccountRepo.findById(request.getBankAccountId()).orElse(null);
        if (account == null) return false;

        if (request.getType().equals("DEPOSIT")) account.setBalance(account.getBalance() + request.getAmount());
        else account.setBalance(account.getBalance() - request.getAmount());

        request.setStatus("VERIFIED");

        bankAccountRepo.save(account);
        validatorRepo.save(request);

        return true;
    }

    public boolean rejectPending(Long id) {
        ValidatorEntity request = validatorRepo.findById(id).orElse(null);

        if (request == null || !request.getStatus().equals("PENDING")) return false;

        request.setStatus("REJECTED");
        validatorRepo.save(request);

        return true;
    }
}