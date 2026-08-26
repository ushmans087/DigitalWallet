package com.sample.wallet_server.Wallet;

import com.sample.wallet_server.WalletDTO.InterTransferRequestDTO;
import com.sample.wallet_server.WalletDTO.PaymentResponseDTO;
import com.sample.wallet_server.WalletDTO.WalletCreateRequestDTO;
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

    @PostMapping("/create")
    public ResponseEntity<?> createWallet(@RequestBody WalletCreateRequestDTO request) {
        return ResponseEntity.ok(walletSer.createWallet(request.getUserId(), request.getUsername(), request.getEmail()));
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
        return ResponseEntity.ok(walletSer.walletToBank(userId,request.getAmount(), request.getAccountId()));
    }

    @PostMapping("/transfer/wallet")
    public ResponseEntity<?> transferWallet(@AuthenticationPrincipal Jwt jwt,@RequestBody InterTransferRequestDTO request) {
        Long userId = jwt.getClaim("id");
        return ResponseEntity.ok(walletSer.bankToWallet(userId,request.getAccountId(),request.getAmount()));
    }

    @PutMapping("/update/balance")
    public ResponseEntity<String> updateBalance(@RequestBody WalletUpdateRequestDTO dto) {
        System.out.println("Reaching here + user : " + dto.getWalletId() + " amount : " + dto.getAmount());
        return ResponseEntity.ok(walletSer.updateBalance(dto));
    }
}