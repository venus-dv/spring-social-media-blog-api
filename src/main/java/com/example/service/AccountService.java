package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

/**
 * Business rules and logic of Accounts
 */
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account registerAccount(Account account) throws IllegalArgumentException, DuplicateUsernameException {
        // Validate that the username is not blank
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be blank");
        }

        // Validate that the password is at least 4 characters long
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters long");
        }

        // Check if the username already exists in the database
        if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
            throw new DuplicateUsernameException("Username already exists");
        }

        // Save the new account to the database
        return accountRepository.save(account);
    }
}
