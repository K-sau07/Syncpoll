package com.syncpoll.model.dto.request;

import jakarta.validation.constraints.NotNull;

public class SubmitAnswerRequest {

    @NotNull(message = "Option ID is required")
    private Long optionId;

    public SubmitAnswerRequest() {}

    public SubmitAnswerRequest(Long optionId) {
        this.optionId = optionId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }
}
