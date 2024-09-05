package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Account;

public interface AccountRepository {
    public interface AccountRepository extends JpaRepository<Account, Integer> {
        Optional<Account> findByUsername(String username); // Finds an account by username
    }
}
