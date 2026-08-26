package com.sample.wallet_server.Validator;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValidatorRepo extends JpaRepository<ValidatorEntity, Long> {
    List<ValidatorEntity> findByStatus(String status);
}
