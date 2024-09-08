package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.*;
import com.example.repository.AccountRepository;

/**
 * Business rules and logic of Accounts
 */
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Registers a new account if the provided username and password meet the
     * validation criteria.
     * 
     * The registration will be successful if:
     * - The username is not blank.
     * - The password is at least 4 characters long.
     * - An account with the given username does not already exist in the database.
     * 
     * @param account The account object containing the username and password to
     *                register
     * @return The newly created account object with its generated accountId
     * @throws IllegalArgumentException   if an account with the given username
     *                                    already exists.
     * @throws DuplicateUsernameException if the username is blank or the password
     *                                    is less than 4 characters.
     */
    public Account registerAccount(Account account) throws IllegalArgumentException, DuplicateUsernameException {
        // Validate that the username is not blank
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("");
        }

        // Validate that the password is at least 4 characters long
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            throw new IllegalArgumentException("");
        }

        // Check if the username already exists in the database
        if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
            throw new DuplicateUsernameException();
        }

        // Save the new account to the database
        return accountRepository.save(account);
    }

    /**
     * Verifies login credentials
     * 
     * Login is successful only if the username and password provided match a real
     * account within the database
     * 
     * @param username The user's username
     * @param password The user's password
     * @return the account object with its accountId
     * @throws UnauthorizedException if access is unauthorized with the given
     *                               username and password
     */
    public Account login(String username, String password) throws UnauthorizedException {
        Optional<Account> optionalAccount = accountRepository.findByUsername(username);

        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();

            // Check if the password matches
            if (account.getPassword().equals(password)) {
                return account;
            }
        }

        // Throw an exception if login is unsuccessful
        throw new UnauthorizedException();
    }

    /**
     * Retrieves an Account from the database by its accountId.
     * 
     * @param accountId The unique identifier of the account to be retrieved.
     * @return The Account object associated with the provided account ID, or null
     *         if no account with that ID exists.
     */
    public Account getAccountById(Integer accountId) {
        return accountRepository.findById(accountId).orElse(null);
    }
}
