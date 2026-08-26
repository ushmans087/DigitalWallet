package com.sample.wallet_server.Bank;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepo extends JpaRepository<BankAccountEntity,Long> {

}
