package com.sample.wallet_server.Validator;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
public class ValidatorController {

    private final ValidatorService validatorService;

    public ValidatorController(ValidatorService validatorService) {
        this.validatorService = validatorService;
    }

    @GetMapping("/validator/pending")
    public ResponseEntity<?> getPending() {
        return ResponseEntity.ok(validatorService.getPending());
    }

    @PutMapping("/validator/{id}/verify")
    public ResponseEntity<?> verifyPending(@PathVariable Long id) {
        return ResponseEntity.ok(validatorService.verifyPending(id));
    }

    @PutMapping("/validator/{id}/reject")
    public ResponseEntity<?> rejectPending(@PathVariable Long id) {
        return ResponseEntity.ok(validatorService.rejectPending(id));
    }
}