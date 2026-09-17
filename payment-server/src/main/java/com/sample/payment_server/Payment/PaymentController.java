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
    public ResponseEntity<?> transferPayment(@RequestBody TransferRequestDTO request, @AuthenticationPrincipal Jwt jwt) {
        String senderEmail = jwt.getSubject();
        return ResponseEntity.ok(paymentService.transfer(senderEmail, request.getReceiverWalletId(), request.getAmount(), request.getDescription()));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAllTransaction(@AuthenticationPrincipal Jwt jwt,@RequestParam(defaultValue = "0") int pageNumber,@RequestParam(defaultValue = "10") int limit) {
        // u have to check first if account exist or not

        Long userId = ((Number) jwt.getClaim("id")).longValue();
        return ResponseEntity.ok(transactionService.getAllTransactions(userId,pageNumber,limit));
    }

    @RequestMapping(value = "/get/filter", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> getAllTransactionsByFilter(@AuthenticationPrincipal Jwt jwt,@RequestBody TransactionDateFilterDTO transactionFilterDTO) {

        // u have to check first if account exist or not , dont trust jwt

        Long userId = ((Number)jwt.getClaim("id")).longValue();
        return ResponseEntity.ok(transactionService.getAllTransactionsByFilter(userId,transactionFilterDTO));
    }

    @GetMapping("/get/recent")
    public ResponseEntity<?> getRecentTransaction(@AuthenticationPrincipal Jwt jwt,@RequestParam(defaultValue = "0") int pageNumber,@RequestParam(defaultValue = "10") int limit) {

        // u have to check first if account exist or not

        Long userId = ((Number)jwt.getClaim("id")).longValue();
        return ResponseEntity.ok(transactionService.getRecentTransactions(userId,pageNumber,limit));
    }
}