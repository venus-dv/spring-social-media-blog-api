package com.example.controller;

import com.example.entity.*;
import com.example.service.AccountService;
import com.example.service.MessageService;
import com.example.exception.*;

import org.hibernate.mapping.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

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
     *                the messageId).
     * @return A ResponseEntity containing the created Message object if successful,
     *         or an appropriate error status.
     */
    @PostMapping("/messages")
    public ResponseEntity<Message> postMessage(@RequestBody Message message) {
        try {
            Message createdMessage = messageService.createMessage(message);

            return new ResponseEntity<>(createdMessage, HttpStatus.OK); // Return the newly created message with 200 OK
                                                                        // status

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Handles the retrieval of all messages
     * 
     * @return A ResponseEntity containing a list of the created Message objects if
     *         successful,
     *         or an empty list.
     */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();

        return new ResponseEntity<>(messages, HttpStatus.OK); // Return the list of messages with 200 OK status
    }

    /**
     * Handles the retrieval of a message by its messageId
     * 
     * @param messageId The unique identifier of the message to be retrieved
     * @return A ResponseEntity containing the Message object if
     *         successful, or empty.
     */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        Optional<Message> message = messageService.getMessageById(messageId);

        return message.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.OK));
    }

    /**
     * Handles the deletion of a message
     * 
     * @param messageId The unique identifier of the message to be deleted
     * @return A ResponseEntity containing an integer 1 if deleted, 0 if not found
     */
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable Integer messageId) {
        int rowsAffected = messageService.deleteMessage(messageId);
        if (rowsAffected == 0) {
            return new ResponseEntity<>(HttpStatus.OK); // Return 200 with empty body
        }
        return ResponseEntity.status(HttpStatus.OK).body(rowsAffected);
    }

    /**
     * Handles updating a message
     * 
     * @param messageId      The unique identifier of the message to be updated
     * @param newMessageText The new text for the message
     * @return A ResponseEntity containing an integer 1 if updated successfully
     */
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<String> updateMessageText(
            @PathVariable Integer messageId, 
            @RequestBody Map<String, String> body) {
        String newMessageText = body.get("messageText");
        try {
            // Validate the message text
            if (newMessageText == null || newMessageText.isBlank() || newMessageText.length() > 255) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // Call the MessageService to update the message text
            int rowsAffected = messageService.updateMessageText(messageId, newMessageText);
            return new ResponseEntity<>(String.valueOf(rowsAffected), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            // Return 400 Bad Request for invalid input
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
