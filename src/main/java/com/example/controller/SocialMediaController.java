package com.example.controller;

import com.example.entity.*;
import com.example.service.AccountService;
import com.example.exception.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use
 * the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations.
 */

@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    /**
     * Handles the registration of a new user account.
     * 
     * This method processes a POST request to create a new account.
     * The request body must contain the account details in JSON format.
     * If the registration is successful, it returns the created account along with
     * an HTTP status of 200 (OK).
     * If the username already exists, it returns an HTTP status of 409 (Conflict).
     * For other validation errors, it returns an HTTP status of 400 (Bad Request).
     * 
     * @param account The account details provided in the request body (without the
     *                accountId).
     * @return A ResponseEntity containing the created Account object if successful,
     *         or an appropriate error status.
     */
    @PostMapping(value = "/register")
    public ResponseEntity<Account> postRegisterAccount(@RequestBody Account account) {
        try {
            // Call the AccountService to handle registration
            Account createdAccount = accountService.registerAccount(account);
            return new ResponseEntity<>(createdAccount, HttpStatus.OK); // Return 200 OK if successful

        } catch (DuplicateUsernameException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Return 409 if username already exists

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Return 400 for validation errors
        }
    }
}
