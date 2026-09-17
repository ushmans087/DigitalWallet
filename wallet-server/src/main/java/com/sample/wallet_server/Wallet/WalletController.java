package com.sample.wallet_server.Wallet;

import com.sample.wallet_server.WalletDTO.InterTransferRequestDTO;
import com.sample.wallet_server.WalletDTO.PaymentResponseDTO;
import com.sample.wallet_server.WalletDTO.WalletUpdateRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    private final WalletService walletSer;
    public WalletController(WalletService walletSer) {
        this.walletSer = walletSer;
    }

    @GetMapping("/findby/email/{email}")
    public ResponseEntity<PaymentResponseDTO> getWallet(@PathVariable String email) {
        return ResponseEntity.ok(walletSer.getWalletByEmail(email));
    }

    @GetMapping("/findby/id/{id}")
    public ResponseEntity<PaymentResponseDTO> getWalletById(@PathVariable Long id) {
        return ResponseEntity.ok(walletSer.getWalletByWalletId(id));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getWallet(@AuthenticationPrincipal Jwt jwt) {
        Long userId = jwt.getClaim("id");
        return ResponseEntity.ok(walletSer.getWallet(userId));
    }

    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(@AuthenticationPrincipal Jwt jwt) {
        Long userId = jwt.getClaim("id");
        return ResponseEntity.ok(walletSer.getBalance(userId));
    }

    @PostMapping("/transfer/bank")
    public ResponseEntity<?> transferBank(@AuthenticationPrincipal Jwt jwt,@RequestBody InterTransferRequestDTO request) {
        Long userId = jwt.getClaim("id");
        String email = jwt.getSubject();
        return ResponseEntity.ok(walletSer.walletToBank(userId,email,request.getAmount(), request.getAccountId()));
    }

    @PostMapping("/transfer/wallet")
    public ResponseEntity<?> transferWallet(@AuthenticationPrincipal Jwt jwt,@RequestBody InterTransferRequestDTO request) {
        Long userId = jwt.getClaim("id");
        String email = jwt.getSubject();
        return ResponseEntity.ok(walletSer.bankToWallet(userId,email,request.getAccountId(),request.getAmount()));
    }

    @PutMapping("/update/balance")
    public ResponseEntity<String> updateBalance(@RequestBody WalletUpdateRequestDTO dto) {
        return ResponseEntity.ok(walletSer.updateBalance(dto));
    }
}