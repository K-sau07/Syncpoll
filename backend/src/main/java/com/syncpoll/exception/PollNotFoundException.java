package com.syncpoll.exception;

public class PollNotFoundException extends RuntimeException {
    
    public PollNotFoundException(String message) {
        super(message);
    }
    
    public PollNotFoundException(Long pollId) {
        super("Poll not found with id: " + pollId);
    }
}
