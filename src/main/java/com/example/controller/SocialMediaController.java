package com.example.controller;

import com.example.entity.*;
import com.example.service.AccountService;

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
 * You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */

@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @PostMapping(value = "/register")
    public ResponseEntity<Account> registerAccount(@RequestBody Account account) {
        try {
            // Call the AccountService to handle registration
            Account createdAccount = accountService.registerAccount(account);
            return new ResponseEntity<>(createdAccount, HttpStatus.OK); // Return 200 OK if successful
        } catch (DuplicateUsernameException e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT); // Return 409 if username already exists
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); // Return 400 for validation errors
        }
    }
}
