package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

import java.util.List;
import java.util.Optional;

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

    /**
     * Retrieves all messages
     * 
     * @return A list of messages retrieved from the database if they exist, or an
     *         empty list if there are no messages
     */
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    /**
     * Retrieves a message by its id
     * 
     * @param messageId The unique identifier of the message to be retrieved.
     * @return The Message object with its messageId
     */
    public Optional<Message> getMessageById(Integer messageId) {
        return messageRepository.findById(messageId);
    }

    /**
     * Deletes a message
     * 
     * @param messageId The unique identifier of the message to be deleted
     * @return The number of rows affected by the deletion operation (1 if deleted,
     *         0 if not found).
     */
    public int deleteMessage(Integer messageId) {
        if (messageRepository.existsById(messageId)) {

            messageRepository.deleteById(messageId);
            return 1;
        }
        
        return 0;
    }

    /**
     * Updates an existing message
     * 
     * @param messageId The unique identifier of the message to be updated
     * @param newMessageText The new text for the message
     * @return The number of rows updated, 1 if successful
     * @throws IllegalArgumentException If the messageId does not exist or the new text is invalid.
     */
    public int updateMessageText(Integer messageId, String newMessageText) {
        
        if (messageRepository.existsById(messageId)) {
            if (newMessageText != null && !newMessageText.isBlank() && newMessageText.length() <= 255) {
                // Find the message
                Message message = messageRepository.findById(messageId).orElseThrow();
                // Update the message text
                message.setMessageText(newMessageText);
                messageRepository.save(message);
                return 1;
            }
            throw new IllegalArgumentException("");
        }
        throw new IllegalArgumentException("");
    }
}
