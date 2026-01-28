package com.syncpoll.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "polls")
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @Enumerated(EnumType.STRING)
    @Column(name = "poll_type", nullable = false, length = 20)
    private PollType pollType = PollType.MULTIPLE_CHOICE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PollStatus status = PollStatus.DRAFT;

    // Whether to show results to participants after they answer
    @Column(name = "show_results")
    private boolean showResults = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "opened_at")
    private LocalDateTime openedAt;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("optionOrder ASC")
    private List<PollOption> options = new ArrayList<>();

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL)
    private List<Answer> answers = new ArrayList<>();

    public Poll() {}

    public Poll(Session session, String question) {
        this.session = session;
        this.question = question;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public void goLive() {
        this.status = PollStatus.LIVE;
        this.openedAt = LocalDateTime.now();
    }

    public void close() {
        this.status = PollStatus.CLOSED;
        this.closedAt = LocalDateTime.now();
    }

    public boolean isLive() {
        return this.status == PollStatus.LIVE;
    }

    public boolean acceptingAnswers() {
        return this.status == PollStatus.LIVE;
    }

    public void addOption(String optionText) {
        PollOption option = new PollOption(this, optionText, this.options.size());
        this.options.add(option);
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
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

    public LocalDateTime getOpenedAt() {
        return openedAt;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public List<PollOption> getOptions() {
        return options;
    }

    public void setOptions(List<PollOption> options) {
        this.options = options;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
