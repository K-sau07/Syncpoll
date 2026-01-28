package com.syncpoll.model.dto.response;

import java.util.List;

public class PollResultResponse {

    private Long pollId;
    private String question;
    private int totalVotes;
    private List<OptionResultResponse> results;

    public PollResultResponse() {}

    public Long getPollId() {
        return pollId;
    }

    public void setPollId(Long pollId) {
        this.pollId = pollId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(int totalVotes) {
        this.totalVotes = totalVotes;
    }

    public List<OptionResultResponse> getResults() {
        return results;
    }

    public void setResults(List<OptionResultResponse> results) {
        this.results = results;
    }

    public static class OptionResultResponse {
        private Long optionId;
        private String text;
        private int votes;
        private double percentage;

        public OptionResultResponse() {}

        public OptionResultResponse(Long optionId, String text, int votes, double percentage) {
            this.optionId = optionId;
            this.text = text;
            this.votes = votes;
            this.percentage = percentage;
        }

        public Long getOptionId() {
            return optionId;
        }

        public void setOptionId(Long optionId) {
            this.optionId = optionId;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getVotes() {
            return votes;
        }

        public void setVotes(int votes) {
            this.votes = votes;
        }

        public double getPercentage() {
            return percentage;
        }

        public void setPercentage(double percentage) {
            this.percentage = percentage;
        }
    }
}
