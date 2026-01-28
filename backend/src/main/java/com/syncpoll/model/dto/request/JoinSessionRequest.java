package com.syncpoll.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class JoinSessionRequest {

    @NotBlank(message = "Join code is required")
    @Size(min = 6, max = 10, message = "Join code must be between 6 and 10 characters")
    private String joinCode;

    @NotBlank(message = "Display name is required")
    @Size(max = 100, message = "Display name must be less than 100 characters")
    private String displayName;

    public JoinSessionRequest() {}

    public JoinSessionRequest(String joinCode, String displayName) {
        this.joinCode = joinCode;
        this.displayName = displayName;
    }

    public String getJoinCode() {
        return joinCode;
    }

    public void setJoinCode(String joinCode) {
        this.joinCode = joinCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
