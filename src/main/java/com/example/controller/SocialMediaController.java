package com.example.controller;

import com.example.entity.*;
import com.example.service.AccountService;
import com.example.service.MessageService;
import com.example.exception.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    /**
     * Handles the registration of a new user account.
     * 
     * @param account The account details provided in the request body (without the
     *                accountId).
     * @return A ResponseEntity containing the created Account object if successful,
     *         or an appropriate error status.
     */
    @PostMapping(value = "/register")
    public ResponseEntity<Account> postRegisterAccount(@RequestBody Account account) {
        try {
            Account createdAccount = accountService.registerAccount(account);
            return new ResponseEntity<>(createdAccount, HttpStatus.OK); // Return 200 OK if successful

        } catch (DuplicateUsernameException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Return 409 if username already exists

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Return 400 for validation errors
        }
    }

    /**
     * Handles the login for a user
     * 
     * @param loginRequest the account details provided in the request body (without
     *                     the acountId).
     * @return A ResponseEntity containing the Account object if successful,
     *         or an appropriate error status.
     */
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account loginRequest) {
        try {
            Account account = accountService.login(loginRequest.getUsername(), loginRequest.getPassword());

            return new ResponseEntity<>(account, HttpStatus.OK); // If successful, return the account and 200 OK

        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Return 401 Unauthorized if login fails
        }
    }

    /**
     * Handles the creation of messages
     * 
     * @param message the message details provided in the request body (without
     *                     the messageId).
     * @return A ResponseEntity containing the created Message object if successful,
     *         or an appropriate error status.
     */
    @PostMapping("/messages")
    public ResponseEntity<Message> postMessage(@RequestBody Message message) {
        try {
            Message createdMessage = messageService.createMessage(message);

            // Return the newly created message with 200 OK status
            return new ResponseEntity<>(createdMessage, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
