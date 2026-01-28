package com.syncpoll.model.dto.response;

import com.syncpoll.model.entity.Poll;
import com.syncpoll.model.entity.PollStatus;
import com.syncpoll.model.entity.PollType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PollResponse {

    private Long id;
    private String question;
    private PollType pollType;
    private PollStatus status;
    private boolean showResults;
    private LocalDateTime createdAt;
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;
    private List<OptionResponse> options;
    private int totalAnswers;

    public PollResponse() {}

    public static PollResponse fromEntity(Poll poll) {
        PollResponse response = new PollResponse();
        response.setId(poll.getId());
        response.setQuestion(poll.getQuestion());
        response.setPollType(poll.getPollType());
        response.setStatus(poll.getStatus());
        response.setShowResults(poll.isShowResults());
        response.setCreatedAt(poll.getCreatedAt());
        response.setOpenedAt(poll.getOpenedAt());
        response.setClosedAt(poll.getClosedAt());
        response.setOptions(poll.getOptions().stream()
                .map(OptionResponse::fromEntity)
                .collect(Collectors.toList()));
        response.setTotalAnswers(poll.getAnswers().size());
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public PollType getPollType() {
        return pollType;
    }

    public void setPollType(PollType pollType) {
        this.pollType = pollType;
    }

    public PollStatus getStatus() {
        return status;
    }

    public void setStatus(PollStatus status) {
        this.status = status;
    }

    public boolean isShowResults() {
        return showResults;
    }

    public void setShowResults(boolean showResults) {
        this.showResults = showResults;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getOpenedAt() {
        return openedAt;
    }

    public void setOpenedAt(LocalDateTime openedAt) {
        this.openedAt = openedAt;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }

    public List<OptionResponse> getOptions() {
        return options;
    }

    public void setOptions(List<OptionResponse> options) {
        this.options = options;
    }

    public int getTotalAnswers() {
        return totalAnswers;
    }

    public void setTotalAnswers(int totalAnswers) {
        this.totalAnswers = totalAnswers;
    }
}
