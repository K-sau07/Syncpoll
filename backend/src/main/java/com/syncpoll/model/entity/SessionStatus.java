package com.syncpoll.model.entity;

/**
 * Lifecycle status of a session.
 * ACTIVE - session is running, participants can join and answer
 * ENDED - session is over, no more answers accepted
 * ARCHIVED - session is hidden from default views
 */
public enum SessionStatus {
    ACTIVE,
    ENDED,
    ARCHIVED
}
