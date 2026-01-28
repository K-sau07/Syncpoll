package com.syncpoll.model.dto.request;

import com.syncpoll.model.entity.JoinMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateSessionRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    private JoinMode joinMode = JoinMode.OPEN;

    public CreateSessionRequest() {}

    public CreateSessionRequest(String title, String description, JoinMode joinMode) {
        this.title = title;
        this.description = description;
        this.joinMode = joinMode;
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

    public JoinMode getJoinMode() {
        return joinMode;
    }

    public void setJoinMode(JoinMode joinMode) {
        this.joinMode = joinMode;
    }
}
