package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

/**
 * Business rules and logic of Messages
 */
@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountService accountService;

    /**
     * Creates a new message
     * 
     * Creation will be successful only if
     * - message is not blank
     * - message <= 255 characters
     * - postedBy refers to an existing user
     * 
     * @param message The message object containing the information needed to create
     *                a message
     * @return The newly created message object with its generated messageId
     */
    public Message createMessage(Message message) {
        String messageText = message.getMessageText();
        if (messageText == null || messageText.trim().isEmpty() || messageText.length() > 255) {
            throw new IllegalArgumentException("");
        }

        // Validate that the postedBy account exists
        Integer postedBy = message.getPostedBy();
        if (accountService.getAccountById(postedBy) == null) {
            throw new IllegalArgumentException("");
        }

        // If all validation passes, save the message
        return messageRepository.save(message);
    }
}
