package com.syncpoll.model.dto.response;

import com.syncpoll.model.entity.JoinMode;
import com.syncpoll.model.entity.Session;
import com.syncpoll.model.entity.SessionStatus;

import java.time.LocalDateTime;

public class SessionResponse {

    private Long id;
    private String title;
    private String description;
    private String joinCode;
    private JoinMode joinMode;
    private SessionStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private LocalDateTime createdAt;
    private int participantCount;
    private int pollCount;

    public SessionResponse() {}

    public static SessionResponse fromEntity(Session session) {
        SessionResponse response = new SessionResponse();
        response.setId(session.getId());
        response.setTitle(session.getTitle());
        response.setDescription(session.getDescription());
        response.setJoinCode(session.getJoinCode());
        response.setJoinMode(session.getJoinMode());
        response.setStatus(session.getStatus());
        response.setStartedAt(session.getStartedAt());
        response.setEndedAt(session.getEndedAt());
        response.setCreatedAt(session.getCreatedAt());
        response.setParticipantCount(session.getParticipants().size());
        response.setPollCount(session.getPolls().size());
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJoinCode() {
        return joinCode;
    }

    public void setJoinCode(String joinCode) {
        this.joinCode = joinCode;
    }

    public JoinMode getJoinMode() {
        return joinMode;
    }

    public void setJoinMode(JoinMode joinMode) {
        this.joinMode = joinMode;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getParticipantCount() {
        return participantCount;
    }

    public void setParticipantCount(int participantCount) {
        this.participantCount = participantCount;
    }

    public int getPollCount() {
        return pollCount;
    }

    public void setPollCount(int pollCount) {
        this.pollCount = pollCount;
    }
}
