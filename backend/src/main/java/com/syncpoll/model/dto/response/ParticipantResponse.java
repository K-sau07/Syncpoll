package com.syncpoll.model.dto.response;

import com.syncpoll.model.entity.Participant;

import java.time.LocalDateTime;

public class ParticipantResponse {

    private Long id;
    private String displayName;
    private String email;
    private boolean verified;
    private LocalDateTime joinedAt;
    private int answerCount;

    public ParticipantResponse() {}

    public static ParticipantResponse fromEntity(Participant participant) {
        ParticipantResponse response = new ParticipantResponse();
        response.setId(participant.getId());
        response.setDisplayName(participant.getDisplayName());
        response.setEmail(participant.getEmail());
        response.setVerified(participant.isVerified());
        response.setJoinedAt(participant.getJoinedAt());
        response.setAnswerCount(participant.getAnswers().size());
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }
}
