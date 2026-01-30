package com.syncpoll.websocket;

/**
 * Types of events we broadcast over WebSocket.
 */
public enum EventType {
    PARTICIPANT_JOINED,
    PARTICIPANT_LEFT,
    POLL_STARTED,
    POLL_CLOSED,
    POLL_RESULTS_UPDATED,
    SESSION_ENDED
}
