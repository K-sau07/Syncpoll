package com.syncpoll.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "answers", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"poll_id", "participant_id"})
})
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id", nullable = false)
    private Poll poll;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable = false)
    private Participant participant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_option_id", nullable = false)
    private PollOption selectedOption;

    @Column(name = "answered_at")
    private LocalDateTime answeredAt;

    public Answer() {}

    public Answer(Poll poll, Participant participant, PollOption selectedOption) {
        this.poll = poll;
        this.participant = participant;
        this.selectedOption = selectedOption;
    }

    @PrePersist
    protected void onCreate() {
        this.answeredAt = LocalDateTime.now();
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public PollOption getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(PollOption selectedOption) {
        this.selectedOption = selectedOption;
    }

    public LocalDateTime getAnsweredAt() {
        return answeredAt;
    }
}
