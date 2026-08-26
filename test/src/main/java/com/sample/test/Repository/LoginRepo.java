package com.sample.test.Repository;

import com.sample.test.Model.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepo extends JpaRepository<user, String> {
    Optional<user> findByEmail(String email);
}
