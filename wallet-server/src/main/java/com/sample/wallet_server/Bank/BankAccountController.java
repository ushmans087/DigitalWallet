package com.sample.wallet_server.Bank;

import com.sample.wallet_server.BankDTO.BankAccountRequestDTO;
import com.sample.wallet_server.BankDTO.BankBalanceUpdateDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallet")
public class BankAccountController {

    private final bankAccountService bankAccountService;

    public BankAccountController(bankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<BankAccountEntity>> getBankAccounts(@AuthenticationPrincipal Jwt jwt) {

        Long userId = jwt.getClaim("id");
        return ResponseEntity.ok(bankAccountService.getAllAccounts(userId));
    }

    @PostMapping("/accounts")
    public ResponseEntity<?> addBankAccount(@AuthenticationPrincipal Jwt jwt, @RequestBody BankAccountRequestDTO request) {

        Long userId = jwt.getClaim("id");
        BankAccountEntity account = bankAccountService.addAccount(userId, request);
        return ResponseEntity.ok(account);
    }

    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<?> deleteBankAccount(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {

        Long userId = jwt.getClaim("id");
        bankAccountService.deleteAccount(userId, id);
        return ResponseEntity.ok("Bank account deleted successfully");
    }

    @PutMapping("/accounts/{id}/deposit")
    public ResponseEntity<?> depositBankAccount(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id, @RequestBody BankBalanceUpdateDTO request) {

        Long userId = jwt.getClaim("id");
        return ResponseEntity.ok(bankAccountService.deposit(userId, id, request.getAmount()));
    }

    @PutMapping("/accounts/{id}/withdraw")
    public ResponseEntity<?> withdrawBankAccount(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id, @RequestBody BankBalanceUpdateDTO request) {

        Long userId = jwt.getClaim("id");
        return ResponseEntity.ok(bankAccountService.withdraw(userId, id, request.getAmount()));
    }
}