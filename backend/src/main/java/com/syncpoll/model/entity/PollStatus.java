package com.syncpoll.model.entity;

/**
 * Lifecycle of a poll within a session.
 * DRAFT - created but not yet shown to participants
 * LIVE - currently accepting answers
 * CLOSED - no more answers, results locked in
 */
public enum PollStatus {
    DRAFT,
    LIVE,
    CLOSED
}
