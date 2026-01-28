package com.syncpoll.model.dto.request;

import com.syncpoll.model.entity.PollType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public class CreatePollRequest {

    @NotBlank(message = "Question is required")
    @Size(max = 500, message = "Question must be less than 500 characters")
    private String question;

    private PollType pollType = PollType.MULTIPLE_CHOICE;

    @NotEmpty(message = "At least one option is required")
    @Size(min = 2, max = 10, message = "Poll must have between 2 and 10 options")
    private List<String> options;

    private boolean showResults = true;

    public CreatePollRequest() {}

    public CreatePollRequest(String question, List<String> options) {
        this.question = question;
        this.options = options;
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

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public boolean isShowResults() {
        return showResults;
    }

    public void setShowResults(boolean showResults) {
        this.showResults = showResults;
    }
}
