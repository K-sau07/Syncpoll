package com.syncpoll.exception;

public class SessionNotFoundException extends RuntimeException {
    
    public SessionNotFoundException(String message) {
        super(message);
    }
    
    public SessionNotFoundException(Long sessionId) {
        super("Session not found with id: " + sessionId);
    }
    
    public SessionNotFoundException(String field, String value) {
        super("Session not found with " + field + ": " + value);
    }
}
