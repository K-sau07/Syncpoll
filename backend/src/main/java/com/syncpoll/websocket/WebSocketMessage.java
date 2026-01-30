package com.syncpoll.websocket;

import java.time.LocalDateTime;

/**
 * Generic wrapper for all WebSocket messages.
 * Every event has a type and payload.
 */
public class WebSocketMessage<T> {

    private EventType type;
    private T payload;
    private LocalDateTime timestamp;

    public WebSocketMessage() {
        this.timestamp = LocalDateTime.now();
    }

    public WebSocketMessage(EventType type, T payload) {
        this.type = type;
        this.payload = payload;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> WebSocketMessage<T> of(EventType type, T payload) {
        return new WebSocketMessage<>(type, payload);
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
