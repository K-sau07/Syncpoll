package com.syncpoll.model.dto.response;

import com.syncpoll.model.entity.Attendance;

import java.time.LocalDateTime;

public class AttendanceResponse {

    private Long id;
    private Long participantId;
    private String participantName;
    private LocalDateTime joinedAt;
    private LocalDateTime leftAt;
    private int answersCount;

    public AttendanceResponse() {}

    public static AttendanceResponse fromEntity(Attendance attendance) {
        AttendanceResponse response = new AttendanceResponse();
        response.setId(attendance.getId());
        response.setParticipantId(attendance.getParticipant().getId());
        response.setParticipantName(attendance.getParticipant().getDisplayName());
        response.setJoinedAt(attendance.getJoinedAt());
        response.setLeftAt(attendance.getLeftAt());
        response.setAnswersCount(attendance.getAnswersCount());
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public LocalDateTime getLeftAt() {
        return leftAt;
    }

    public void setLeftAt(LocalDateTime leftAt) {
        this.leftAt = leftAt;
    }

    public int getAnswersCount() {
        return answersCount;
    }

    public void setAnswersCount(int answersCount) {
        this.answersCount = answersCount;
    }
}
