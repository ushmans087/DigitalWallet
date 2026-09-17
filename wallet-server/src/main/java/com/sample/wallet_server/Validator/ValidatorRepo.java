package com.sample.wallet_server.Validator;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValidatorRepo extends JpaRepository<ValidatorEntity, Long> {
    List<ValidatorEntity> findByStatus(String status);

    @Query("SELECT v.userId FROM ValidatorEntity v WHERE v.id = :id")
    Long findUserIdById(@Param("id") Long id);
}
