package com.sample.payment_server.Payment;

import com.sample.payment_server.Transactions.TransactionDateFilterDTO;
import com.sample.payment_server.Transactions.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final TransactionService transactionService;

    public PaymentController(PaymentService paymentService, TransactionService transactionService) {
        this.paymentService = paymentService;
        this.transactionService = transactionService;
    }

    @PutMapping("/transfer")
    public ResponseEntity<?> transferPayment(
            @RequestBody TransferRequestDTO request,
            @AuthenticationPrincipal Jwt jwt) {

        String senderEmail = jwt.getSubject();

        return ResponseEntity.ok(
                paymentService.transfer(
                        senderEmail,
                        request.getReceiverWalletId(),
                        request.getAmount(),
                        request.getDescription()
                )
        );
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAllTransaction(
            @AuthenticationPrincipal Jwt jwt) {

        String email = jwt.getSubject();
        Long walletId = paymentService.getSender(email).getWalletId();

        return ResponseEntity.ok(
                transactionService.getAllTransactions(walletId)
        );
    }

    @RequestMapping(value = "/get/filter", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> getAllTransactionsByFilter(
            @AuthenticationPrincipal Jwt jwt,@RequestBody TransactionDateFilterDTO transactionFilterDTO) {

        Long senderId = ((Number)jwt.getClaim("id")).longValue();
        return ResponseEntity.ok(transactionService.getAllTransactionsByFilter(senderId,transactionFilterDTO));
    }

    @GetMapping("/get/recent")
    public ResponseEntity<?> getRecentTransaction(
            @AuthenticationPrincipal Jwt jwt) {

        String email = jwt.getSubject();
        Long walletId = paymentService.getSender(email).getWalletId();

        return ResponseEntity.ok(
                transactionService.getRecentTransactions(walletId)
        );
    }
}