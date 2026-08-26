package com.sample.wallet_server.BankDTO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/*
Request to update the bank balance after Deposit and withdraw ,
usually verified and called by an TRANSACTION_VALIDATOR
 */

@Component
@AllArgsConstructor
@NoArgsConstructor
public class BankBalanceUpdateDTO {
    private Double amount;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
