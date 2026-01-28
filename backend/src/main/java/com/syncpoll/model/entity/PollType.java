package com.syncpoll.model.entity;

/**
 * Types of polls we support.
 * Starting simple with multiple choice, can add more later.
 */
public enum PollType {
    MULTIPLE_CHOICE,
    WORD_CLOUD,
    OPEN_ENDED,
    RATING
}
