package com.sample.test.Repository;

import com.sample.test.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepo extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}
