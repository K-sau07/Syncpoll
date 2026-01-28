package com.syncpoll.model.dto.response;

import com.syncpoll.model.entity.PollOption;

public class OptionResponse {

    private Long id;
    private String text;
    private int order;

    public OptionResponse() {}

    public static OptionResponse fromEntity(PollOption option) {
        OptionResponse response = new OptionResponse();
        response.setId(option.getId());
        response.setText(option.getOptionText());
        response.setOrder(option.getOptionOrder());
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
