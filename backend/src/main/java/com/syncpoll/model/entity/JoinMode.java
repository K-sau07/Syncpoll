package com.syncpoll.model.entity;

/**
 * Determines how participants can join a session.
 * OPEN - anyone can join with just a name
 * VERIFIED - participants must login with Google
 */
public enum JoinMode {
    OPEN,
    VERIFIED
}
