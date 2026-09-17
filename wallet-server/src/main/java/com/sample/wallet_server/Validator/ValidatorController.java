package com.sample.wallet_server.Validator;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/wallet")
public class ValidatorController {

    private final ValidatorService validatorService;
    private final ValidatorRepo validatorRepo;

    public ValidatorController(ValidatorService validatorService, ValidatorRepo validatorRepo) {
        this.validatorService = validatorService;
        this.validatorRepo = validatorRepo;
    }

    @GetMapping("/validator/pending")
    public ResponseEntity<?> getPending() {
        return ResponseEntity.ok(validatorService.getPending());
    }

    @PutMapping("/validator/{id}/verify")
    public ResponseEntity<?> verifyPending(@PathVariable Long id) {

        Optional<ValidatorEntity> validatorEntity = validatorRepo.findById(id);
        if(validatorEntity.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(validatorService.verifyPending(id,validatorEntity.get()));
    }

    @PutMapping("/validator/{id}/reject")
    public ResponseEntity<?> rejectPending(@PathVariable Long id) {
        return ResponseEntity.ok(validatorService.rejectPending(id));
    }
}